// Examples for /language-basics/data-types/type-inference
// Adapted from https://kotlinlang.org/docs/basic-syntax.html#variables
// and https://kotlinlang.org/docs/types-overview.html
package examples.languagebasics

// A single-expression function's return type is inferred.
fun sum(a: Int, b: Int) = a + b

// A block body's is not, so it has to be declared.
fun sumBlock(a: Int, b: Int): Int {
    return a + b
}

// The inferred type may be narrower than you want.
fun half(x: Int) = x / 2.0
fun halfAsNumber(x: Int): Number = x / 2.0

open class Animal2
class Dog2 : Animal2()
class Cat2 : Animal2()

fun main() {
    println("== inferred from the initializer ==")
    val n = 5
    val d = 5.0
    val f = 5.0f
    val l = 5L
    val s = "five"
    val c = '5'
    val b = true
    for (v in listOf<Any>(n, d, f, l, s, c, b)) println("$v -> ${v::class.simpleName}")

    println("== inferred for functions ==")
    println(sum(19, 23))
    println(sumBlock(19, 23))
    println(half(5))
    println(halfAsNumber(5))
    println(half(5)::class.simpleName)

    println("== inferred for lambdas ==")
    val double = { x: Int -> x * 2 }
    println(double(21))
    println(listOf(1, 2, 3).map { it.toString() })
    println(listOf(1, 2, 3).map { it.toString() }::class.simpleName)

    println("== inferred for generic calls ==")
    val ints = listOf(1, 2, 3)
    val mixed = listOf(1, "two")
    val nullable = listOf(1, null)
    println(ints.map { it::class.simpleName })
    println(mixed.map { it::class.simpleName })
    println(nullable.map { it?.let { v -> v::class.simpleName } })

    println("== the least common supertype ==")
    val animals = listOf(Dog2(), Cat2())
    println(animals.map { it::class.simpleName })
    println(animals.all { it is Animal2 })
    val widened: List<Animal2> = animals
    println(widened.size)

    println("== inference across a chain ==")
    val lengths = listOf("one", "three", "five").map { it.length }.filter { it > 3 }
    println(lengths)
    println(lengths::class.simpleName)

    println("== where inference will not help ==")
    // A declaration with no initializer needs a type.
    val later: Int
    later = 3
    println(later)
    // An empty collection has nothing to infer from.
    val empty = listOf<String>()
    println(empty.size)
    val emptyLeft: List<String> = listOf()
    println(emptyLeft.size)

    println("== numeric inference is worth pinning down ==")
    val asInt = 1
    val asLong: Long = 1
    println("${asInt::class.simpleName} / ${asLong::class.simpleName}")
    val sneaky = listOf(1, 2L)
    println(sneaky.map { it::class.simpleName })
    val pinned: List<Number> = listOf(1.toInt(), 2L)
    println(pinned.map { it::class.simpleName })

    println("== inference does not read the declared type backwards ==")
    val explicitDouble: Double = 1 / 2.0
    val wrong: Double = (1 / 2).toDouble()
    println(explicitDouble)
    println(wrong)

    println("== var fixes the type at declaration ==")
    var count = 0
    count = 42
    println(count)
    // count = "forty-two" // Error: assignment type mismatch
    var anything: Any = 0
    anything = "forty-two"
    println(anything)
}
