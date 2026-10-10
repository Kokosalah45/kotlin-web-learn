import { DOCS, API } from '@/data/roadmap.mjs'

type Props = {
  /** kotlinlang.org/docs slugs this page is based on. */
  docs?: string[]
  /** Paths under kotlinlang.org/api/core/. */
  api?: string[]
  /** A non-kotlinlang.org primary source (Ktor, Quarkus, roadmap.sh, …). */
  external?: string
}

const label = (slug: string) =>
  slug
    .replace(/\/$/, '')
    .split('/')
    .pop()!
    .replace(/-/g, ' ')
    .replace(/^kotlin /, 'kotlin.')

/**
 * Every page states which official pages it was written from. This is the
 * attribution the Apache-2.0 license on the docs asks for, and it is also the
 * fastest way to get from a summary here to the authoritative text.
 */
export function DocSource({ docs = [], api = [], external }: Props) {
  if (!docs.length && !api.length && !external) return null
  return (
    <aside className="nx-doc-source">
      <span className="nx-doc-source__label">Official sources</span>
      <ul>
        {docs.map((slug) => (
          <li key={slug}>
            <a href={`${DOCS}${slug}.html`} target="_blank" rel="noreferrer">
              {label(slug)}
            </a>
          </li>
        ))}
        {api.map((path) => (
          <li key={path}>
            <a href={`${API}${path}`} target="_blank" rel="noreferrer">
              <code>{label(path)}</code> API
            </a>
          </li>
        ))}
        {external && (
          <li>
            <a href={external} target="_blank" rel="noreferrer">
              {new URL(external).hostname.replace(/^www\./, '')}
            </a>
          </li>
        )}
      </ul>
    </aside>
  )
}
