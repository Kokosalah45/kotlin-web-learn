// Examples for /functions/function-types/lambda-functions
// Adapted from https://kotlinlang.org/docs/lambdas.html#lambda-expressions-and-anonymous-functions
package examples.functions

class HtmlBuilder {
    private val lines = mutableListOf<String>()
    fun body(text: String) {
        lines += "<body>$text</body>"
    }

    fun render() = lines.joinToString("\n")
}

fun html(init: HtmlBuilder.() -> Unit): HtmlBuilder {
    val builder = HtmlBuilder() // create the receiver object
    builder.init()              // pass the receiver object to the lambda
    return builder
}

fun main() {
    println("== the full form, then everything optional removed ==")
    val sumExplicit: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
    val sumInferred = { x: Int, y: Int -> x + y }
    println(sumExplicit(2, 3))
    println(sumInferred(2, 3))

    println("== the last expression is the return value ==")
    val classify = { n: Int ->
        val even = n % 2 == 0
        if (even) "even" else "odd"
    }
    println(classify(4))
    println(classify(5))

    println("== trailing lambda ==")
    val items = listOf(1, 2, 3, 4)
    println(items.fold(1) { acc, e -> acc * e })
    println(items.fold(1, { acc, e -> acc * e }))
    run { println("the only argument, so no parentheses at all") }

    println("== it ==")
    val ints = listOf(-2, -1, 0, 1, 2)
    println(ints.filter { it > 0 })
    println(ints.map { it * it })

    println("== it does not nest ==")
    println(listOf("ab", "cd").map { outer -> outer.map { it.uppercaseChar() } })

    println("== explicit return from a lambda ==")
    println(ints.filter {
        val shouldKeep = it > 0
        shouldKeep
    })
    println(ints.filter {
        val shouldKeep = it > 0
        return@filter shouldKeep
    })

    println("== underscore for unused parameters ==")
    val map = mapOf("a" to 1, "b" to 2)
    map.forEach { (_, value) -> println("$value!") }

    println("== destructuring ==")
    map.forEach { (key, value) -> println("$key -> $value") }
    println(listOf(1 to "one", 2 to "two").map { (n, name) -> "$n is $name" })

    println("== closures capture, and can write ==")
    var total = 0
    ints.filter { it > 0 }.forEach { total += it }
    println(total)

    var counter = 0
    val increment = { counter++ }
    increment()
    increment()
    increment()
    println(counter)

    println("== the capture is the variable, not its value ==")
    var message = "first"
    val printMessage = { println(message) }
    printMessage()
    message = "second"
    printMessage()

    println("== a lambda in a loop captures the loop variable per iteration ==")
    val printers = mutableListOf<() -> Unit>()
    for (i in 1..3) printers += { print(i) }
    printers.forEach { it() }
    println()

    println("== lambdas with receiver ==")
    val plus: Int.(Int) -> Int = { other -> plus(other) }
    println(2.plus(3))
    println(html { body("Hello") }.render())
}
