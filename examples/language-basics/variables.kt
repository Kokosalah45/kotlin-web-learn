// Examples for /language-basics/variables
// Adapted from https://kotlinlang.org/docs/basic-syntax.html#variables
package examples.languagebasics

// Top-level properties.
val PI = 3.14
var counter = 0

fun incrementCounter() {
    counter += 1
}

fun main() {
    println("== val and var ==")
    val fixed: Int = 5
    println(fixed)
    var changing: Int = 5
    changing += 1
    println(changing)

    println("== the type can be left out ==")
    val inferred = 5
    println("$inferred is an ${inferred::class.simpleName}")
    val text = "Kotlin"
    println("$text is a ${text::class.simpleName}")

    println("== declare now, initialize later ==")
    val immediate = 5
    // No initializer, so the type is required.
    val later: Int
    later = 3
    println(immediate)
    println(later)

    println("== top-level properties ==")
    println("counter = $counter; PI = $PI")
    incrementCounter()
    println("incrementCounter()")
    println("counter = $counter; PI = $PI")

    println("== shadowing in a nested scope ==")
    val name = "outer"
    run {
        val name = "inner"
        println(name)
    }
    println(name)

    println("== a val can hold a mutable object ==")
    val numbers = mutableListOf(1, 2, 3)
    numbers.add(4)
    println(numbers)

    println("== conditional initialization still gives a val ==")
    val label: String
    if (fixed > 3) {
        label = "big"
    } else {
        label = "small"
    }
    println(label)

    println("== but an expression reads better ==")
    val betterLabel = if (fixed > 3) "big" else "small"
    println(betterLabel)

    println("== destructuring declarations ==")
    val (first, second) = 1 to "one"
    println("$first / $second")
    val (q, r) = divideWithRemainder(17, 5)
    println("$q remainder $r")
    val (_, onlySecond) = 1 to "one"
    println(onlySecond)

    println("== lateinit ==")
    val station = WeatherStation()
    station.printReading()
    station.latestReading = "22C, sunny"
    station.printReading()
    val broken = ReportGenerator()
    println(runCatching { broken.printReport() }.exceptionOrNull()?.let {
        "${it::class.simpleName}: ${it.message}"
    })

    println("== const ==")
    println(MAX_LOGIN_ATTEMPTS)
    println(Limits.TIMEOUT_MS)
}

data class DivisionResult(val quotient: Int, val remainder: Int)

fun divideWithRemainder(a: Int, b: Int) = DivisionResult(a / b, a % b)

const val MAX_LOGIN_ATTEMPTS = 3

object Limits {
    const val TIMEOUT_MS = 5_000
}

class WeatherStation {
    lateinit var latestReading: String

    fun printReading() {
        if (this::latestReading.isInitialized) {
            println("Latest reading: $latestReading")
        } else {
            println("No reading available")
        }
    }
}

class ReportGenerator {
    lateinit var report: String

    fun printReport() {
        println(report)
    }
}
