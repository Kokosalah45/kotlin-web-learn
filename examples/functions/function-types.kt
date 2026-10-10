// Examples for /functions/function-types
// Adapted from https://kotlinlang.org/docs/lambdas.html#function-types
package examples.functions

typealias Transform = (String) -> String

class IntTransformer : (Int) -> Int {
    override operator fun invoke(x: Int): Int = x * x
}

fun isOdd(n: Int): Boolean = n % 2 != 0

fun runTransformation(f: (String, Int) -> String): String = f("hello", 3)

fun main() {
    println("== writing the type out ==")
    val onClick: () -> Unit = { println("clicked") }
    onClick()
    val describe: (Int) -> String = { n -> "n is $n" }
    println(describe(7))
    val named: (x: Int, y: Int) -> String = { x, y -> "($x, $y)" }
    println(named(1, 2))

    println("== inference ==")
    val a = { i: Int -> i + 1 }
    println(a(41))
    println(a::class.java.genericInterfaces.joinToString())

    println("== nullable and nested function types ==")
    val maybe: ((Int, Int) -> Int)? = null
    println(maybe?.invoke(1, 2))
    val curriedAdd: (Int) -> (Int) -> Int = { x -> { y -> x + y } }
    println(curriedAdd(1)(2))

    println("== typealias ==")
    val shout: Transform = { it.uppercase() }
    println(shout("hello"))

    println("== the four ways to get an instance ==")
    val fromLambda: (Int) -> Boolean = { it % 2 != 0 }
    val fromAnonymous: (Int) -> Boolean = fun(n: Int): Boolean { return n % 2 != 0 }
    val fromReference: (Int) -> Boolean = ::isOdd
    val fromBoundReference: () -> String = "kodee"::uppercase
    val fromClass: (Int) -> Int = IntTransformer()
    println(listOf(fromLambda(3), fromAnonymous(3), fromReference(3)))
    println(fromBoundReference())
    println(fromClass(9))

    println("== invoking ==")
    val stringPlus: (String, String) -> String = String::plus
    val intPlus: Int.(Int) -> Int = Int::plus
    println(stringPlus.invoke("<-", "->"))
    println(stringPlus("Hello, ", "world!"))
    println(intPlus.invoke(1, 1))
    println(intPlus(1, 2))
    println(2.intPlus(3))

    println("== receiver and first parameter are interchangeable ==")
    val repeatFun: String.(Int) -> String = { times -> this.repeat(times) }
    val twoParameters: (String, Int) -> String = repeatFun
    println(repeatFun("ab", 3))
    println(twoParameters("ab", 3))
    println(runTransformation(repeatFun))

    println("== a function type is an interface at runtime ==")
    println(onClick is Function0<*>)
    println(describe is Function1<*, *>)
    println(IntTransformer() is Function1<*, *>)
}
