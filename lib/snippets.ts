import { randomBytes } from 'node:crypto'
import { Redis } from '@upstash/redis'
import { MAX_CODE_BYTES } from '@/lib/kotlin'

/**
 * A saved playground snippet. `kotlinVersion` records which compiler the author
 * had selected, so reopening the link runs the same one rather than whatever
 * `latestStable` has become.
 */
export type Snippet = {
  code: string
  kotlinVersion: string
  createdAt: string
}

/** Crockford base32 minus the ambiguous letters i, l, o and u. */
const ALPHABET = '0123456789abcdefghjkmnpqrstvwxyz'
const ID_LENGTH = 10
const ID_PATTERN = new RegExp(`^[${ALPHABET}]{${ID_LENGTH}}$`)

export { MAX_CODE_BYTES }

/**
 * `ALPHABET.length` is 32 and 256 divides evenly by 32, so masking a uniform
 * byte with 31 stays uniform — no rejection sampling needed. 10 characters is
 * 50 bits.
 */
export function newId(): string {
  const bytes = randomBytes(ID_LENGTH)
  let id = ''
  for (const byte of bytes) id += ALPHABET[byte & 31]
  return id
}

export function isValidId(id: string): boolean {
  return ID_PATTERN.test(id)
}

const key = (id: string) => `snippet:${id}`

type Store = {
  readonly name: string
  put(id: string, snippet: Snippet): Promise<void>
  get(id: string): Promise<Snippet | null>
}

/**
 * Set `SNIPPET_TTL_DAYS` to expire snippets. Unset means links never rot, which
 * is the point of a share link; the variable exists for whoever is paying for
 * the store.
 */
function ttlSeconds(): number | undefined {
  const days = Number(process.env.SNIPPET_TTL_DAYS)
  return Number.isFinite(days) && days > 0 ? Math.round(days * 86_400) : undefined
}

/**
 * Upstash injects `UPSTASH_REDIS_REST_URL`/`_TOKEN`; a store carried over from
 * the retired Vercel KV is wired up as `KV_REST_API_URL`/`KV_REST_API_TOKEN`.
 * Both are accepted and the credentials are passed to the documented
 * `new Redis({ url, token })` constructor rather than to `Redis.fromEnv()`,
 * which would tie this to one of the two namings.
 */
function redisCredentials(): { url: string; token: string } | null {
  const url = process.env.UPSTASH_REDIS_REST_URL ?? process.env.KV_REST_API_URL
  const token = process.env.UPSTASH_REDIS_REST_TOKEN ?? process.env.KV_REST_API_TOKEN
  return url && token ? { url, token } : null
}

function redisStore(credentials: { url: string; token: string }): Store {
  const redis = new Redis(credentials)
  return {
    name: 'upstash-redis',
    async put(id, snippet) {
      const ttl = ttlSeconds()
      await redis.set(key(id), snippet, ttl ? { ex: ttl } : undefined)
    },
    async get(id) {
      // The REST API returns parsed JSON for values that were stored as objects.
      return (await redis.get<Snippet>(key(id))) ?? null
    }
  }
}

/**
 * Development fallback so the playground's save button works from a clean
 * checkout with no credentials. Deliberately never selected on Vercel, whose
 * filesystem is read-only outside /tmp and whose instances do not share one.
 */
function fileStore(): Store {
  const dir = '.snippets'
  const path = async (id: string) => {
    const { join } = await import('node:path')
    return join(process.cwd(), dir, `${id}.json`)
  }
  return {
    name: 'local-file',
    async put(id, snippet) {
      const { mkdir, writeFile } = await import('node:fs/promises')
      const { join } = await import('node:path')
      await mkdir(join(process.cwd(), dir), { recursive: true })
      await writeFile(await path(id), JSON.stringify(snippet), 'utf8')
    },
    async get(id) {
      const { readFile } = await import('node:fs/promises')
      try {
        return JSON.parse(await readFile(await path(id), 'utf8')) as Snippet
      } catch {
        return null
      }
    }
  }
}

export class NoStoreConfigured extends Error {
  constructor() {
    super(
      'No snippet store is configured. Connect an Upstash Redis store from the ' +
        'Vercel Marketplace, or set UPSTASH_REDIS_REST_URL and UPSTASH_REDIS_REST_TOKEN.'
    )
    this.name = 'NoStoreConfigured'
  }
}

let cached: Store | undefined

/** @throws {NoStoreConfigured} in a deployed environment with no credentials. */
export function snippetStore(): Store {
  if (cached) return cached
  const credentials = redisCredentials()
  if (credentials) return (cached = redisStore(credentials))
  if (process.env.VERCEL) throw new NoStoreConfigured()
  return (cached = fileStore())
}

/** True when saving will work, so the UI can hide the button instead of failing. */
export function snippetStoreAvailable(): boolean {
  return redisCredentials() !== null || !process.env.VERCEL
}
