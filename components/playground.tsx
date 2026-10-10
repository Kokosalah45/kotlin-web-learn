'use client'

import { useCallback, useEffect, useRef, useState } from 'react'
import type { ExecutableCode, PlaygroundFragment } from 'kotlin-playground'
import { DEFAULT_KOTLIN_VERSION, MAX_CODE_BYTES } from '@/lib/kotlin'

type Props = {
  /** Initial editor contents. */
  code: string
  /** Compiler to request. Must be one of the versions `/versions` lists. */
  version?: string
  /** Hide the save button where a share link makes no sense. */
  saveable?: boolean
  /** Shown above the editor. */
  label?: string
}

type Share = { id: string; url: string }

/**
 * CodeMirror themes bundled with kotlin-playground. `getTheme()` warns about
 * any other value because it would need stylesheets the bundle does not ship.
 */
const THEME_DARK = 'darcula'
const THEME_LIGHT = 'idea'

const isDarkDocument = () => document.documentElement.classList.contains('dark')

/**
 * A runnable Kotlin editor.
 *
 * `kotlin-playground` is a DOM library, not a React one: it hides the node you
 * hand it, inserts a sibling next to it and then owns that subtree. So React
 * renders an empty host element and never any children — the target node is
 * built imperatively inside the effect. Letting React render the target as JSX
 * would mean two writers for one subtree.
 *
 * The module cannot be imported on the server at all: `src/config.js` calls
 * `getCurrentScript()` — `document.getElementsByTagName('script')` — while it
 * is being evaluated, and the UMD wrapper references `self`. Hence the dynamic
 * import inside the effect.
 */
