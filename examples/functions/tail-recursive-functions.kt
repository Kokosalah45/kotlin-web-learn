// Examples for /functions/tail-recursive-functions
// Adapted from https://kotlinlang.org/docs/functions.html#tail-recursive-functions
package examples.functions

import kotlin.math.abs
import kotlin.math.cos

// An arbitrary "good enough" precision
val eps = 1E-10

tailrec fun findFixPoint(x: Double = 1.0): Double =
    if (abs(x - cos(x)) < eps) x else findFixPoint(cos(x))

// The loop the compiler rewrites the above into.
fun findFixPointByLoop(): Double {
    var x = 1.0
    while (true) {
        val y = cos(x)
        if (abs(x - y) < eps) return x
        x = cos(x)
    }
}

// Ordinary recursion: one JVM frame per call.
fun countDown(n: Int): Int = if (n == 0) 0 else countDown(n - 1)

// Same shape, but the self-call is the whole body, so `tailrec` applies.
tailrec fun countDownTail(n: Int): Int = if (n == 0) 0 else countDownTail(n - 1)

// Accumulator-passing style is what makes a fold tail-recursive.
tailrec fun sumTo(n: Long, acc: Long = 0): Long = if (n == 0L) acc else sumTo(n - 1, acc + n)

// NOT a tail call: the multiplication happens after the recursive call returns.
fun factorial(n: Long): Long = if (n <= 1L) 1L else n * factorial(n - 1)

tailrec fun factorialTail(n: Long, acc: Long = 1L): Long =
    if (n <= 1L) acc else factorialTail(n - 1, acc * n)

fun main() {
    println("== the fixed point of cosine ==")
    println(findFixPoint())
    println(findFixPointByLoop())
    println(findFixPoint() == findFixPointByLoop())

    println("== without tailrec ==")
    println(runCatching { countDown(100_000) }.exceptionOrNull()?.let { it::class.simpleName })

    println("== with tailrec ==")
    println(countDownTail(100_000))
    println(countDownTail(10_000_000))

    println("== accumulator passing ==")
    println(sumTo(1_000_000))
    println(factorialTail(20))
    println(factorial(20))
}
