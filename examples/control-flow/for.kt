// Examples for /control-flow/loops/for
// Adapted from https://kotlinlang.org/docs/control-flow.html#for-loops
package examples.controlflow

fun main() {
    println("== over a collection ==")
    val shoppingList = listOf("Milk", "Bananas", "Bread")
    for (item in shoppingList) print("$item ")
    println()

    println("== over a range ==")
    for (i in 1..6) print(i)
    println()
    for (i in 1..<6) print(i)
    println()
    for (i in 6 downTo 0 step 2) print(i)
    println()
    for (c in 'a'..'e') print(c)
    println()

    println("== an empty range just does not run ==")
    for (i in 5..1) print(i)
    println("(nothing above)")

    println("== indices and withIndex ==")
    val routineSteps = arrayOf("Wake up", "Brush teeth", "Make coffee")
    for (i in routineSteps.indices) println("$i: ${routineSteps[i]}")
    for ((index, value) in routineSteps.withIndex()) println("The step at $index is \"$value\"")

    println("== destructuring a map ==")
    val capitals = linkedMapOf("France" to "Paris", "Japan" to "Tokyo")
    for ((country, capital) in capitals) println("$country -> $capital")

    println("== the loop variable is a val ==")
    val printers = mutableListOf<() -> Unit>()
    for (i in 1..3) printers += { print(i) }
    printers.forEach { it() }
    println()

    println("== a String iterates over Chars ==")
    for (c in "Kotlin") print("$c.")
    println()

    println("== modifying a list while iterating it ==")
    val removeLast = mutableListOf(1, 2, 3, 4, 5)
    println(runCatching {
        for (n in removeLast) if (n == 5) removeLast.remove(n)
    }.exceptionOrNull()?.let { it::class.simpleName } ?: "no exception")
    println(removeLast)

    val removeSecondToLast = mutableListOf(1, 2, 3, 4, 5)
    println(runCatching {
        for (n in removeSecondToLast) if (n == 4) removeSecondToLast.remove(n)
    }.exceptionOrNull()?.let { it::class.simpleName } ?: "no exception")
    println(removeSecondToLast)

    val removeFirst = mutableListOf(1, 2, 3, 4, 5)
    println(runCatching {
        for (n in removeFirst) if (n == 1) removeFirst.remove(n)
    }.exceptionOrNull()?.let { it::class.simpleName } ?: "no exception")
    println(removeFirst)

    println("== iterating a copy is the fix ==")
    val safe = mutableListOf(1, 2, 3, 4, 5)
    for (n in safe.toList()) if (n % 2 == 0) safe.remove(n)
    println(safe)

    println("== a range is not materialised ==")
    var sum = 0L
    for (i in 1..10_000_000) sum += i
    println(sum)
}
