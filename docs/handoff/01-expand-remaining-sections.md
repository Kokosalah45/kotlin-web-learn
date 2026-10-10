# Handoff: expand Functions, Control Flow, Language Basics

Written 2026-10-10. Start a session in `E:\Projects\kotlin-learn-web` and work from this
brief. Read the repo `README.md` first — it documents the whole architecture.

## What exists

Next.js 16 + Nextra 4 + pnpm docs site, 179 prerendered pages, Vercel-ready. `pnpm dev`
serves it on <http://localhost:3111>, registered in the **workspace** launch.json at
`E:\Projects\.claude\launch.json` as `kotlin-learn-web` — `preview_start` reads that file,
not the project-level one.

- `data/roadmap.mjs` — all 153 roadmap.sh nodes (52 topics, 101 subtopics) under 10
  sections, plus a few `extra: true` pages. Each node carries verified `docs` slugs and
  `api` paths.
- `content/` — one `.mdx` per node, mirroring the tree. The `_meta.ts` files are
  **generated** by `scripts/scaffold.mjs`; edit `data/roadmap.mjs`, never the meta files.
- `.content-cache/` — 100 official Kotlin doc Markdown files (gitignored; `pnpm
  content:fetch` repopulates).
- `examples/` — runnable Kotlin, one file per page, whose real stdout is recorded in
  `examples/.outputs.json`.

## The task

Write these out to the same standard as `content/collection-operations/`. Read two or three
of those pages first — they are the quality bar.

1. **Functions** — standard functions, parameters/return/varargs, local, member,
   tail-recursive, function types, lambdas, higher-order, anonymous, extension functions,
   scope functions
2. **Control Flow** — if, when, for, while, break & continue, label loops,
   throwing/catching exceptions
3. **Language Basics**, excluding collections/sequences/ranges/progressions which are
   already done — introduction, code organization, main function, variables, val vs var,
   data types, type inference, type checks & casts, all seven Basic Types, comments,
   printing data, string templates

Pages still showing a *"Summary quoted from the official documentation … has not been
expanded"* callout are the ones that need writing. Remove that callout when you expand a
page.

## Non-negotiable: verify everything

The user's standing instruction is **"don't assume anything"** — nothing written from
recall. This is enforced mechanically:

- Write each page against the cached Markdown in `.content-cache/<slug>.md`. The page's
  `docs` slugs in `data/roadmap.mjs` say which files.
- **Every output block must be real executed stdout.** Add `examples/<section>/<page>.kt`
  with a `package examples.<sectionnodashes>` declaration and its own `main()`, then run:

```bash
KOTLINC="C:/Users/kerol/AppData/Local/Temp/claude/E--Projects/45d3bfdf-9a42-4bd2-b0e7-383e86ac9101/scratchpad/kotlinc/bin" pnpm examples:run
```

That scratchpad `kotlinc` (2.4.21, checksum-verified) is session-temporary and may be gone.
If so, re-download `kotlin-compiler-2.4.21.zip` from the `JetBrains/kotlin` GitHub release
and verify it against the published `.sha256` before unpacking.

Outputs land in `examples/.outputs.json`; copy them into the MDX verbatim.
`pnpm examples:run --check` fails if any recorded output drifts.

Running the code caught several things that would otherwise have been wrong: `reduce` on an
empty list throws `UnsupportedOperationException` (not `NoSuchElementException`), `list[5]`
throws `ArrayIndexOutOfBoundsException`, and removing the second-to-last element inside a
`for` loop throws **no** exception — it silently skips the tail. Expect similar surprises.

## Page shape

Frontmatter (title, description) → `# Title` → `<DocSource docs={[...]} api={[...]} />` →
prose → `## sections` with Kotlin code blocks each followed by a ```text block of real
output → `## Related` links → `import { Callout } from 'nextra/components'` at the
**bottom** if callouts are used.

`<Callout type="warning">` for genuine gotchas, `type="info"` for clarifications. Lead with
what trips people up — that is what makes these pages worth reading over the official docs.

## Gotchas

- `zod` is pinned to `4.1.12` in `pnpm-workspace.yaml`. Do not unpin —
  `nextra-theme-docs@4.6.1` 500s every page on zod 4.6.x. pnpm ignores `overrides` in
  `package.json`.
- `Cards.Card` is self-closing; passing children breaks the build.
- Kotlin file facade names replace invalid identifier characters with `_`
  (`plus-and-minus.kt` → `Plus_and_minusKt`). The runner already handles this.
- After expanding pages, update the "Currently expanded" lists in `README.md` and
  `content/attribution.mdx`.

## Done when

`pnpm build` passes (179 static pages), `pnpm content:verify` reports 0 broken links,
`pnpm examples:run --check` passes, and no page in those three sections still shows the
not-expanded callout.

Commit with:

```
Co-Authored-By: Claude Opus 5 <noreply@anthropic.com>
```
