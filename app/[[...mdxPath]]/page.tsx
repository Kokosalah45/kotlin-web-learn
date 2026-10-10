import { notFound } from 'next/navigation'
import { generateStaticParamsFor, importPage } from 'nextra/pages'
import { useMDXComponents as getMDXComponents } from '@/mdx-components'

export const generateStaticParams = generateStaticParamsFor('mdxPath')

type PageProps = {
  params: Promise<{ mdxPath?: string[] }>
}

const toKey = (mdxPath: string | string[] = []) => [mdxPath].flat().join('/')

async function loadKnownPaths() {
  const entries = await generateStaticParams()
  return new Set(entries.map((entry) => toKey(entry.mdxPath)))
}

let productionPaths: Promise<Set<string>> | undefined

/**
 * URLs that are not pages (`/robots.txt`, the `/.well-known/…` probe Chrome
 * DevTools sends, crawlers) make `importPage` log a stack trace before it
 * 404s. Checking the known paths first turns them into a plain 404. The list is
 * cached in production only, so new files in `content/` still show up during
 * development without a restart.
 */
async function assertPageExists(mdxPath?: string[]) {
  const paths =
    process.env.NODE_ENV === 'production' ? (productionPaths ??= loadKnownPaths()) : loadKnownPaths()
  if (!(await paths).has(toKey(mdxPath))) notFound()
}

export async function generateMetadata(props: PageProps) {
  const params = await props.params
  await assertPageExists(params.mdxPath)
  const { metadata } = await importPage(params.mdxPath)
  return metadata
}

const Wrapper = getMDXComponents().wrapper

export default async function Page(props: PageProps) {
  const params = await props.params
  await assertPageExists(params.mdxPath)
  const { default: MDXContent, toc, metadata, sourceCode } = await importPage(params.mdxPath)
  return (
    <Wrapper toc={toc} metadata={metadata} sourceCode={sourceCode}>
      <MDXContent {...props} params={params} />
    </Wrapper>
  )
}
