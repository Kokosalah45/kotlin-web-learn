// Compiles and runs every example under examples/ and records what each one
// actually prints, so the output shown on a page is observed rather than
// predicted. Outputs land in examples/.outputs.json, keyed by file path.
//
//   pnpm examples:run           compile + run + write .outputs.json
//   pnpm examples:run --check   fail if any recorded output changed
//
// Needs kotlinc on PATH, or KOTLINC pointing at the compiler's bin directory.
// Each example is a standalone file with its own `main()`; the file path
// determines the JVM class name (foo-bar.kt -> FooBarKt).
import { readdir, readFile, writeFile, mkdir, rm } from 'node:fs/promises'
import { existsSync } from 'node:fs'
import { execFile } from 'node:child_process'
import { promisify } from 'node:util'
import path from 'node:path'

const run = promisify(execFile)
const check = process.argv.includes('--check')

const SRC = 'examples'
const OUT = '.examples-build'
const RESULTS = 'examples/.outputs.json'

const KOTLINC = process.env.KOTLINC ? path.join(process.env.KOTLINC, 'kotlinc') : 'kotlinc'

async function walk(dir) {
  const out = []
  for (const entry of await readdir(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name)
    if (entry.isDirectory()) out.push(...(await walk(full)))
    else if (entry.name.endsWith('.kt')) out.push(full)
  }
  return out
}

if (!existsSync(SRC)) {
  console.log('no examples/ directory - nothing to run')
  process.exit(0)
}

const files = (await walk(SRC)).sort()
if (!files.length) {
  console.log('no .kt files under examples/')
  process.exit(0)
}
console.log(`${files.length} example file(s)`)

await rm(OUT, { recursive: true, force: true })
await mkdir(OUT, { recursive: true })

// One compile for everything: far faster than invoking kotlinc per file.
console.log('compiling...')
const started = Date.now()
try {
  await run(KOTLINC, [...files, '-include-runtime', '-d', `${OUT}/examples.jar`, '-nowarn'], {
    maxBuffer: 64 * 1024 * 1024,
    shell: process.platform === 'win32'
  })
} catch (err) {
  console.error('compilation failed:\n')
  console.error(err.stderr || err.message)
  process.exit(1)
}
console.log(`compiled in ${((Date.now() - started) / 1000).toFixed(1)}s`)

// file path -> fully-qualified name of the file facade class Kotlin generates.
// The rule (verified against kotlinc 2.4.21): capitalize the first character,
// replace every character that is not valid in a Java identifier with `_`, and
// append `Kt` -- so `plus-and-minus-operators.kt` becomes
// `Plus_and_minus_operatorsKt`, NOT `PlusAndMinusOperatorsKt`. The file's own
// `package` declaration prefixes it.
const className = async (file) => {
  const source = await readFile(file, 'utf8')
  const pkg = source.match(/^\s*package\s+([\w.]+)/m)?.[1]
  const base = path.basename(file, '.kt')
  const facade = `${base.charAt(0).toUpperCase()}${base.slice(1)}`.replace(/[^\w$]/g, '_')
  return pkg ? `${pkg}.${facade}Kt` : `${facade}Kt`
}

const results = {}
let failed = 0
for (const file of files) {
  const key = file.split(path.sep).join('/')
  try {
    const { stdout } = await run('java', ['-cp', `${OUT}/examples.jar`, await className(file)], {
      maxBuffer: 16 * 1024 * 1024,
      shell: process.platform === 'win32'
    })
    results[key] = stdout.replace(/\r\n/g, '\n').trimEnd()
  } catch (err) {
    failed++
    console.error(`  RUN FAILED ${key}: ${(err.stderr || err.message).split('\n')[0]}`)
    results[key] = null
  }
}

if (check) {
  const previous = existsSync(RESULTS) ? JSON.parse(await readFile(RESULTS, 'utf8')) : {}
  const changed = Object.keys(results).filter((k) => previous[k] !== results[k])
  if (changed.length) {
    console.error(`\noutput changed for ${changed.length} example(s):`)
    for (const k of changed) console.error(`  ${k}`)
    process.exit(1)
  }
  console.log('all outputs match')
} else {
  await writeFile(RESULTS, `${JSON.stringify(results, null, 2)}\n`)
  console.log(`wrote ${RESULTS}`)
}

await rm(OUT, { recursive: true, force: true })
if (failed) process.exit(1)
