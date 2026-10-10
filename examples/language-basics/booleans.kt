// Examples for /language-basics/basic-types/booleans
// Adapted from https://kotlinlang.org/docs/booleans.html
package examples.languagebasics

var sideEffects = 0

fun loud(name: String, value: Boolean): Boolean {
    sideEffects++
    println("  evaluated $name")
    return value
}

fun main() {
    println("== declaring ==")
    val isTrue: Boolean = true
    val isFalse = false
    println("$isTrue $isFalse ${isTrue::class.simpleName}")
    val isEnabled: Boolean? = null
    println(isEnabled)

    println("== producing Booleans ==")
    val number = 10
    println(number > 0)
    println("Kotlin".isEmpty())
    println("Kotlin".isNotEmpty())
    println(number in 1..100)
    println(listOf(1, 2, 3).contains(2))

    println("== negation, AND, OR, XOR ==")
    println(!isTrue)
    println(listOf(false && false, false && true, true && false, true && true))
    println(listOf(false || false, false || true, true || false, true || true))
    println(listOf(false xor false, false xor true, true xor false, true xor true))

    println("== && and || short-circuit ==")
    sideEffects = 0
    println(loud("left", false) && loud("right", true))
    println("evaluations: $sideEffects")

    sideEffects = 0
    println(loud("left", true) || loud("right", false))
    println("evaluations: $sideEffects")

    println("== and / or / xor do not ==")
    sideEffects = 0
    println(loud("left", false) and loud("right", true))
    println("evaluations: $sideEffects")

    sideEffects = 0
    println(loud("left", true) or loud("right", false))
    println("evaluations: $sideEffects")

    println("== precedence: ! then infix then && then || ==")
    println(true || false && false)
    println((true || false) && false)
    println(true || true && false)
    println(false && true xor true)
    println(!false && false)

    println("== no truthiness ==")
    val isEven = 4 % 2 == 0
    if (isEven) println("The number is even.") else println("The number is odd.")
    println(isEven == true)

    println("== Boolean? needs care ==")
    val maybe: Boolean? = null
    println(maybe == true)
    println(maybe != true)
    println(maybe ?: false)
    println(maybe?.not())

    println("== in a when ==")
    val n = 3
    when {
        n > 0 -> println("The number is positive.")
        n < 0 -> println("The number is negative.")
        else -> println("The number is zero.")
    }

    println("== in a while ==")
    var isCalculating = true
    while (isCalculating) {
        println("Calculating...")
        isCalculating = false
    }

    println("== parsing ==")
    println("true".toBooleanStrict())
    println("TRUE".toBoolean())
    println("yes".toBooleanStrictOrNull())
    println(runCatching { "yes".toBooleanStrict() }.exceptionOrNull()?.let { it::class.simpleName })
    println("TRUE".toBooleanStrictOrNull())

    println("== compare and sort ==")
    println(false < true)
    println(listOf(true, false, true).sorted())
    println(true.compareTo(false))
}
