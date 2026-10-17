'use client'

import Link from 'next/link'
import { useEffect, useState } from 'react'
import type { Snippet } from '@/lib/snippets'
import { Playground } from '@/components/playground'
import { getLocalSnippet } from '@/lib/local-snippets'

type Props = { id: string }

type State =
  | { status: 'looking' }
  | { status: 'found'; snippet: Snippet }
  | { status: 'missing' }

/**
 * What `/p/[id]` falls back to when the server has no snippet with that id —
 * either because no store is configured or because that store has never heard
 * of it. Both cases end up here, because a snippet saved while the deployment
 * had no store lives in the reader's own IndexedDB, and the server cannot know
 * that.
 *
 * Rendered rather than `notFound()` so the lookup can happen in the browser;
 * a snippet nobody has is reported here instead, with the same page chrome.
 */
export function LocalSnippet({ id }: Props) {
  const [state, setState] = useState<State>({ status: 'looking' })

  useEffect(() => {
    let cancelled = false
    getLocalSnippet(id)
      .then((snippet) => {
        if (cancelled) return
        setState(snippet ? { status: 'found', snippet } : { status: 'missing' })
      })
      // A browser that will not open IndexedDB cannot be holding the snippet
      // either, so it is missing as far as this page is concerned.
      .catch(() => {
        if (!cancelled) setState({ status: 'missing' })
      })
    return () => {
      cancelled = true
    }
  }, [id])

  if (state.status === 'looking') {
    return (
      <article className="kl-snippet-page">
        <h1>Saved snippet</h1>
        <p className="kl-snippet-page__meta" role="status">
          Looking for this snippet in your browser…
        </p>
      </article>
    )
  }

  if (state.status === 'missing') {
    return (
      <article className="kl-snippet-page">
        <h1>No such snippet</h1>
        <p className="kl-snippet-page__meta">
          Nothing is saved under <code>{id}</code>, here or on the server. A snippet saved
          while this site had no snippet store lives in the browser that saved it, so a link
          like this one does not open anywhere else.
        </p>
        <p className="kl-snippet-page__meta">
          <Link href="/playground">Open an empty playground</Link> ·{' '}
          <Link href="/">Back to the reference</Link>
        </p>
      </article>
    )
  }

  const saved = new Date(state.snippet.createdAt)

  return (
    <article className="kl-snippet-page">
      <h1>Saved snippet</h1>
      <p className="kl-snippet-page__meta">
        Saved <time dateTime={state.snippet.createdAt}>{saved.toISOString().slice(0, 10)}</time>
        , to run on Kotlin {state.snippet.kotlinVersion}. Edit it and save again to get a new
        link — snippets are never overwritten.
      </p>
      <p className="kl-snippet-page__meta kl-snippet-page__local">
        Stored in this browser, not on the server: this site has no snippet store, so the
        link works on this device only, and clearing site data takes the snippet with it.
      </p>

      <Playground
        code={state.snippet.code}
        version={state.snippet.kotlinVersion}
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
