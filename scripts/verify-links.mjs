// Verifies every `docs` slug and `api` path in data/roadmap.mjs resolves with
// HTTP 200 on kotlinlang.org. Run: pnpm content:verify
import { allNodes, docUrl, apiUrl } from '../data/roadmap.mjs'

const nodes = allNodes()

const docSlugs = new Set()
const apiPaths = new Set()
for (const n of nodes) {
  for (const d of n.docs ?? []) docSlugs.add(d)
  for (const a of n.api ?? []) apiPaths.add(a)
}

const targets = [
  ...[...docSlugs].sort().map((s) => ({ kind: 'doc', key: s, url: docUrl(s) })),
  ...[...apiPaths].sort().map((p) => ({ kind: 'api', key: p, url: apiUrl(p) })),
]

console.log(`nodes: ${nodes.length}`)
console.log(`distinct doc slugs: ${docSlugs.size}`)
console.log(`distinct api paths: ${apiPaths.size}`)
console.log(`checking ${targets.length} URLs...\n`)

const LIMIT = 8
const failures = []
let done = 0

async function check(t) {
  for (let attempt = 0; attempt < 3; attempt++) {
    try {
      const res = await fetch(t.url, { method: 'GET', redirect: 'follow' })
      if (res.ok) return { ...t, status: res.status }
      if (res.status >= 500 && attempt < 2) continue
      return { ...t, status: res.status, failed: true }
    } catch (err) {
      if (attempt === 2) return { ...t, status: String(err.cause?.code ?? err.message), failed: true }
    }
  }
  return { ...t, status: 'unknown', failed: true }
}

const queue = [...targets]
await Promise.all(
  Array.from({ length: LIMIT }, async () => {
    while (queue.length) {
      const t = queue.shift()
      const r = await check(t)
      done++
      if (r.failed) {
        failures.push(r)
        console.log(`  FAIL ${r.status}  ${r.kind}:${r.key}`)
      }
      if (done % 25 === 0) console.log(`  ...${done}/${targets.length}`)
    }
  })
)

console.log(`\nchecked ${done}, failures ${failures.length}`)
if (failures.length) {
  console.log('\nBROKEN:')
  for (const f of failures) console.log(`  ${f.kind}  ${f.key}  -> ${f.status}  ${f.url}`)
  process.exit(1)
}
console.log('all links resolve')
