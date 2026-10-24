'use client'

import { useCallback, useEffect, useRef, useState } from 'react'
import type { ExecutableCode, PlaygroundFragment } from 'kotlin-playground'
import { DEFAULT_KOTLIN_VERSION, MAX_CODE_BYTES } from '@/lib/kotlin'
import { putLocalSnippet } from '@/lib/local-snippets'
import { newId } from '@/lib/snippet-ids'
import {
  editorFromHost,
  formatSignature,
  isLookupToken,
  lookupSignatures,
  type SignatureItem
} from '@/lib/signature-lookup'

type Props = {
  /** Initial editor contents. */
  code: string
  /** Compiler to request. Must be one of the versions `/versions` lists. */
  version?: string
  /** Hide the save button where saving makes no sense. */
  saveable?: boolean
  /** Shown above the editor. */
  label?: string
  /**
   * `inline` sizes the editor to its contents, which is what a short excerpt on
   * a concept page wants. `tall` gives it a large fixed working area instead,
   * for the places where the editor *is* the page: /playground, a saved
   * snippet, and the dock.
   */
  size?: 'inline' | 'tall'
  /**
   * Called with the editor's contents as they change, for a caller that has to
   * keep them somewhere — the dock, which writes them to `sessionStorage`.
   * Inherits kotlin-playground's 500ms debounce, so it is a few keystrokes
   * behind the screen.
   */
  onCodeChange?: (code: string) => void
  /**
   * Called after a save lands, so a caller showing the list of saved snippets
   * — the drawer — can pick the new one up.
   */
  onSaved?: (id: string) => void
}

/**
 * CodeMirror themes bundled with kotlin-playground. `getTheme()` warns about
 * any other value because it would need stylesheets the bundle does not ship.
 */
const THEME_DARK = 'darcula'
const THEME_LIGHT = 'idea'

/**
 * How long the pointer has to rest on a name before its signature is looked
 * up. A lookup is a round trip to api.kotlinlang.org — measured at 1.1–1.4s —
 * so this is about not starting one for every name the pointer crosses.
 */
const HOVER_DELAY = 350

/** Enough to see that a name is overloaded without burying the page in it. */
const MAX_SIGNATURES = 5

