// Examples for /functions/standard-functions/return
// Adapted from https://kotlinlang.org/docs/returns.html
// and https://kotlinlang.org/docs/inline-functions.html
package examples.functions

// --- the inline UI-builder trap ------------------------------------------
// `content` is called between two prints, so a non-local return out of it
// skips the rest of the builder as well as the rest of the caller.
inline fun columnOf(content: () -> Unit) {
    println("<Column>")
    content()
    println("</Column>")
}

fun renderScreenBare(userName: String?) {
    columnOf {
        if (userName == null) return
        println("  Hello, $userName")
    }
    println("  footer")
}

fun renderScreenLabelled(userName: String?) {
    columnOf {
        if (userName == null) return@columnOf
        println("  Hello, $userName")
    }
    println("  footer")
}

// --- non-local return as a deliberate early exit --------------------------
fun firstFailingCheck(checks: List<Pair<String, Boolean>>): String? {
    checks.forEach { (name, passed) ->
        if (!passed) return name
    }
    return null
}

// --- the non-inline wall --------------------------------------------------
// A lambda that is stored and called later cannot return from the function
// that created it: by the time it runs, that function has already returned.
val pending = mutableListOf<() -> Unit>()

fun queueGreeting(userName: String?) {
    // The label has to be written by hand: an implicit one is named after the
    // function the lambda is passed to, and this lambda is not an argument to
    // one. `return` without a label is a compile error here either way.
    pending += greet@{
        if (userName == null) return@greet
        println("  queued hello to $userName")
    }
    println("  queueGreeting finished")
}

// --- crossinline and noinline, in the shapes that need them ---------------
inline fun runTwice(crossinline body: () -> Unit) {
    // Wrapping the lambda in another object is why `body` must be crossinline:
    // a non-local return out of it would have nowhere to return to.
    val task = Runnable { body() }
    task.run()
    task.run()
}

inline fun <T> remembered(noinline factory: () -> T): () -> T = factory

// --- anonymous function versus lambda -------------------------------------
fun anonymousFunctionLoop(values: List<Int>) {
    values.forEach(fun(value: Int) {
        if (value == 3) return
        print(value)
    })
    println(" | anonymousFunctionLoop finished")
}

fun lambdaLoop(values: List<Int>) {
    values.forEach { value ->
        if (value == 3) return
        print(value)
    }
    println(" | lambdaLoop finished")
}

// --- non-local break and continue (Kotlin 2.2 and later) ------------------
fun scanGrid(rows: List<List<Int>>) {
    for (row in rows) {
        row.forEach { cell ->
            if (cell < 0) continue
            if (cell > 99) break
            print(cell)
        }
    }
    println(" | scanGrid finished")
}

// --- label-heavy versus idiomatic -----------------------------------------
fun priceOfLabelled(items: List<String>, prices: Map<String, Int>): Int {
    var total = 0
    items.forEach { item ->
        val price = prices[item] ?: return@forEach
        if (price <= 0) return@forEach
        total += price
    }
    return total
}

fun priceOfLoop(items: List<String>, prices: Map<String, Int>): Int {
    var total = 0
    for (item in items) {
        val price = prices[item] ?: continue
        if (price <= 0) continue
        total += price
    }
    return total
}

fun priceOfIdiomatic(items: List<String>, prices: Map<String, Int>): Int =
    items.mapNotNull { prices[it] }.filter { it > 0 }.sum()

// --- labelled loops, where the label is on the loop and not on a lambda ---
fun findInGrid(rows: List<List<String>>, needle: String): String? {
    var found: String? = null
    rows@ for ((y, row) in rows.withIndex()) {
        for ((x, cell) in row.withIndex()) {
            if (cell == needle) {
                found = "$needle at ($x, $y)"
                break@rows
            }
        }
    }
    return found
}

fun main() {
    println("== inline builder, bare return ==")
    renderScreenBare("Kodee")
    renderScreenBare(null)

    println("== inline builder, labelled return ==")
    renderScreenLabelled(null)

    println("== non-local return as an early exit ==")
    println(firstFailingCheck(listOf("auth" to true, "quota" to false, "tos" to true)))
    println(firstFailingCheck(listOf("auth" to true)))

    println("== the non-inline wall ==")
    queueGreeting("Kodee")
    queueGreeting(null)
    pending.forEach { it() }

    println("== crossinline and noinline ==")
    runTwice { print("tick ") }
    println()
    val makeList = remembered { listOf(1, 2, 3) }
    println(makeList())

    println("== anonymous function versus lambda ==")
    anonymousFunctionLoop(listOf(1, 2, 3, 4, 5))
    lambdaLoop(listOf(1, 2, 3, 4, 5))
    // lambdaLoop's own trailing println never runs, so end the line here.
    println()

    println("== non-local break and continue ==")
    scanGrid(listOf(listOf(1, -2, 3), listOf(4, 100, 5)))

    println("== returning a value at a label ==")
    val scores = listOf(96, -1, 72)
    println(scores.map { score ->
        if (score < 0) return@map "n/a"
        if (score >= 90) "A" else "B"
    })

    println("== label-heavy versus idiomatic ==")
    val prices = mapOf("book" to 12, "pen" to 0)
    val basket = listOf("book", "pen", "ghost")
    println(priceOfLabelled(basket, prices))
    println(priceOfLoop(basket, prices))
    println(priceOfIdiomatic(basket, prices))

    println("== break out of nested loops ==")
    val grid = listOf(listOf("a", "b"), listOf("c", "x"), listOf("x", "d"))
    println(findInGrid(grid, "x"))

    println("== eager versus lazy short-circuit ==")
    var eagerTouched = 0
    val eager = (1..10).map { eagerTouched++; it * it }.first { it > 9 }
    var lazyTouched = 0
    val lazy = (1..10).asSequence().map { lazyTouched++; it * it }.first { it > 9 }
    println("eager: found $eager after $eagerTouched evaluations")
    println("lazy:  found $lazy after $lazyTouched evaluations")
}
