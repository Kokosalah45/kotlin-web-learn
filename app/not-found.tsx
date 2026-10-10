import Link from 'next/link'

export default function NotFound() {
  return (
    <div style={{ padding: '6rem 1.5rem', textAlign: 'center' }}>
      <h1 style={{ fontSize: '1.5rem', fontWeight: 600 }}>Page not found</h1>
      <p style={{ marginTop: '0.75rem', opacity: 0.7 }}>
        That concept is not in the roadmap tree.
      </p>
      <p style={{ marginTop: '1.5rem' }}>
        <Link href="/">Back to the roadmap</Link>
      </p>
    </div>
  )
}
