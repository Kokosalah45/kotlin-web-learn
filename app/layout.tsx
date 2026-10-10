import type { Metadata } from 'next'
import Link from 'next/link'
import type { ReactNode } from 'react'
import { Footer, Layout, Navbar } from 'nextra-theme-docs'
import { Head } from 'nextra/components'
import { getPageMap } from 'nextra/page-map'
import 'nextra-theme-docs/style.css'
import './globals.css'
import { Logo } from '@/components/logo'

export const metadata: Metadata = {
  title: {
    default: 'Kotlin Learn',
    template: '%s – Kotlin Learn'
  },
  description:
    'The roadmap.sh Kotlin roadmap as a browsable reference: every concept grouped under its parent category, grounded in the official Kotlin documentation.',
  applicationName: 'Kotlin Learn'
}

const navbar = (
  <Navbar logo={<Logo />} projectLink="https://github.com/JetBrains/kotlin">
    <a
      href="https://kotlinlang.org/docs/home.html"
      target="_blank"
      rel="noreferrer"
      className="x:text-sm x:whitespace-nowrap x:hover:underline"
    >
      Official docs ↗
    </a>
  </Navbar>
)

const footer = (
  <Footer>
    <span>
      Content adapted from the{' '}
      <a href="https://kotlinlang.org/docs/home.html" target="_blank" rel="noreferrer">
        official Kotlin documentation
      </a>{' '}
      (Apache&nbsp;2.0) — see <Link href="/attribution">attribution</Link>. Structure from{' '}
      <a href="https://roadmap.sh/kotlin" target="_blank" rel="noreferrer">
        roadmap.sh/kotlin
      </a>
      . Kotlin is a trademark of the Kotlin Foundation.
    </span>
  </Footer>
)

export default async function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="en" dir="ltr" suppressHydrationWarning>
      <Head color={{ hue: 270, saturation: 70 }} />
      <body>
        <Layout
          navbar={navbar}
          footer={footer}
          pageMap={await getPageMap()}
          docsRepositoryBase="https://github.com/JetBrains/kotlin-web-site/tree/master/docs/topics"
          editLink={null}
          feedback={{ content: null }}
          sidebar={{ defaultMenuCollapseLevel: 1, autoCollapse: true }}
          toc={{ backToTop: 'Back to top' }}
        >
          {children}
        </Layout>
      </body>
    </html>
  )
}
