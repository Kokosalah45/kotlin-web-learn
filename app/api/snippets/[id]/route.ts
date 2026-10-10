import { NoStoreConfigured, isValidId, snippetStore } from '@/lib/snippets'

/**
 * GET route handlers have been dynamic by default since Next 15, but this one
 * reads a store at request time and the rest of the site is prerendered, so it
 * says so outright.
 */
export const dynamic = 'force-dynamic'

export async function GET(_request: Request, ctx: RouteContext<'/api/snippets/[id]'>) {
  const { id } = await ctx.params

  if (!isValidId(id)) {
    return Response.json({ error: 'Not a snippet id.' }, { status: 400 })
  }

  let snippet
  try {
    snippet = await snippetStore().get(id)
  } catch (error) {
    if (error instanceof NoStoreConfigured) {
      return Response.json({ error: error.message }, { status: 503 })
    }
    throw error
  }

  if (!snippet) {
    return Response.json({ error: 'No snippet with that id.' }, { status: 404 })
  }

  return Response.json(snippet, {
    // Immutable: a snippet is written once and never updated.
    headers: { 'Cache-Control': 'public, max-age=31536000, immutable' }
  })
}
