import { readFile } from 'node:fs/promises'
import { join, normalize, sep } from 'node:path'
import { DEFAULT_KOTLIN_VERSION } from '@/lib/kotlin'
import { Playground } from '@/components/playground'

type Props = {
  /**
   * A path under `examples/`, such as `collection-operations/grouping.kt`.
   * Loading the file rather than inlining the code keeps this site's rule that
   * every sample is a real, compiled, output-recorded file — the editor opens
   * the same text that `pnpm examples:run` executed.
   */
  src?: string
  /** Literal code, for a scratch editor or a reopened saved snippet. */
  code?: string
  /** Defaults to the compiler the recorded outputs were produced with. */
  version?: string
  saveable?: boolean
  label?: string
}

const EXAMPLES_DIR = 'examples'

/**
 * `src` comes from MDX authored in this repository, not from a request, but the
 * path is still confined to `examples/` so a stray `../` in a page cannot read
 * the rest of the checkout at build time.
 */
async function readExample(src: string): Promise<string> {
  const root = join(process.cwd(), EXAMPLES_DIR)
  const resolved = normalize(join(root, src))
  if (!resolved.startsWith(root + sep)) {
    throw new Error(`<Runnable src> must stay inside ${EXAMPLES_DIR}/: got "${src}"`)
  }
  try {
    return await readFile(resolved, 'utf8')
  } catch {
    throw new Error(
      `<Runnable src="${src}"> does not exist. Expected ${EXAMPLES_DIR}/${src} ` +
        `relative to the repository root.`
    )
  }
}

/**
 * Makes a Kotlin sample runnable in the page.
 *
 * A server component, so the file read happens while the page is prerendered
 * and `examples/` never has to be part of the deployed bundle. The editor
 * itself is the client component it renders.
 */
export async function Runnable({
  src,
  code,
  version = DEFAULT_KOTLIN_VERSION,
  saveable = true,
  label
}: Props) {
  if (!src && code === undefined) {
    throw new Error('<Runnable> needs either a `src` or a `code` prop.')
  }

  const source = src ? await readExample(src) : code!

  return (
    <Playground
      code={source.replace(/\s+$/, '')}
      version={version}
      saveable={saveable}
      label={label ?? (src ? `examples/${src}` : undefined)}
    />
  )
}
