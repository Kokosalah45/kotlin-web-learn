// Examples for /functions/standard-functions
// Adapted from https://kotlinlang.org/docs/functions.html
package examples.functions

// Block body: the return type is never inferred, so it has to be written out.
fun double(x: Int): Int {
    return 2 * x
}

// Single-expression body: the return type IS inferred.
fun triple(x: Int) = 3 * x

// ...but an inferred type may not be the one you wanted.
fun halfAsNumber(x: Int): Number = x / 2.0

fun powerOf(
    number: Int,
    exponent: Int, // trailing comma
): Int = Math.pow(number.toDouble(), exponent.toDouble()).toInt()

// No explicit return type and a block body means Unit.
fun shout(text: String) {
    println(text.uppercase())
}

fun <T> firstOrLabel(items: List<T>, label: T): T = items.firstOrNull() ?: label

class MyStringCollection {
    val items = mutableListOf<String>()

    infix fun add(s: String) {
        println("Adding: $s")
        items += s
    }

    fun build() {
        add("first")      // ordinary call
        this add "second" // infix call, explicit receiver required
    }

    fun printAll() = println("Items = $items")
}

infix fun Int.timesPlus(x: Int): Int = this * x + x

fun main() {
    println("== block vs single-expression ==")
    println(double(5))
    println(triple(5))
    println(halfAsNumber(5))
    println(powerOf(2, 10))

    println("== Unit ==")
    shout("hello")
    val result = shout("again")
    println(result)
    println(result == Unit)
    println(Unit === Unit)
    println(shout("x") === shout("y"))

    println("== generic ==")
    println(firstOrLabel(listOf("a", "b"), "none"))
    println(firstOrLabel(emptyList<String>(), "none"))

    println("== infix ==")
    val myStrings = MyStringCollection()
    myStrings.build()
    myStrings.printAll()
    println(1 timesPlus 2 + 3)
    println(1 timesPlus (2 + 3))
    println((1 timesPlus 2) + 3)
}
