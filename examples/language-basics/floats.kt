// Examples for /language-basics/basic-types/floats
// Adapted from https://kotlinlang.org/docs/numbers.html#floating-point-types
package examples.languagebasics

import kotlin.math.abs
import kotlin.math.roundToInt

fun generalizedEquals(a: Any, b: Any): Boolean = a == b

fun main() {
    println("== the two types ==")
    println("Float  ${Float.SIZE_BITS} bits, 24 significant bits, 6-7 decimal digits")
    println("Double ${Double.SIZE_BITS} bits, 53 significant bits, 15-16 decimal digits")
    println("Float range:  ${Float.MIN_VALUE} .. ${Float.MAX_VALUE}")
    println("Double range: ${Double.MIN_VALUE} .. ${Double.MAX_VALUE}")

    println("== literals and inference ==")
    val pi = 3.14
    val avogadro = 6.02214076e23
    val eFloat = 2.7182817f
    println("${pi::class.simpleName} $pi")
    println("${avogadro::class.simpleName} $avogadro")
    println("${eFloat::class.simpleName} $eFloat")

    println("== a Float literal is rounded to what Float can store ==")
    val tooPrecise = 3.141592653589793f
    println(tooPrecise)
    println(3.141592653589793)
    println(0.1f)
    println(0.1f.toDouble())

    println("== arithmetic ==")
    println(3.14 * 2.71)
    println(10.0 / 3)
    println(0.1 + 0.2)
    println(0.1 + 0.2 == 0.3)
    println((0.1 + 0.2) - 0.3)

    println("== division by zero does not throw ==")
    println(2.0 / 0.0)
    println(-2.0 / 0.0)
    println(0.0 / 0.0)
    println(Double.MAX_VALUE * 2)
    println(runCatching { 2 / 0 }.exceptionOrNull()?.let { it::class.simpleName })

    println("== NaN is not equal to itself ==")
    val nan = Double.NaN
    println(nan == nan)
    println(nan != nan)
    println(nan.isNaN())
    println(nan < 1.0)
    println(nan > 1.0)
    println(nan + 1.0)

    println("== zero has a sign ==")
    println(0.0 == -0.0)
    println(1.0 / 0.0)
    println(1.0 / -0.0)
    println(0.0.toString())
    println((-0.0).toString())

    println("== but through a generic type the rules change ==")
    println(generalizedEquals(Double.NaN, Double.NaN))
    println(generalizedEquals(0.0, -0.0))
    println(listOf(Double.NaN).contains(Double.NaN))
    println(setOf(0.0, -0.0).size)
    println(listOf(1.0, Double.NaN, Double.POSITIVE_INFINITY, -0.0, 0.0).sorted())

    println("== conversions ==")
    println(1.9.toInt())
    println((-1.9).toInt())
    println(1.9.roundToInt())
    println((-1.9).roundToInt())
    println(1.5f.toDouble())
    println(1.1f.toDouble())
    println(Double.NaN.toInt())
    println(Double.POSITIVE_INFINITY.toInt())

    println("== comparing safely ==")
    val a = 0.1 + 0.2
    val b = 0.3
    println(abs(a - b) < 1e-9)
    println(java.math.BigDecimal("0.1").add(java.math.BigDecimal("0.2")))

    println("== nullable floats ==")
    val maybeAbsent: Double? = null
    println(maybeAbsent)
}
