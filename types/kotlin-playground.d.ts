/**
 * `kotlin-playground` ships no type declarations (its package.json exposes only
 * `main: dist/playground.min.js`, a UMD bundle). These types are transcribed
 * from the sources the bundle is built from:
 *
 *   src/index.js                    — create(selector, options), the option names
 *   src/executable-code/index.js    — target may be string | Node | NodeList,
 *                                     and the returned instances carry destroy()
 *   src/executable-code/executable-fragment.js
 *                                   — getInstance() receives the fragment, which
 *                                     is what exposes getCode()/execute()
 *
 * Only the members this site actually uses are declared.
 */
declare module 'kotlin-playground' {
  /**
   * The ExecutableFragment handed to `getInstance`. `getCode()` returns the
   * editor's current text, including the folded prefix/suffix when the snippet
   * is collapsed.
   */
  export interface PlaygroundFragment {
    getCode(): string
    execute(): void
    /**
     * Monkberry view state. `compilerVersion` is the version actually resolved
     * by `ExecutableCode.create()` — the element's `data-version`, else the
     * `version` option, else the server's `latestStable` — so it is the
     * authoritative answer to "which compiler ran this?". Optional because it
     * is only present once the view has been updated.
     */
    state: {
      code: string
      output: string | null
      folded: boolean
      theme: string
      compilerVersion?: string
    }
  }

  /** One mounted editor. `destroy()` unmounts it and restores the target node. */
  export interface ExecutableCode {
    destroy(): void
  }

  export interface PlaygroundOptions {
    /**
     * Compile server. The published bundle bakes
     * `https://api.kotlinlang.org/` in via webpack's `__WEBDEMO_URL__` define,
     * and `src/config.js` reads `__WEBDEMO_URL__ || RUNTIME_CONFIG.server` —
     * so the `data-server` script attribute is dead in the npm build and this
     * option is the only way to repoint it.
     */
    server?: string
    /** Falls back to the server's `latestStable` when absent or unknown. */
    version?: string
    /** Fires on editor changes, debounced 500ms (DEBOUNCE_TIME). */
    onChange?(code: string): void
    onRun?(): void
    onError?(): void
    onOpenConsole?(): void
    onCloseConsole?(): void
    onTestPassed?(): void
    onTestFailed?(): void
    getJsCode?(code: string): void
    callback?(targetNode: HTMLElement, mountNode: HTMLElement): void
    getInstance?(fragment: PlaygroundFragment): void
  }

  /**
   * Resolves once the server's `/versions` list has been fetched and every
   * matching node is mounted. Nodes that are empty or already carry
   * `data-kotlin-playground-initialized` are skipped, so the array can be
   * shorter than the selector's match count.
   */
  export default function create(
    target: string | Node | NodeList,
    options?: PlaygroundOptions
  ): Promise<ExecutableCode[]>
}
