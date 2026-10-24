/**
 * Constants shared by the browser bundle and the route handlers. Kept apart
 * from `lib/snippets.ts` so importing them into a client component does not
 * drag `node:crypto` and the Redis client along with them.
 */

/**
 * The compiler every recorded output on this site was produced with (see
 * "Every output shown was actually executed" in the README). Pinning the
 * playground to it means the editor runs the same compiler as the `text`
 * blocks next to it, instead of whichever version api.kotlinlang.org currently
 * reports as `latestStable`.
 *
 * Confirmed present and flagged `latestStable` by
 * GET https://api.kotlinlang.org/versions on 2026-10-10.
 */
export const DEFAULT_KOTLIN_VERSION = '2.4.21'

/** 64 KiB. Large enough for any teaching snippet, small enough to reject abuse. */
export const MAX_CODE_BYTES = 64 * 1024

/**
 * The compile server `kotlin-playground` posts to. The published bundle bakes
 * this same origin in through webpack's `__WEBDEMO_URL__` define (see
 * `types/kotlin-playground.d.ts`), so naming it here points the hover lookup
 * at the server the editor beside it is already using, rather than at a second
 * one that could answer differently.
 */
export const KOTLIN_COMPILER_SERVER = 'https://api.kotlinlang.org'
