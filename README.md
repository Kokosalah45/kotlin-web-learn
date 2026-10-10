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

## Deploying

Zero-config on Vercel: import the repository and accept the detected Next.js settings.
`pnpm build` produces a fully static export (179 prerendered pages).

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
components/           DocSource (the "Official sources" block), Logo
.content-cache/       fetched official Markdown (gitignored)
```

## Attribution

Content is adapted from the official Kotlin documentation, which is Apache 2.0 licensed.
See [NOTICE.md](./NOTICE.md) and the site's `/attribution` page.
