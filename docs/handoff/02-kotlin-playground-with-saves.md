# Handoff: Kotlin playground endpoint with saved snippets

Written 2026-10-10. Start a session in `E:\Projects\kotlin-learn-web` and work from this
brief. Read the repo `README.md` first.

Goal: make code on the site runnable, and let snippets be saved and shared by link.

## Research already done — do not re-derive

All verified against current official docs on 2026-10-10. Re-check only if something fails.

### Vercel can host this

| Path | How it runs | Notes |
| :--- | :--- | :--- |
| **A. Vercel Sandbox + custom image** | Firecracker microVM per run, booted from your own OCI image in Vercel Container Registry | Vercel's docs name this use case: *"Build interactive tools: Create code playgrounds"* |
| **B. Container image as a Vercel Function** | `Dockerfile.vercel` at repo root, serves HTTP on port 80 (override with `PORT`) | Autoscales on Fluid compute; scales to zero after 5 min idle in production, 30s in preview; `SIGTERM` with 30s grace |
| **C. Embed JetBrains' hosted playground** | `kotlin-playground` npm component posts to JetBrains' compile server | Zero backend. `data-server` repoints it at your own instance later |

Path A commands:

```bash
vercel vcr build docker . kotlin-playground:latest --push
```

```ts
import { Sandbox } from '@vercel/sandbox'
const sandbox = await Sandbox.create({ image: 'kotlin-playground:latest' })
```

### Kotlin backend

[`JetBrains/kotlin-compiler-server`](https://github.com/JetBrains/kotlin-compiler-server)
(Apache 2.0, actively maintained) is the real backend behind play.kotlinlang.org. Ships
`docker-image-build.sh` and an `executor.policy` Java security policy for sandboxing
executed code. That is the playground Docker image.

[`JetBrains/kotlin-playground`](https://github.com/JetBrains/kotlin-playground) (Apache
2.0) is the embeddable front end.

### Vercel Sandbox limits

| | Hobby | Pro |
| :--- | :--- | :--- |
| Active CPU | 5 hours/month included | $0.128/hour |
| Provisioned memory | 420 GB-hours/month | $0.0212/GB-hour |
| Sandbox creations | 5,000/month | $0.60/1M |
| Concurrent sandboxes | 10 | 10,000 |
| Max session duration | 45 minutes | 24 hours |
| Max vCPU / memory | 4 / 8 GB | 8 / 16 GB |
| Disk | 64 GB | 64 GB |
| Snapshot storage | 15 GB lifetime | $0.08/GB-month |

### Storage

**Vercel KV no longer exists.** Existing stores were moved to Upstash Redis in December
2024; `@vercel/kv` on npm is deprecated and `/docs/storage/vercel-kv` redirects to
`/docs/redis`. Use the Marketplace — credentials are injected as env vars automatically:

```bash
vercel install upstash   # Redis, via @upstash/redis
vercel install neon      # Postgres
```

## Build this

Start with **path C plus your own saves** — the smallest shippable thing, no compile
infrastructure.

1. Embed `kotlin-playground` as a Nextra MDX component (e.g. `<Runnable>`) so any code
   block on a concept page can be made runnable. The site already has
   `components/doc-source.tsx` and registers components in `mdx-components.tsx`.
2. `POST /api/snippets` — store `{ code, kotlinVersion, createdAt }`, return a short id.
   `GET /api/snippets/[id]` — read it back. Add a `/p/[id]` page that opens a saved
   snippet in the playground.
3. Choose the store based on whether a "my snippets" listing is wanted. Redis is a perfect
   fit for get-by-id but has no queries — listing or ordering means hand-maintained sorted
   sets. Postgres gives `ORDER BY created_at` for free. **Ask the user which they want**
   if it is not obvious from how they describe the feature.

Only move to path A if the user wants their own compiler — their own libraries on the
classpath, or no dependency on JetBrains' public server.

## Measure before committing to path A

Do not size this from guesses. Compiling Kotlin is pure CPU, and Sandbox bills Active CPU,
so it is exactly the workload the pricing model does not discount.

The only measurement taken so far: kotlinc 2.4.21 compiled 20 small example files in 8.3s
on this machine — a batch, on local hardware. Time a single realistic snippet end to end
before deciding, and report the number.

If you do build path A, the key cost trick is **not** booting a JVM per submission. Use
Sandbox persistent sandboxes or snapshots to keep one warm compiler server and reuse it.

## Specific gotchas

- Vercel Sandbox **does not run Docker `ENTRYPOINT` or `CMD`** for custom images. Start the
  server yourself with `sandbox.runCommand()` after create.
- For untrusted code set `networkPolicy: 'deny-all'`, a short `timeout`, and
  `persistent: false`. Sandboxes are **persistent by default** now.
- Never use `vm2`, `eval` or `child_process` to sandbox user code.
- `zod` is pinned to `4.1.12` in `pnpm-workspace.yaml` — do not unpin,
  `nextra-theme-docs@4.6.1` 500s every page on 4.6.x.
- The site is currently fully static (179 prerendered pages). Adding API routes makes it
  static pages plus functions, which is fine and needs no hosting change.

## Constraints

The user's standing instruction is **"don't assume anything"** — verify library behaviour
against official docs rather than writing from recall, and quote doc wording. The site's
content standard is that every shown output was actually executed; hold the playground code
to the same bar.

`pnpm build` must still pass and `pnpm content:verify` must still report 0 broken links.

Commit with:

```
Co-Authored-By: Claude Opus 5 <noreply@anthropic.com>
```

## Sources

- <https://vercel.com/docs/sandbox>
- <https://vercel.com/docs/sandbox/pricing>
- <https://vercel.com/docs/sandbox/concepts/images>
- <https://vercel.com/kb/guide/docker>
- <https://vercel.com/docs/redis>
- <https://github.com/JetBrains/kotlin-compiler-server>
- <https://github.com/JetBrains/kotlin-playground>
