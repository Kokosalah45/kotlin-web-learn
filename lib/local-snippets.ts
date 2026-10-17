import type { Snippet } from '@/lib/snippets'

/**
 * The browser's own snippet store, used when the server has none.
 *
 * `lib/snippets.ts` needs Upstash credentials (or, off Vercel, a writable
 * `.snippets/`); a deployment without either answers 503 and the save button
 * used to be a dead end. Rather than lose the snippet, it is written here and
 * `/p/<id>` reads it back on the same device.
 *
 * What this is not: a share link. IndexedDB is per-origin **and** per-browser
 * profile, so a snippet saved here does not travel to anyone else, to the
 * reader's other devices, or to the same site in a different browser. The UI
 * says so wherever such a link is produced or opened, because a link that
 * silently works for only one person is worse than no link.
 *
 * It is also not permanent. Without `navigator.storage.persist()` — which
 * would be a storage prompt in exchange for a scratch snippet — the browser
 * may evict the database under storage pressure, and clearing site data takes
 * it with everything else.
 *
 * Imported only by client components. The type import above is erased at
 * compile time, so it does not pull the Redis client into the browser bundle.
 */

const DB_NAME = 'kl-playground'
const DB_VERSION = 1
const STORE = 'snippets'

/** IndexedDB is missing or refuses to open: private mode, a blocking policy. */
export class NoLocalStore extends Error {
  constructor() {
    super('This browser will not let the page store anything locally.')
    this.name = 'NoLocalStore'
  }
}

function openDatabase(): Promise<IDBDatabase> {
  return new Promise((resolve, reject) => {
    if (typeof indexedDB === 'undefined') {
      reject(new NoLocalStore())
      return
    }

    let request: IDBOpenDBRequest
    try {
      request = indexedDB.open(DB_NAME, DB_VERSION)
    } catch {
      // Firefox throws here rather than failing the request in private windows.
      reject(new NoLocalStore())
      return
    }

    request.onupgradeneeded = () => {
      // Keys are passed to put()/get() rather than read from the value, so the
      // stored object stays exactly the `Snippet` the server would have held.
      if (!request.result.objectStoreNames.contains(STORE)) {
        request.result.createObjectStore(STORE)
      }
    }
    request.onsuccess = () => resolve(request.result)
    request.onerror = () => reject(request.error ?? new NoLocalStore())
    // Only fires if another tab holds an older version open; this schema has
    // one version, so it means something is wrong rather than in progress.
    request.onblocked = () => reject(new NoLocalStore())
  })
}

/**
 * Runs one transaction and closes the connection, rather than keeping a
 * database handle around for the life of the page: saving and opening are both
 * one-off actions, and an open connection would block a future upgrade.
 */
async function transact<T>(
  mode: IDBTransactionMode,
  run: (store: IDBObjectStore) => IDBRequest<T>
): Promise<T> {
  const db = await openDatabase()
  try {
    return await new Promise<T>((resolve, reject) => {
      const tx = db.transaction(STORE, mode)
      const request = run(tx.objectStore(STORE))
      // Resolve on the transaction, not the request: for a write, only
      // `oncomplete` means it is durable.
      tx.oncomplete = () => resolve(request.result)
      tx.onerror = () => reject(tx.error ?? new NoLocalStore())
      tx.onabort = () => reject(tx.error ?? new NoLocalStore())
    })
  } finally {
    db.close()
  }
}

/** A stored snippet together with the id it is filed under. */
export type SavedSnippet = Snippet & { id: string }

/** @throws {NoLocalStore} when the browser has nowhere to put it. */
export function putLocalSnippet(id: string, snippet: Snippet): Promise<unknown> {
  return transact('readwrite', (store) => store.put(snippet, id))
}

/** Null when this browser has never held that id. */
export async function getLocalSnippet(id: string): Promise<Snippet | null> {
  const snippet = await transact<Snippet | undefined>('readonly', (store) => store.get(id))
  return snippet ?? null
}

/**
 * Everything this browser holds, newest first.
 *
 * A cursor rather than `getAll()`, which returns the values without the keys
 * the snippets are filed under — and the id is what `/p/<id>` needs. The store
 * is keyed by id, so cursor order is alphabetical by a random string; the sort
 * below is what makes it chronological.
 */
export async function listLocalSnippets(): Promise<SavedSnippet[]> {
  const db = await openDatabase()
  try {
    const snippets = await new Promise<SavedSnippet[]>((resolve, reject) => {
      const found: SavedSnippet[] = []
      const tx = db.transaction(STORE, 'readonly')
      const request = tx.objectStore(STORE).openCursor()
      request.onsuccess = () => {
        const cursor = request.result
        if (!cursor) return
        found.push({ ...(cursor.value as Snippet), id: String(cursor.key) })
        cursor.continue()
      }
      tx.oncomplete = () => resolve(found)
      tx.onerror = () => reject(tx.error ?? new NoLocalStore())
      tx.onabort = () => reject(tx.error ?? new NoLocalStore())
    })
    return snippets.sort((a, b) => b.createdAt.localeCompare(a.createdAt))
  } finally {
    db.close()
  }
}

export function deleteLocalSnippet(id: string): Promise<unknown> {
  return transact('readwrite', (store) => store.delete(id))
}
