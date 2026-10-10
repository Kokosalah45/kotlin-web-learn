// Fetches the official Kotlin docs Markdown sources that back each page in
// data/roadmap.mjs into .content-cache/, so page content can be grounded in (and
// checked against) the real docs rather than written from memory.
//
// Source of truth: https://github.com/JetBrains/kotlin-web-site/tree/master/docs/topics
// Docs hosted in other JetBrains repos (coroutines, serialization, Dokka, KMP,
// Lincheck) have no Markdown here; those are reported as `no-md` and the page
// links out to kotlinlang.org instead.
//
// Run: pnpm content:fetch
import { mkdir, writeFile, readFile } from 'node:fs/promises'
import { existsSync } from 'node:fs'
import { allNodes } from '../data/roadmap.mjs'

const CACHE = '.content-cache'
const TREE = 'https://api.github.com/repos/JetBrains/kotlin-web-site/git/trees/master?recursive=1'
const RAW = 'https://raw.githubusercontent.com/JetBrains/kotlin-web-site/master/'

await mkdir(CACHE, { recursive: true })

// 1. Build live-slug -> repo-path map from the repo tree.
const mapPath = `${CACHE}/_slug2md.json`
let slug2md
if (existsSync(mapPath)) {
  slug2md = JSON.parse(await readFile(mapPath, 'utf8'))
  console.log(`slug map: ${Object.keys(slug2md).length} (cached)`)
} else {
  const res = await fetch(TREE)
  if (!res.ok) throw new Error(`tree fetch failed: ${res.status}`)
  const tree = await res.json()
  const topics = tree.tree.filter((x) => x.path.startsWith('docs/topics/') && x.path.endsWith('.md'))
  const byBase = {}
  for (const t of topics) {
    const base = t.path.replace('docs/topics/', '').replace(/\.md$/, '').split('/').pop()
    byBase[base] ??= t.path
  }
  slug2md = byBase
  await writeFile(mapPath, JSON.stringify(slug2md, null, 1))
  console.log(`slug map: ${Object.keys(slug2md).length} topics (fetched)`)
}

// 2. Collect every slug referenced by the manifest.
const slugs = new Set()
for (const n of allNodes()) for (const d of n.docs ?? []) slugs.add(d)

const wanted = [...slugs].sort()
console.log(`manifest references ${wanted.length} distinct doc slugs\n`)

const noMd = []
let fetched = 0
let cached = 0
const failed = []

for (const slug of wanted) {
  const base = slug.replace(/\/$/, '').split('/').pop()
  const repoPath = slug2md[base]
  if (!repoPath) {
    noMd.push(slug)
    continue
  }
  const out = `${CACHE}/${base}.md`
  if (existsSync(out)) {
    cached++
    continue
  }
  const res = await fetch(RAW + repoPath)
  if (!res.ok) {
    failed.push(`${slug} (${res.status})`)
    continue
  }
  await writeFile(out, await res.text())
  fetched++
}

console.log(`fetched ${fetched}, already cached ${cached}`)
if (noMd.length) {
  console.log(`\nno Markdown in kotlin-web-site (${noMd.length}) - link out instead:`)
  console.log(`  ${noMd.join(', ')}`)
}
if (failed.length) {
  console.log(`\nfailed (${failed.length}): ${failed.join(', ')}`)
  process.exit(1)
}
