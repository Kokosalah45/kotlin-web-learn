// Examples for /functions/function-types/anonymous-functions
// Adapted from https://kotlinlang.org/docs/lambdas.html#anonymous-functions
package examples.functions

// Lambda: a bare `return` leaves the enclosing function (forEach is inline).
fun lambdaReturn(): String {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return "left lambdaReturn() at $it"
        print(it)
    }
    return "finished the loop"
}

// Anonymous function: the same `return` leaves only the anonymous function.
fun anonymousReturn(): String {
    listOf(1, 2, 3, 4, 5).forEach(fun(value: Int) {
        if (value == 3) return
        print(value)
    })
    return "finished the loop"
}

fun main() {
    println("== expression body and block body ==")
    val addExpression = fun(x: Int, y: Int): Int = x + y
    val addBlock = fun(x: Int, y: Int): Int {
        return x + y
    }
    println(addExpression(2, 3))
    println(addBlock(2, 3))

    println("== the return type can be stated, unlike in a lambda ==")
    val half = fun(x: Int): Double = x / 2.0
    println(half(5))
    println(half(5)::class.simpleName)

    println("== parameter types are inferable from context ==")
    val ints = listOf(-2, -1, 0, 1, 2)
    println(ints.filter(fun(item) = item > 0))

    println("== a block body infers nothing: it is Unit unless declared ==")
    val logIt = fun(s: String) {
        println("log: $s")
    }
    println(logIt("hello"))

    println("== return: lambda vs anonymous function ==")
    println()
    println(lambdaReturn())
    println(anonymousReturn())

    println("== an anonymous extension function ==")
    data class Order(val weight: Double)
    val calculateShipping = fun Order.(rate: Double): Double = this.weight * rate
    println(Order(2.5).calculateShipping(3.0))

    println("== anonymous functions go inside the parentheses ==")
    println(ints.count(fun(n: Int) = n < 0))
    println(ints.count { it < 0 })
}
