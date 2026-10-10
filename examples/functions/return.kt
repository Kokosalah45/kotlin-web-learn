// Examples for /functions/standard-functions/return
// Adapted from https://kotlinlang.org/docs/functions.html#return-types
// and https://kotlinlang.org/docs/returns.html
package examples.functions

data class OrderSummary(
    val subtotal: Double,
    val tax: Double,
)

fun calculateOrderSummary(prices: List<Double>): OrderSummary {
    val subtotal = prices.sum()
    val tax = subtotal * 0.2
    return OrderSummary(subtotal, tax)
}

fun calculateOrderTotals(prices: List<Double>): Pair<Double, Double> {
    val subtotal = prices.sum()
    return Pair(subtotal, subtotal * 0.2)
}

fun displayName(userId: String?): String = "user-$userId"

// A `return` inside an expression body forces an explicit return type.
fun getDisplayNameOrDefault(userId: String?): String =
    displayName(userId ?: return "default")

fun fail(message: String): Nothing = throw IllegalStateException(message)

fun describe(value: Any?): String = when (value) {
    null -> fail("value was null")
    is Int -> "Int $value"
    else -> "other $value"
}

// return from the lambda, not from this function (explicit label).
fun printUpToThreeExplicit() {
    listOf(1, 2, 3, 4, 5).forEach lit@{
        if (it == 3) return@lit
        print(it)
    }
    println(" done with explicit label")
}

// Same, with the implicit label named after the function the lambda went to.
fun printUpToThreeImplicit() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return@forEach
        print(it)
    }
    println(" done with implicit label")
}

// An unlabelled `return` inside a lambda passed to an inline function
// leaves the WHOLE function.
fun printUpToThreeNonLocal() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return
        print(it)
    }
    println(" this line is never reached")
}

fun hasZeros(ints: List<Int>): Boolean {
    ints.forEach {
        if (it == 0) return true // returns from hasZeros
    }
    return false
}

fun main() {
    println("== returning several values ==")
    val summary = calculateOrderSummary(listOf(12.50, 8.00, 4.50))
    println(summary)
    println(summary.subtotal)
    println(summary.tax)
    val totals = calculateOrderTotals(listOf(12.50, 8.00, 4.50))
    println(totals)
    println(totals.first)
    println(totals.second)

    println("== return inside an expression body ==")
    println(getDisplayNameOrDefault("kodee"))
    println(getDisplayNameOrDefault(null))

    println("== Nothing ==")
    println(describe(42))
    println(runCatching { describe(null) }.exceptionOrNull()?.message)

    println("== local returns ==")
    printUpToThreeExplicit()
    printUpToThreeImplicit()

    println("== non-local return ==")
    printUpToThreeNonLocal()
    println("back in main")
    println(hasZeros(listOf(1, 2, 0, 3)))
    println(hasZeros(listOf(1, 2, 3)))
}
