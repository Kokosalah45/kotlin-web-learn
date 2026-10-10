// Examples for /control-flow/loops/while
// Adapted from https://kotlinlang.org/docs/control-flow.html#while-loops
package examples.controlflow

fun main() {
    println("== while ==")
    var carsInGarage = 0
    val maxCapacity = 3
    while (carsInGarage < maxCapacity) {
        println("Car entered. Cars now in garage: ${++carsInGarage}")
    }
    println("Garage is full!")

    println("== while may never run ==")
    var full = 3
    while (full < maxCapacity) {
        println("this never prints")
        full++
    }
    println("skipped the body entirely")

    println("== do-while always runs once ==")
    var attempts = 0
    do {
        attempts++
        println("Attempt $attempts")
    } while (attempts < 0)
    println("attempts = $attempts")

    println("== a deterministic do-while ==")
    val rolls = listOf(2, 4, 6, 1)
    var index = 0
    var roll: Int
    do {
        roll = rolls[index++]
        println("Rolled a $roll")
    } while (roll != 6)
    println("Got a 6! Game over.")

    println("== the condition sees variables declared in the body ==")
    var remaining = 5
    do {
        val half = remaining / 2
        remaining = half
    } while (half > 0)
    println("remaining = $remaining")

    println("== while(true) with break ==")
    var n = 0
    while (true) {
        n++
        if (n >= 3) break
    }
    println("n = $n")

    println("== a hand-rolled iterator loop ==")
    val iterator = listOf("a", "b", "c").iterator()
    while (iterator.hasNext()) print(iterator.next())
    println()

    println("== digits of a number, the classic while ==")
    var value = 9402
    val digits = mutableListOf<Int>()
    while (value > 0) {
        digits += value % 10
        value /= 10
    }
    println(digits.reversed())
}
