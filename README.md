# kotlin-learn-web

The [roadmap.sh Kotlin roadmap](https://roadmap.sh/kotlin) as a browsable reference site:
every concept grouped under its parent category, with each page written against — and
linked to — the [official Kotlin documentation](https://kotlinlang.org/docs/home.html).

Companion to the `kotlin-learn` Gradle playground next door.

## Running it

```bash
pnpm install
pnpm dev
```

The site runs on <http://localhost:3111>.

## How the content is kept honest

Three rules, each enforced by a script rather than by discipline.

### 1. The category tree comes from the roadmap, not from memory

`data/roadmap.mjs` holds all 153 roadmap nodes (52 topics, 101 subtopics) under 10 parent
sections, reconstructed from [`roadmap.sh/kotlin.json`](https://roadmap.sh/kotlin.json).
roadmap.sh groups nodes three ways and all three were used: graph edges, the drawn section
boxes (*Basic Types*, *Collection-specific Operations*, *More on Classes & Objects*), and
column/row position for the satellites that carry no edge. Every subtopic resolves to a
parent with none left over.

A handful of pages marked `extra: true` are concepts the roadmap implies but does not draw
as nodes, such as *Equality & Hashing* and *Scope Functions*.

`scripts/scaffold.mjs` regenerates the `content/` tree and every `_meta.ts` from that file,
so the sidebar order always matches the roadmap. It never overwrites an existing `.mdx`.

```bash
pnpm content:scaffold          # add any missing pages, rewrite the _meta files
node scripts/scaffold.mjs --list   # report which pages are still placeholders
```

Pages that have not been expanded by hand are not left blank. `pnpm content:fill` replaces
the placeholder marker with the opening prose of the corresponding `kotlinlang.org` page,
quoted from the cached Markdown rather than paraphrased, and adds a callout saying the page
has not been expanded or verified yet. 15 nodes have no cached Markdown and get the links
alone.

### 2. Every documentation link is checked

```bash
pnpm content:verify
```

Fetches all 117 `kotlinlang.org/docs` slugs and 20 `kotlinlang.org/api/core` paths
referenced by the manifest and fails if any does not return HTTP 200.

```bash
pnpm content:fetch
```

Caches the official Markdown sources into `.content-cache/` from
[`JetBrains/kotlin-web-site`](https://github.com/JetBrains/kotlin-web-site/tree/master/docs/topics),
so pages are written against the real text. 17 slugs have no Markdown there — coroutines,
serialization, Dokka and Kotlin Multiplatform live in other repositories — and those pages
link out rather than summarize.

### 3. Expanded pages state what is and is not verified

Pages written out in full carry worked examples with executed output. Pages still showing
the quoted-summary callout have not been expanded, and say so on the page itself — there is
no page that looks finished but is not.

Currently expanded: all of **Collection Operations** (18 pages), plus **Collections**,
**Sequences**, **Ranges** and **Progressions** under Language Basics.

### 4. Every output shown was actually executed

Code samples live as runnable files under `examples/`, one per page. They are compiled and
run, and their real stdout is recorded in `examples/.outputs.json`. Nothing printed on a
page is written by hand.

```bash
pnpm examples:run           # compile, run, record outputs
pnpm examples:run --check   # fail if any recorded output changed
```

Needs `kotlinc` on `PATH`, or `KOTLINC` pointing at the compiler's `bin` directory:

```bash
KOTLINC=/path/to/kotlin-compiler-2.4.21/bin pnpm examples:run
```

Outputs on this site were produced with Kotlin 2.4.21 on JDK 17.

## The playground

Any sample can be made runnable in the page. `<Runnable>` mounts
[`kotlin-playground`](https://github.com/JetBrains/kotlin-playground) (Apache 2.0), the
component JetBrains embeds in `kotlinlang.org`, which posts to `api.kotlinlang.org` — the
public instance of
[`JetBrains/kotlin-compiler-server`](https://github.com/JetBrains/kotlin-compiler-server),
the backend behind play.kotlinlang.org. **No compiler runs on this site or on Vercel.**

```mdx
<Runnable src="collection-operations/grouping.kt" />
```

`src` is a path under `examples/`, read while the page is prerendered. That is the point:
the editor opens the same file `pnpm examples:run` compiled and recorded, so the editor and
the `text` blocks above it are the same code at the same compiler version. Verified on
2026-10-10 by running `examples/collection-operations/grouping.kt` in the browser and
comparing: its output is byte-identical (SHA-256 `4c02394f…`) to the entry recorded by local
`kotlinc` 2.4.21 in `examples/.outputs.json`.

There is also a `code` prop for literal source, used by `/playground` and `/p/[id]`. Prefer
`src`: `code` is a JSX expression, so Kotlin's `${...}` string templates in it would be read
as JavaScript interpolation, and nothing compiles it in CI.

The editor requests **Kotlin 2.4.21** — the version every recorded output was produced with,
and currently `latestStable` per `GET https://api.kotlinlang.org/versions`. If the server
stops offering it the component falls back to whatever is `latestStable`, and a saved
snippet records the version that actually ran, read back from the mounted editor rather than
assumed.

### Saved snippets

| Route | Does |
| :--- | :--- |
| `POST /api/snippets` | Stores `{ code, kotlinVersion, createdAt }`, returns `{ id, url, createdAt }` |
| `GET /api/snippets/[id]` | Reads one back, or 404 |
| `/p/[id]` | Opens a saved snippet in the editor |

Ids are 10 characters of Crockford base32 (50 bits). Snippets are write-once — saving an
edited snippet mints a new id, so a shared link keeps showing what was shared. There is no
listing: a snippet is reachable only by its id.

Storage is [Upstash Redis](https://vercel.com/marketplace/upstash) through the Vercel
Marketplace, which injects the credentials. Note that **Vercel KV no longer exists** — it
was moved to Upstash in December 2024 and `@vercel/kv` is deprecated.

```bash
vercel install upstash
```

`lib/snippets.ts` reads `UPSTASH_REDIS_REST_URL`/`UPSTASH_REDIS_REST_TOKEN`, falling back to
`KV_REST_API_URL`/`KV_REST_API_TOKEN` for a store carried over from the retired KV. It
passes them to the documented `new Redis({ url, token })` constructor rather than
`Redis.fromEnv()`, so neither naming is baked in. `SNIPPET_TTL_DAYS` expires snippets if
set; unset means links never rot.

With no credentials and no `VERCEL` in the environment, saves go to `.snippets/` on disk so
`pnpm dev` works from a clean checkout. Deployed with no store configured, saving returns
503 with a message saying so and running code still works.

Two things this deliberately does not do: there is **no rate limit** on `POST /api/snippets`
beyond a 64 KiB body cap, and nothing deletes a snippet. Both matter if the site gets
traffic.

### Why not run the compiler here

Measured on 2026-10-10, a realistic snippet compiled and ran against `api.kotlinlang.org` in
**1.4–1.8 s** end-to-end (three runs: 1.47 s, 1.80 s, 1.53 s), and 1.25 s from the browser.
Hosting the compiler on Vercel Sandbox would have to beat that while billing Active CPU for
work that is pure CPU — the one workload the pricing model does not discount. Worth
revisiting only to put your own libraries on the classpath, or to stop depending on a public
service.

## Deploying

Zero-config on Vercel: import the repository and accept the detected Next.js settings.
`pnpm build` produces 180 prerendered pages plus three server-rendered routes —
`/api/snippets`, `/api/snippets/[id]` and `/p/[id]`. Adding those changed the output
from a pure static export to static pages plus functions, which Vercel handles with no
configuration change.

## Dependency overrides

`pnpm-workspace.yaml` pins `zod` to 4.1.12. `nextra-theme-docs@4.6.1` destructures
`children` out of its props and then validates the remainder against a schema that still
marks `children` required; zod 4.1.x tolerated that, zod 4.6.x reports
`expected nonoptional, received undefined` and every page 500s. Remove the pin once the
theme fixes `LayoutPropsSchema`.

pnpm does not read the `overrides` field in `package.json` — that one is for npm — so the
pin has to live in `pnpm-workspace.yaml`.

## Layout

```
content/              one .mdx per roadmap node, mirroring the category tree
data/roadmap.mjs      the 153-node manifest: titles, doc slugs, API paths
examples/             runnable Kotlin backing the output blocks
scripts/              scaffold, link verification, docs fetch, example runner
components/           DocSource, Runnable + Playground (the editor), Logo
lib/                  snippet store and the shared Kotlin constants
types/                hand-written declarations for kotlin-playground, which ships none
.content-cache/       fetched official Markdown (gitignored)
```

## Attribution

Content is adapted from the official Kotlin documentation, which is Apache 2.0 licensed.
See [NOTICE.md](./NOTICE.md) and the site's `/attribution` page.
