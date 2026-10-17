'use client'

import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { useCallback, useEffect, useRef, useState } from 'react'
import { Playground } from '@/components/playground'
import { SavedSnippets } from '@/components/saved-snippets'
import type { SavedSnippet } from '@/lib/local-snippets'

/** What an untouched dock editor opens with. */
const STARTER = `fun main() {
    println("Hello, Kotlin")
}`

/**
 * Where the dock keeps what the reader typed. `sessionStorage`, not
 * `localStorage`: it survives reloads and follows the reader from page to page
 * within the tab they are reading in, and goes away with that tab rather than
 * greeting them weeks later with a snippet they have forgotten writing.
 */
const STORAGE_KEY = 'kl:playground-dock'

/** Both calls can throw — Safari's private mode, a storage-blocking policy. */
const readSaved = (): string | null => {
  try {
    return sessionStorage.getItem(STORAGE_KEY)
  } catch {
    return null
  }
}

const writeSaved = (code: string) => {
  try {
    sessionStorage.setItem(STORAGE_KEY, code)
  } catch {
    // Not being able to remember the code is not worth breaking the editor.
  }
}

/**
 * The physical R key with Alt (Option on a Mac) held. `event.code` rather than
 * `event.key` because Option+R on macOS produces "®", and Alt+R is free in
 * every browser's own shortcuts — unlike Ctrl/Cmd+K, which the theme's search
 * already owns.
 */
const SHORTCUT_CODE = 'KeyR'

/**
 * Pages that already put a full-size playground in the content column. The
 * launcher is hidden there — a second editor floating over the first one would
 * only be a way to lose track of which is which.
 */
const hasOwnPlayground = (pathname: string) =>
  pathname === '/playground' || pathname.startsWith('/p/')

/**
 * The playground, reachable from every page.
 *
 * Mounted once in the root layout. The editor itself is only rendered after the
 * first open, so a reader who never asks for it never downloads the
 * `kotlin-playground` bundle (it is a dynamic import inside `<Playground>`) and
 * never hits `api.kotlinlang.org` for the version list.
 *
 * It is a drawer, not a dialog: there is no backdrop, the page behind it keeps
 * its scrolling and its clicks, and it is anchored to one corner rather than
 * spread across the viewport. Reading on and running code are meant to happen
 * at the same time, so the editor must not be something to dismiss first.
 *
 * After that first open the panel stays mounted and is hidden with
 * `visibility`, not `display: none`, so that whatever the reader typed — and
 * the output of their last run — is still there the next time they open it,
 * including after moving to another page, since this sits in the root layout.
 * `display: none` would also break the editor: CodeMirror measures itself when
 * it mounts, and `<Playground>` remounts it on a light/dark switch, which in a
 * display-none subtree would measure zero and come back blank. (That remount
 * carries the code across but not the output pane, here as anywhere else on
 * the site.)
 *
 * A full page reload drops all of that, so the contents are also written to
 * `sessionStorage` as they change and read back when the dock is next opened.
 * The write inherits kotlin-playground's 500ms debounce: a reload inside that
 * window loses the last few keystrokes, which is the price of not reaching
 * into the editor on every keypress.
 */
