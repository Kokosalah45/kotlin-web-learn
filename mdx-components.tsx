import type { MDXComponents } from 'mdx/types'
import { useMDXComponents as getDocsMDXComponents } from 'nextra-theme-docs'
import { DocSource } from '@/components/doc-source'
import { Runnable } from '@/components/runnable'
import { SavedSnippets } from '@/components/saved-snippets'

const docsComponents = getDocsMDXComponents()

export function useMDXComponents(components?: MDXComponents) {
  return {
    ...docsComponents,
    DocSource,
    Runnable,
    SavedSnippets,
    ...components
  }
}