export function Playground({
  code,
  version = DEFAULT_KOTLIN_VERSION,
  saveable = true,
  label
}: Props) {
  const hostRef = useRef<HTMLDivElement>(null)
  const fragmentRef = useRef<PlaygroundFragment | null>(null)

  /**
   * The contents to mount with. Re-read from the editor before a theme change
   * forces a remount, so switching light/dark does not discard the user's edits.
   */
  const [source, setSource] = useState(code)
  const [theme, setTheme] = useState<string | null>(null)
  const [status, setStatus] = useState<'loading' | 'ready' | 'failed'>('loading')
  const [share, setShare] = useState<Share | null>(null)
  const [saving, setSaving] = useState(false)
  const [saveError, setSaveError] = useState<string | null>(null)
  const [copied, setCopied] = useState(false)

  // Resolved in an effect, not during render: the server has no `document`.
  useEffect(() => {
    const sync = () => setTheme(isDarkDocument() ? THEME_DARK : THEME_LIGHT)
    sync()
    const observer = new MutationObserver(sync)
    observer.observe(document.documentElement, { attributes: true, attributeFilter: ['class'] })
    return () => observer.disconnect()
  }, [])

  // Carry the current editor contents across the remount a theme change needs.
  useEffect(() => {
    const current = fragmentRef.current?.getCode()
    if (current !== undefined) setSource(current)
  }, [theme])

  useEffect(() => {
    const host = hostRef.current
    if (!host || theme === null) return

    // StrictMode runs effects twice and `create()` is async, so an instance can
    // arrive after this effect has already been torn down.
    let cancelled = false
    let mounted: ExecutableCode[] = []

    const target = document.createElement('pre')
    target.textContent = source
    // Most per-editor settings are attributes on the target node rather than
    // options to create(); the names are the ATTRIBUTES map in
    // src/executable-code/index.js. Set here rather than as JSX props so React
    // never has an opinion about non-standard attributes like `theme`.
    const attributes: Record<string, string> = {
      theme,
      lines: 'true',
      indent: '4',
      'match-brackets': 'true',
      'auto-indent': 'true',
      'data-autocomplete': 'true',
      'data-target-platform': 'java',
      'data-crosslink': 'enabled'
    }
    for (const [name, value] of Object.entries(attributes)) target.setAttribute(name, value)
    host.appendChild(target)

    setStatus('loading')

    import('kotlin-playground')
      .then(({ default: createPlayground }) =>
        createPlayground(target, {
          version,
          getInstance: (fragment) => {
            fragmentRef.current = fragment
          },
          // Debounced 500ms upstream. Used only to retire a share link that no
          // longer matches what is on screen; the code itself is read with
          // getCode() at save time, so the debounce cannot truncate a save.
          onChange: () => {
            setShare(null)
            setSaveError(null)
          }
        })
      )
      .then((instances) => {
        if (cancelled) {
          instances.forEach((instance) => instance.destroy())
          return
        }
        mounted = instances
        setStatus(instances.length > 0 ? 'ready' : 'failed')
      })
      .catch((error: unknown) => {
        console.error('kotlin-playground failed to mount', error)
        if (!cancelled) setStatus('failed')
      })

    return () => {
      cancelled = true
      mounted.forEach((instance) => instance.destroy())
      fragmentRef.current = null
      // destroy() unmounts the editor it inserted; clearing the host also drops
      // the target node this effect created.
      host.replaceChildren()
    }
  }, [source, theme, version])

  const save = useCallback(async () => {
    const current = fragmentRef.current?.getCode() ?? source
    if (current.trim() === '') {
      setSaveError('Nothing to save.')
      return
    }
    if (new TextEncoder().encode(current).length > MAX_CODE_BYTES) {
      setSaveError('Snippets are limited to ' + MAX_CODE_BYTES / 1024 + ' KiB.')
      return
    }

    setSaving(true)
    setSaveError(null)
    try {
      const response = await fetch('/api/snippets', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          code: current,
          // The resolved compiler, which differs from `version` if the server
          // no longer offers it and fell back to `latestStable`.
          kotlinVersion: fragmentRef.current?.state?.compilerVersion ?? version
        })
      })
      const body = (await response.json()) as { id?: string; url?: string; error?: string }
      if (!response.ok || !body.id || !body.url) {
        setSaveError(body.error ?? 'Save failed (' + response.status + ').')
        return
      }
      setShare({ id: body.id, url: body.url })
      setCopied(false)
    } catch {
      setSaveError('Save failed — the network request did not complete.')
    } finally {
      setSaving(false)
    }
  }, [source, version])

  const shareUrl = share ? new URL(share.url, window.location.origin).toString() : null

  const copy = useCallback(async () => {
    if (!shareUrl) return
    try {
      await navigator.clipboard.writeText(shareUrl)
      setCopied(true)
    } catch {
      setCopied(false)
    }
  }, [shareUrl])

  return (
    <div className="kl-playground">
      <div className="kl-playground__bar">
        <span className="kl-playground__label">
          {label ?? 'Runnable'}
          <span className="kl-playground__version">Kotlin {version}</span>
        </span>
        {saveable && (
          <button
            type="button"
            className="kl-playground__save"
            onClick={save}
            disabled={saving || status !== 'ready'}
          >
            {saving ? 'Saving…' : 'Save & share'}
          </button>
        )}
      </div>

      <div ref={hostRef} className="kl-playground__host" />

      {status === 'loading' && (
        <p className="kl-playground__note" role="status">
          Loading the editor…
        </p>
      )}
      {status === 'failed' && (
        <p className="kl-playground__note kl-playground__note--error" role="status">
          The editor could not start. It needs <code>api.kotlinlang.org</code>, which answers the
          version list and compiles the code.
        </p>
      )}

      {shareUrl && share && (
        <p className="kl-playground__note" role="status">
          Saved. <a href={share.url}>{shareUrl}</a>{' '}
          <button type="button" className="kl-playground__copy" onClick={copy}>
            {copied ? 'Copied' : 'Copy'}
          </button>
        </p>
      )}
      {saveError && (
        <p className="kl-playground__note kl-playground__note--error" role="status">
          {saveError}
        </p>
      )}
    </div>
  )
}
