import {
  MAX_CODE_BYTES,
  NoStoreConfigured,
  newId,
  snippetStore,
  type Snippet
} from '@/lib/snippets'

/** Writes are never cacheable; stated rather than inferred. */
export const dynamic = 'force-dynamic'

/** Matches `2.4.21` and `2.5.0-Beta1`, both of which api.kotlinlang.org serves. */
const VERSION_PATTERN = /^\d+\.\d+\.\d+(-[A-Za-z0-9.]+)?$/

const bad = (message: string, status = 400) =>
  Response.json({ error: message }, { status })

/**
 * Stores a snippet and returns the id its share link is built from.
 *
 * The body is validated by hand rather than with zod: zod is pinned to 4.1.12
 * for nextra-theme-docs' sake (see pnpm-workspace.yaml) and two fields do not
 * justify making this route a second reason that pin can never move.
 */
export async function POST(request: Request) {
  let body: unknown
  try {
    body = await request.json()
  } catch {
    return bad('Body must be JSON.')
  }

  if (typeof body !== 'object' || body === null) return bad('Body must be a JSON object.')
  const { code, kotlinVersion } = body as Record<string, unknown>

  if (typeof code !== 'string') return bad('`code` must be a string.')
  if (code.trim() === '') return bad('`code` must not be empty.')
  if (Buffer.byteLength(code, 'utf8') > MAX_CODE_BYTES) {
    return bad(`\`code\` must be at most ${MAX_CODE_BYTES} bytes.`, 413)
  }
  if (typeof kotlinVersion !== 'string' || !VERSION_PATTERN.test(kotlinVersion)) {
    return bad('`kotlinVersion` must be a version string such as "2.4.21".')
  }

  const snippet: Snippet = { code, kotlinVersion, createdAt: new Date().toISOString() }
  const id = newId()

  try {
    await snippetStore().put(id, snippet)
  } catch (error) {
    if (error instanceof NoStoreConfigured) {
      return bad(error.message, 503)
    }
    throw error
  }

  return Response.json({ id, url: `/p/${id}`, createdAt: snippet.createdAt }, { status: 201 })
}
