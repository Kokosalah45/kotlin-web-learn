import { KOTLIN_COMPILER_SERVER } from '@/lib/kotlin'

/**
 * Signatures for the editor's hover, out of the compiler server's completion
 * endpoint.
 *
 * There is no hover or documentation endpoint to ask. `kotlin-playground`
 * talks to four routes — `compiler/run`, `compiler/test`, `compiler/translate`
 * and `compiler/highlight` — plus `compiler/complete`, and only the last one
 * knows anything about a name. Real hover types would need a language server
 * for Kotlin running somewhere; this site has no server of its own at all.
 *
 * Completion is enough to answer "what is this?", because its items carry the
 * signature the dropdown shows:
 *
 *     POST /api/2.4.21/compiler/complete?line=2&ch=13
 *     { "args": "", "confType": "java",
 *       "files": [{ "name": "File.kt", "text": "…s.replace…", "publicId": "" }] }
 *
 *     [{ "text": "replace(",
 *        "displayText": "replace(oldValue: String, newValue: String, ignoreCase: Boolean = ...)",
 *        "tail": "String", "icon": "method" }, …]
 *
 * A function that takes nothing comes back as `{"text": "uppercase()"}`, and
 * one behind an import as `{"text": "kotlin.text.uppercase()", "import": …}`.
 *
 * — verified against api.kotlinlang.org on 2026-10-24, which also answered
 * `{"text":"greeting","displayText":"greeting","tail":"String","icon":"property"}`
 * for a local `val greeting = "hi"`. The request shape is the one
 * `kotlin-playground` builds in `WebDemoApi.getAutoCompletion`, so the hover
 * asks the same server the editor already compiles against, the same way.
 *
 * What this is not: resolution. Completion answers "what could be written
 * here", so every overload comes back rather than the one a call resolves to,
 * nothing carries KDoc prose, and a name that is not completable where it sits
 * — inside a string, a comment, a declaration being introduced — has no
 * answer. It also costs a network round trip: three calls measured 1.1s, 1.1s
 * and 1.4s, so the hover is deliberately delayed and cached rather than
 * pretending to be an IDE.
 */

/** One completion item, as the server sends it. */
export type SignatureItem = {
  /** What would be inserted: `replace(`, or `kotlin.collections.joinTo(`. */
  text: string
  /** The name with its parameter list, which is the signature to show. */
  displayText: string
  /** A return type (`String`), or a package in parentheses for a class. */
  tail?: string
  /** `method`, `class`, `property`, `package`… */
  icon?: string
  /** Present when the symbol would need an import first. */
  import?: string
}

const FILE_NAME = 'File.kt'

/** Matches the editor's `data-target-platform="java"`. */
const CONF_TYPE = 'java'

/**
 * The bare name a completion item would insert.
 *
 * `text` carries the call syntax and sometimes the package: `uppercase()` for
 * a function that takes nothing, `uppercase(` for one that takes something,
 * and `kotlin.text.uppercase()` for the same name behind an import. All three
 * have to reduce to `uppercase` or the name under the pointer will not match
 * its own overloads — and dropping the empty-parens form drops the overload a
 * reader is most likely to be hovering.
 */
function itemName(item: SignatureItem): string {
  const inserted = item.text.replace(/\(\)?$/, '')
  return inserted.slice(inserted.lastIndexOf('.') + 1)
}

/**
 * The signatures for `word`, asked at the position just after it.
 *
 * Items for other names come back too — the endpoint answers with everything
 * completable at that position — so they are filtered down to exact matches on
 * the name under the pointer, with the ones that are already in scope before
 * the ones that would need an import.
 */
export async function lookupSignatures({
  code,
  line,
  ch,
  word,
  version,
  signal
}: {
  code: string
  line: number
  ch: number
  word: string
  version: string
  signal?: AbortSignal
}): Promise<SignatureItem[]> {
  const url =
    `${KOTLIN_COMPILER_SERVER}/api/${version}/compiler/complete` +
    `?line=${line}&ch=${ch}`

  const response = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json; charset=utf-8' },
    body: JSON.stringify({
      args: '',
      confType: CONF_TYPE,
      files: [{ name: FILE_NAME, text: code, publicId: '' }]
    }),
    signal
  })
  if (!response.ok) throw new Error(`complete answered ${response.status}`)

  const items = (await response.json()) as SignatureItem[]
  if (!Array.isArray(items)) return []

  return items
    .filter((item) => itemName(item) === word)
    .sort((a, b) => Number(Boolean(a.import)) - Number(Boolean(b.import)))
}

/**
 * One line of hover text. `tail` is a return type for a method or property and
 * a parenthesised package for a class, so it is joined on differently.
 */
export function formatSignature(item: SignatureItem): string {
  // Items that name their package come with two spaces before it.
  const signature = item.displayText.replace(/\s{2,}/g, ' ')
  const tail = item.tail?.trim() ?? ''
  if (tail === '') return signature
  if (tail.startsWith('(')) return `${signature} ${tail}`
  return `${signature}: ${tail}`
}

/** The subset of CodeMirror 5 the hover needs. */
export type HoverEditor = {
  coordsChar(coords: { left: number; top: number }, mode?: string): { line: number; ch: number }
  getTokenAt(pos: { line: number; ch: number }): {
    string: string
    start: number
    end: number
    type: string | null
  }
  getValue(): string
  on(event: string, handler: () => void): void
  off(event: string, handler: () => void): void
}

/**
 * The editor `kotlin-playground` mounted, read off the DOM.
 *
 * CodeMirror 5 hangs the instance on its own wrapper element, and the library
 * hands out its fragment rather than the editor, so this is the way to it.
 * Checked at runtime against the editor on /playground: CodeMirror 5.65.18,
 * with `coordsChar`, `getTokenAt`, `getValue` and `on` all present.
 */
export function editorFromHost(host: HTMLElement): HoverEditor | null {
  const wrapper = host.querySelector('.CodeMirror')
  if (!wrapper) return null
  const instance = (wrapper as HTMLElement & { CodeMirror?: HoverEditor }).CodeMirror
  return instance ?? null
}

/** Identifiers only: no keywords, numbers, strings or comment text. */
const IDENTIFIER = /^[A-Za-z_][A-Za-z0-9_]*$/
const SKIPPED_TOKEN_TYPES = new Set(['keyword', 'number', 'string', 'comment', 'string-2'])

export function isLookupToken(token: { string: string; type: string | null }): boolean {
  if (token.type !== null && SKIPPED_TOKEN_TYPES.has(token.type)) return false
  return IDENTIFIER.test(token.string)
}
