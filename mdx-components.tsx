import type { MDXComponents } from 'mdx/types'
import { useMDXComponents as getDocsMDXComponents } from 'nextra-theme-docs'
import { DocSource } from '@/components/doc-source'

const docsComponents = getDocsMDXComponents()

export function useMDXComponents(components?: MDXComponents) {
  return {
    ...docsComponents,
    DocSource,
    ...components
  }
}
