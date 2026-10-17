import type { Metadata } from 'next'
import Link from 'next/link'
import { notFound } from 'next/navigation'
import { LocalSnippet } from '@/components/local-snippet'
import { Playground } from '@/components/playground'
import { NoStoreConfigured, isValidId, snippetStore, type Snippet } from '@/lib/snippets'

/**
 * Reads a store per request. Every other route in this app is prerendered, so
 * this is stated rather than left to the default.
 */
export const dynamic = 'force-dynamic'

type PageProps = { params: Promise<{ id: string }> }

/**
 * Null means "not on the server" — no store configured, or a store that has
 * never held this id. The page then hands the id to the browser, which may
 * have saved it in IndexedDB when the server had nowhere to put it. A
 * malformed id is not a snippet anywhere, so that one is still a 404.
 */
async function load(id: string): Promise<Snippet | null> {
  if (!isValidId(id)) notFound()
  try {
    // Read the store directly instead of fetching /api/snippets/[id]: a server
    // component has no origin to fetch from, and the route handler adds nothing
    // but a second hop.
    return await snippetStore().get(id)
  } catch (error) {
    if (error instanceof NoStoreConfigured) return null
    throw error
  }
}

export const metadata: Metadata = {
  title: 'Saved snippet',
  description: 'A Kotlin snippet saved from the playground.',
  // A share link has no lasting value to an index, and the contents are
  // whatever a visitor typed.
  robots: { index: false, follow: false }
}

export default async function SavedSnippetPage({ params }: PageProps) {
  const { id } = await params
  const snippet = await load(id)
  if (!snippet) return <LocalSnippet id={id} />
  const saved = new Date(snippet.createdAt)

  return (
    <article className="kl-snippet-page">
      <h1>Saved snippet</h1>
      <p className="kl-snippet-page__meta">
        Saved <time dateTime={snippet.createdAt}>{saved.toISOString().slice(0, 10)}</time>, to run
        on Kotlin {snippet.kotlinVersion}. Edit it and save again to get a new link — snippets are
        never overwritten.
      </p>

      <Playground
        code={snippet.code}
        version={snippet.kotlinVersion}
        label={`Snippet ${id}`}
        size="tall"
      />

      <p className="kl-snippet-page__meta">
        <Link href="/playground">Open an empty playground</Link> ·{' '}
        <Link href="/">Back to the reference</Link>
      </p>
    </article>
  )
}