/** Matches `max-width` on `.kl-signature`, and the room it needs below. */
const TOOLTIP_WIDTH = 360
const TOOLTIP_CLEARANCE = 200

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
  label,
  size = 'inline',
  onCodeChange,
  onSaved
}: Props) {
  const hostRef = useRef<HTMLDivElement>(null)
  const fragmentRef = useRef<PlaygroundFragment | null>(null)

  /**
   * Held in a ref so the mounting effect below does not have to list the
   * callback as a dependency: a caller that passes an inline arrow function
   * would otherwise tear the editor down and rebuild it on every render.
   */
  const onCodeChangeRef = useRef(onCodeChange)
  const onSavedRef = useRef(onSaved)
  useEffect(() => {
    onCodeChangeRef.current = onCodeChange
    onSavedRef.current = onSaved
  })

  /**
   * The contents to mount with. Re-read from the editor before a theme change
   * forces a remount, so switching light/dark does not discard the user's edits.
   */
  const [source, setSource] = useState(code)
  const [theme, setTheme] = useState<string | null>(null)
  const [status, setStatus] = useState<'loading' | 'ready' | 'failed'>('loading')
  const [saving, setSaving] = useState(false)
  const [saved, setSaved] = useState(false)
  const [saveError, setSaveError] = useState<string | null>(null)

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
          // Debounced 500ms upstream. Used to retire a share link that no
          // longer matches what is on screen, and to report the contents to a
          // caller that keeps them. The code saved to a share link is read with
          // getCode() at save time, so the debounce cannot truncate a save.
          onChange: (current) => {
            // What is on screen is no longer what was saved, so retire the
            // confirmation rather than leave it standing over newer text.
            setSaved(false)
            setSaveError(null)
            onCodeChangeRef.current?.(current)
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

  /**
   * What the pointer is resting on: where to draw the tooltip, the name being
   * asked about, and the answer once it arrives. `items` stays null while the
   * request is in flight, which is what the tooltip shows as "looking up".
   */
  const [hover, setHover] = useState<{
    x: number
    y: number
    /** True when the tooltip has to sit above the pointer to stay on screen. */
    flip: boolean
    word: string
    items: SignatureItem[] | null
  } | null>(null)

  /**
   * Signatures on hover, which neither CodeMirror nor kotlin-playground does.
   * See `lib/signature-lookup.ts` for where the answers come from and what
   * they are worth. Everything here is about not asking too often: a rest of
   * HOVER_DELAY before the first request, one request at a time, a cache per
   * name and position, and the cache emptied whenever the code changes.
   */
  useEffect(() => {
    if (status !== 'ready') return
    const host = hostRef.current
    if (!host) return
    const editor = editorFromHost(host)
    if (!editor) return

    const cache = new Map<string, SignatureItem[]>()
    let pending: AbortController | null = null
    let timer: ReturnType<typeof setTimeout> | null = null
    let shown: string | null = null

    const cancel = () => {
      if (timer !== null) clearTimeout(timer)
      timer = null
      pending?.abort()
      pending = null
    }

    const clear = () => {
      cancel()
      shown = null
      setHover(null)
    }

    const ask = async (event: MouseEvent) => {
      const position = editor.coordsChar({ left: event.clientX, top: event.clientY })
      const token = editor.getTokenAt(position)
      if (!isLookupToken(token)) {
        clear()
        return
      }

      const key = `${position.line}:${token.end}:${token.string}`
      if (key === shown) return
      shown = key

      // Kept inside the window: clamped away from the right edge, and drawn
      // above the pointer rather than below it near the bottom.
      const point = {
        x: Math.min(event.clientX, window.innerWidth - TOOLTIP_WIDTH),
        y: event.clientY,
        flip: event.clientY > window.innerHeight - TOOLTIP_CLEARANCE,
        word: token.string
      }
      const cached = cache.get(key)
      if (cached) {
        setHover(cached.length > 0 ? { ...point, items: cached } : null)
        return
      }

      setHover({ ...point, items: null })
      pending?.abort()
      const controller = new AbortController()
      pending = controller
      try {
        const items = await lookupSignatures({
          code: editor.getValue(),
          line: position.line,
          ch: token.end,
          word: token.string,
          version,
          signal: controller.signal
        })
        if (controller.signal.aborted) return
        cache.set(key, items)
        // Nothing to say is said by saying nothing, rather than by a tooltip
        // reading "no signature" over every second word.
        setHover(items.length > 0 ? { ...point, items } : null)
      } catch {
        if (!controller.signal.aborted) setHover(null)
      }
    }

    const onMove = (event: MouseEvent) => {
      if (timer !== null) clearTimeout(timer)
      timer = setTimeout(() => void ask(event), HOVER_DELAY)
    }

    // Typing, scrolling and leaving all mean the tooltip is about to be wrong.
    const onChange = () => {
      cache.clear()
      clear()
    }

    host.addEventListener('mousemove', onMove)
    host.addEventListener('mouseleave', clear)
    editor.on('change', onChange)
    editor.on('scroll', clear)

    return () => {
      cancel()
      host.removeEventListener('mousemove', onMove)
      host.removeEventListener('mouseleave', clear)
      editor.off('change', onChange)
      editor.off('scroll', clear)
    }
  }, [status, version])

  /**
   * Saving keeps the snippet in this browser, in the IndexedDB store that
   * backs the list of saved snippets. There is no link to hand anyone: the
   * site's own store cannot be listed by design (a snippet is reachable only
   * by its id), so the list is the browser's, and so is the snippet.
   *
   * The id comes from the same generator the route handler uses, which is
   * what lets `/p/<id>` open one of these in a page of its own.
   */
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
    const id = newId()
    try {
      await putLocalSnippet(id, {
        code: current,
        // The resolved compiler, which differs from `version` if the server
        // no longer offers it and fell back to `latestStable`.
        kotlinVersion: fragmentRef.current?.state?.compilerVersion ?? version,
        createdAt: new Date().toISOString()
      })
      setSaved(true)
      onSavedRef.current?.(id)
    } catch {
      setSaveError('This browser will not let the page store anything locally.')
    } finally {
      setSaving(false)
    }
  }, [source, version])

  return (
    <div className={size === 'tall' ? 'kl-playground kl-playground--tall' : 'kl-playground'}>
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
            {saving ? 'Saving…' : 'Save'}
          </button>
        )}
      </div>

      <div ref={hostRef} className="kl-playground__host" />

      {hover && (
        <div
          className="kl-signature"
          role="tooltip"
          data-flip={hover.flip}
          style={{ left: hover.x, top: hover.y }}
        >
          {hover.items === null ? (
            <span className="kl-signature__pending">Looking up {hover.word}…</span>
          ) : (
            hover.items.slice(0, MAX_SIGNATURES).map((item, index) => (
              <code className="kl-signature__line" key={`${item.text}-${index}`}>
                {formatSignature(item)}
                {item.import && <span className="kl-signature__import">needs an import</span>}
              </code>
            ))
          )}
          {hover.items !== null && hover.items.length > MAX_SIGNATURES && (
            <span className="kl-signature__more">
              and {hover.items.length - MAX_SIGNATURES} more overloads
            </span>
          )}
        </div>
      )}

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

      {saved && (
        <p className="kl-playground__note" role="status">
          Saved in this browser, to the list in the playground drawer.
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