export function PlaygroundDock() {
  const pathname = usePathname()
  const [open, setOpen] = useState(false)
  /** The panel is rendered from the first open onwards, never before. */
  const [everOpened, setEverOpened] = useState(false)
  /**
   * What the editor opens with. Read from `sessionStorage` in an effect rather
   * than during render: the server has no `sessionStorage`, and a value read
   * during render would not match the HTML the server sent. Safe to settle
   * late because the editor is not rendered until the first open, and
   * `<Playground>` reads `code` only when it mounts.
   */
  const [initialCode, setInitialCode] = useState(STARTER)
  /** Filled in on the client; "Alt+R" is the right guess for most readers. */
  const [shortcutLabel, setShortcutLabel] = useState('Alt+R')
  const launcherRef = useRef<HTMLButtonElement>(null)
  const closeRef = useRef<HTMLButtonElement>(null)

  useEffect(() => {
    const saved = readSaved()
    if (saved !== null) setInitialCode(saved)
    if (/Mac|iPhone|iPad/.test(navigator.platform)) setShortcutLabel('⌥R')
  }, [])

  const show = useCallback(() => {
    setEverOpened(true)
    setOpen(true)
  }, [])

  const hide = useCallback(() => {
    setOpen(false)
    launcherRef.current?.focus()
  }, [])

  /** Bumped after a save, so the list pulls IndexedDB again. */
  const [listRevision, setListRevision] = useState(0)

  /**
   * The editor's `key`. Changing a key is what makes React build a fresh
   * editor around newly chosen code, since `<Playground>` reads `code` once,
   * when it mounts. Only opening a saved snippet moves it — a save must leave
   * the editor, its cursor and the output of the last run exactly as they are.
   */
  const [editorKey, setEditorKey] = useState(0)

  const openSaved = useCallback((snippet: SavedSnippet) => {
    setInitialCode(snippet.code)
    setEditorKey((current) => current + 1)
  }, [])

  const noteSaved = useCallback(() => setListRevision((current) => current + 1), [])

  // Escape closes, as a dialog should.
  useEffect(() => {
    if (!open) return
    const onKey = (event: KeyboardEvent) => {
      if (event.key === 'Escape') hide()
    }
    document.addEventListener('keydown', onKey)
    return () => document.removeEventListener('keydown', onKey)
  }, [open, hide])

  // Alt+R from anywhere on the site, open or closed. Registered even on the
  // pages that hide the launcher: it costs nothing and a reader who has learnt
  // the shortcut should not find it dead on one page in a hundred.
  useEffect(() => {
    const onKey = (event: KeyboardEvent) => {
      if (!event.altKey || event.ctrlKey || event.metaKey) return
      if (event.code !== SHORTCUT_CODE) return
      event.preventDefault()
      if (open) hide()
      else show()
    }
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [open, show, hide])

  // Move focus into the drawer so a keyboard user is not left behind on the
  // launcher. Nothing is trapped and nothing is `aria-modal`: tabbing past the
  // last control reaches the page again, which is the point of a drawer.
  useEffect(() => {
    if (open) closeRef.current?.focus()
  }, [open])

  const showLauncher = !hasOwnPlayground(pathname)

  // Nothing at all until there is something to show: no launcher on the
  // playground's own pages, and no panel before the first open.
  if (!showLauncher && !everOpened) return null

  return (
    <>
      {showLauncher && (
        <button
          type="button"
          ref={launcherRef}
          className="kl-dock-launcher"
          // Fades out under the open panel rather than unmounting, so focus
          // can return to it when the panel closes.
          data-dock-open={open}
          onClick={show}
          aria-expanded={open}
          aria-haspopup="dialog"
          title={`Open the Kotlin playground (${shortcutLabel})`}
        >
          <span aria-hidden="true" className="kl-dock-launcher__glyph">
            ▶
          </span>
          Run Kotlin
          <kbd aria-hidden="true" className="kl-dock-launcher__kbd">
            {shortcutLabel}
          </kbd>
        </button>
      )}

      {everOpened && (
        <div className="kl-dock" data-open={open} inert={!open}>
          <section
            className="kl-dock__panel"
            role="region"
            aria-label="Kotlin playground"
          >
            <header className="kl-dock__head">
              <span className="kl-dock__title">
                Kotlin playground
                <span className="kl-dock__hint">
                  runs on api.kotlinlang.org · {shortcutLabel} to close
                </span>
              </span>
              <Link className="kl-dock__link" href="/playground" onClick={hide}>
                Open full page
              </Link>
              <button
                type="button"
                ref={closeRef}
                className="kl-dock__close"
                onClick={hide}
                aria-label="Close the playground"
              >
                Close
              </button>
            </header>
            <div className="kl-dock__body">
              <Playground
                key={editorKey}
                code={initialCode}
                label="Scratch"
                size="tall"
                onCodeChange={writeSaved}
                onSaved={noteSaved}
              />
              <aside className="kl-dock__saved">
                <h2 className="kl-dock__saved-heading">
                  Saved
                  <span className="kl-dock__hint">in this browser</span>
                </h2>
                <SavedSnippets onOpen={openSaved} revision={listRevision} />
              </aside>
            </div>
          </section>
        </div>
      )}
    </>
  )
}
