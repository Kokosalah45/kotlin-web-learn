// Replaces the `{/* placeholder */}` marker in scaffolded pages with a short
// orientation drawn from the official Kotlin docs, so no page in the tree is
// blank while it waits to be expanded.
//
// The summary is the opening prose of the corresponding kotlinlang.org page,
// taken verbatim from the cached Markdown (Apache 2.0, attributed on the page
// and in NOTICE.md) rather than paraphrased from memory. Pages whose docs live
// in other JetBrains repositories have no cached Markdown; those get the links
// and a note instead of an invented summary.
//
//   pnpm content:fill            fill every placeholder
//   pnpm content:fill --dry      report what would be filled
import { readFile, writeFile } from 'node:fs/promises'
import { existsSync } from 'node:fs'
import { allNodes, sections, docUrl } from '../data/roadmap.mjs'

const CACHE = '.content-cache'
const dry = process.argv.includes('--dry')
const MARKER = '{/* placeholder */}'

/** Pull the opening prose out of a cached kotlin-web-site topic file. */
async function leadParagraphs(slug, max = 1) {
  const base = slug.replace(/\/$/, '').split('/').pop()
  const path = `${CACHE}/${base}.md`
  if (!existsSync(path)) return null

  const raw = await readFile(path, 'utf8')
  const paragraphs = []
  let buffer = []

  // Keep only self-contained prose. A paragraph often ends by introducing the
  // code block or list that follows ("... are marked with `data`:"); drop that
  // trailing stem rather than discarding the whole paragraph.
  const trimStem = (input) => {
    // Mask markdown links so the dots inside URLs ("en.wikipedia.org") do not
    // look like sentence boundaries.
    const links = []
    const text = input.replace(/\[[^\]]*\]\([^)]*\)/g, (m) => {
      links.push(m)
      return `\u0000${links.length - 1}\u0000`
    })
    const restore = (s) => s.replace(/\u0000(\d+)\u0000/g, (_m, i) => links[Number(i)])
    const sentences = (text.match(/[^.!?]+[.!?]+(?=\s|$)|[^.!?]+$/g) ?? []).map(restore)
    const kept = []
    for (const s of sentences) {
      const sentence = s.trim()
      if (!/[.!?]$/.test(sentence)) continue
      if (/\b(following|below|as follows|like this)\b/i.test(sentence)) continue
      kept.push(sentence)
    }
    return kept.join(' ')
  }

  const flush = () => {
    const text = trimStem(buffer.join(' ').trim())
    buffer = []
    if (text.length >= 60) paragraphs.push(text)
  }

  let headings = 0
  let inCode = false
  for (const line of raw.split('\n')) {
    const t = line.trim()
    if (t.startsWith('```')) {
      inCode = !inCode
      flush()
      continue
    }
    if (inCode) continue
    // Indented lines are continuations of a list item or nested block, not prose.
    if (/^\s{2,}\S/.test(line)) {
      flush()
      continue
    }
    // Stay in the document's introduction; prose from deeper sections loses its
    // context and reads as a non-sequitur.
    if (t.startsWith('##')) {
      headings++
      if (headings > 1) break
    }
    if (!t) {
      flush()
      if (paragraphs.length >= max) break
      continue
    }
    // Skip front matter, headings, code, directives, images, tables, lists.
    if (
      t.startsWith('[//]:') ||
      t.startsWith('#') ||
      t.startsWith('```') ||
      t.startsWith('>') ||
      t.startsWith('{') ||
      t.startsWith('![') ||
      t.startsWith('<') ||
      t.startsWith('|') ||
      t.startsWith('*') ||
      t.startsWith('-') ||
      /^\d+\./.test(t)
    ) {
      flush()
      if (paragraphs.length >= max) break
      continue
    }
    buffer.push(t)
  }
  flush()

  if (!paragraphs.length) return null

  // Rewrite the docs' own relative links into absolute kotlinlang.org ones and
  // drop their anchors-only references, which mean nothing out of context.
  return paragraphs
    .slice(0, max)
    .map((p) =>
      p
        .replace(/\[([^\]]+)\]\(([a-z0-9-]+)\.md(#[\w-]+)?\)/gi, (_m, text, target) => `[${text}](${docUrl(target)})`)
        .replace(/\[([^\]]+)\]\(#[\w-]+\)/g, '$1')
        .replace(/\{[^}]*\}/g, '')
        .replace(/\s+/g, ' ')
        .trim()
    )
}

const bySlug = new Map()
for (const n of allNodes()) bySlug.set(n.path, n)

// section overview pages
const sectionPages = new Map()
for (const s of sections) sectionPages.set(s.slug, s)

function pagePath(node) {
  const parts = node.path.split('/')
  if (node.kind === 'sub') return `content/${parts.join('/')}.mdx`
  return node.subs?.length ? `content/${node.path}/index.mdx` : `content/${node.path}.mdx`
}

let filled = 0
let noSource = 0
const skipped = []

for (const node of allNodes()) {
  const file = pagePath(node)
  if (!existsSync(file)) continue
  const current = await readFile(file, 'utf8')
  if (!current.includes(MARKER)) continue

  const lead = (await Promise.all((node.docs ?? []).map((d) => leadParagraphs(d)))).find(Boolean)

  let body
  if (lead) {
    body = [
      lead.join('\n\n'),
      '',
      '<Callout type="info">',
      '  Summary quoted from the official documentation linked above, which remains the',
      '  authoritative source. This page has not been expanded with worked examples yet —',
      '  unlike the [Collection Operations](/collection-operations) section, nothing here has',
      '  been verified by running it.',
      '</Callout>',
      '',
      "import { Callout } from 'nextra/components'"
    ].join('\n')
    filled++
  } else {
    body = [
      `Kotlin's documentation for **${node.title}** lives outside the`,
      '[kotlin-web-site](https://github.com/JetBrains/kotlin-web-site) repository, so there is',
      'no summary to quote here. Follow the official sources above.',
      '',
      '<Callout type="info">',
      '  This page has not been expanded yet. The links above are authoritative.',
      '</Callout>',
      '',
      "import { Callout } from 'nextra/components'"
    ].join('\n')
    noSource++
  }

  if (dry) {
    skipped.push(`${file}  ${lead ? 'summary' : 'links-only'}`)
    continue
  }
  await writeFile(file, current.replace(MARKER, body))
}

// Section overview pages: list their topics.
for (const s of sections) {
  const file = `content/${s.slug}/index.mdx`
  if (!existsSync(file)) continue
  const current = await readFile(file, 'utf8')
  if (!current.includes(MARKER)) continue

  const rows = s.topics.map((t) => {
    const href = `/${s.slug}/${t.slug}`
    const subs = (t.subs ?? []).map((x) => x.title).join(', ')
    return `| [${t.title}](${href}) | ${subs || '—'} |`
  })

  const body = [
    `The **${s.title}** part of the roadmap, with every concept it contains.`,
    '',
    '| Topic | Subtopics |',
    '| :--- | :--- |',
    ...rows
  ].join('\n')

  if (dry) {
    skipped.push(`${file}  section-index`)
    continue
  }
  await writeFile(file, current.replace(MARKER, body))
  filled++
}

if (dry) {
  console.log(`would fill ${skipped.length} page(s):`)
  for (const s of skipped) console.log(`  ${s}`)
} else {
  console.log(`filled ${filled} page(s) with an official summary`)
  console.log(`${noSource} page(s) had no cached Markdown and link out instead`)
}
