'use client'

import Link from 'next/link'
import { useCallback, useEffect, useState } from 'react'
import {
  deleteLocalSnippet,
  listLocalSnippets,
  type SavedSnippet
} from '@/lib/local-snippets'

type Props = {
  /**
   * Opens a snippet where the list is shown rather than on a page of its own.
   * The drawer passes this, so a reader can go back to an earlier snippet
   * without leaving the page they are reading. Without it every row is a link
   * to `/p/<id>`.
   */
  onOpen?: (snippet: SavedSnippet) => void
  /** Bumped by the caller after a save, to pull the list again. */
  revision?: number
}

/** The first line with anything on it, which is as much of a name as there is. */
function title(code: string): string {
  const line = code.split('\n').find((candidate) => candidate.trim() !== '')
  return line ? line.trim() : '(empty)'
}

/** Local date, because a snippet is filed where the person who wrote it is. */
function when(createdAt: string): string {
  const date = new Date(createdAt)
  return Number.isNaN(date.getTime()) ? '' : date.toLocaleDateString()
}

/**
 * Everything saved from the playground, newest first.
 *
 * The list is the browser's own: snippets live in IndexedDB (see
 * `lib/local-snippets.ts`), and the server's store deliberately has no index
 * to list. So this is per-browser — it is not a library that follows the
 * reader to another device, and clearing site data empties it.
 */
export function SavedSnippets({ onOpen, revision = 0 }: Props) {
  const [snippets, setSnippets] = useState<SavedSnippet[] | null>(null)
  const [failed, setFailed] = useState(false)

  const refresh = useCallback(() => {
    listLocalSnippets()
      .then(setSnippets)
      .catch(() => {
        // A browser that will not open IndexedDB has nothing to list; say so
        // once rather than leaving an empty list looking like an empty store.
        setFailed(true)
        setSnippets([])
      })
  }, [])

  useEffect(refresh, [refresh, revision])

  const remove = useCallback(
    async (id: string) => {
      // Dropped from the list first: the delete is the reader's own click, and
      // waiting on a round trip to IndexedDB to redraw looks like a dead button.
      setSnippets((current) => current?.filter((snippet) => snippet.id !== id) ?? null)
      try {
        await deleteLocalSnippet(id)
      } finally {
        refresh()
      }
    },
    [refresh]
  )

  if (snippets === null) {
    return (
      <p className="kl-saved__empty" role="status">
        Looking for saved snippets…
      </p>
    )
  }

  if (failed) {
    return (
      <p className="kl-saved__empty">
        This browser will not let the page store anything, so nothing can be saved here.
      </p>
    )
  }

  if (snippets.length === 0) {
    return <p className="kl-saved__empty">Nothing saved yet. Press Save to keep a snippet.</p>
  }

  return (
    <ul className="kl-saved">
      {snippets.map((snippet) => (
        <li key={snippet.id} className="kl-saved__item">
          {onOpen ? (
            <button
              type="button"
              className="kl-saved__open"
              onClick={() => onOpen(snippet)}
              title="Open this snippet in the editor"
            >
              <span className="kl-saved__title">{title(snippet.code)}</span>
              <span className="kl-saved__meta">
                {when(snippet.createdAt)} · Kotlin {snippet.kotlinVersion}
              </span>
            </button>
          ) : (
            <Link className="kl-saved__open" href={`/p/${snippet.id}`}>
              <span className="kl-saved__title">{title(snippet.code)}</span>
              <span className="kl-saved__meta">
                {when(snippet.createdAt)} · Kotlin {snippet.kotlinVersion}
              </span>
            </Link>
          )}
          <button
            type="button"
            className="kl-saved__remove"
            onClick={() => remove(snippet.id)}
            aria-label={`Delete the snippet saved on ${when(snippet.createdAt)}`}
          >
            ×
          </button>
        </li>
      ))}
    </ul>
  )
}
