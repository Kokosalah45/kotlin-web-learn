/**
 * Snippet ids, shared by the route handlers and the browser bundle.
 *
 * Kept out of `lib/snippets.ts` for the same reason `lib/kotlin.ts` is: that
 * module reaches for `@upstash/redis`, and a client component that only needs
 * to mint an id should not drag the Redis client along with it. The browser
 * mints ids when it falls back to its own IndexedDB store, and those ids have
 * to be the same shape as the server's so that `/p/<id>` means one thing.
 */

/** Crockford base32 minus the ambiguous letters i, l, o and u. */
const ALPHABET = '0123456789abcdefghjkmnpqrstvwxyz'
const ID_LENGTH = 10
const ID_PATTERN = new RegExp(`^[${ALPHABET}]{${ID_LENGTH}}$`)

/**
 * `ALPHABET.length` is 32 and 256 divides evenly by 32, so masking a uniform
 * byte with 31 stays uniform — no rejection sampling needed. 10 characters is
 * 50 bits.
 *
 * Web Crypto rather than `node:crypto`, so the same function runs in a route
 * handler and in the browser. `globalThis.crypto` is standard in browsers and
 * in the Node versions Next 16 supports.
 */
export function newId(): string {
  const bytes = crypto.getRandomValues(new Uint8Array(ID_LENGTH))
  let id = ''
  for (const byte of bytes) id += ALPHABET[byte & 31]
  return id
}

export function isValidId(id: string): boolean {
  return ID_PATTERN.test(id)
}
