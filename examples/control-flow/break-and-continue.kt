// Examples for /control-flow/loops/break-and-continue
// Adapted from https://kotlinlang.org/docs/returns.html
package examples.controlflow

fun firstFactorOf(n: Int): Int {
    for (i in 2..n) {
        if (n % i == 0) return i
    }
    return n
}

fun main() {
    println("== break ==")
    for (i in 1..10) {
        if (i > 4) break
        print(i)
    }
    println()

    println("== continue ==")
    for (i in 1..10) {
        if (i % 2 == 0) continue
        print(i)
    }
    println()

    println("== both affect the NEAREST enclosing loop ==")
    for (i in 1..3) {
        for (j in 1..3) {
            if (j == 2) break
            print("$i$j ")
        }
    }
    println()
    for (i in 1..3) {
        for (j in 1..3) {
            if (j == 2) continue
            print("$i$j ")
        }
    }
    println()

    println("== continue in a while must not skip the update ==")
    var i = 0
    var skipped = 0
    while (i < 6) {
        i++
        if (i % 2 == 0) {
            skipped++
            continue
        }
        print(i)
    }
    println(" (skipped $skipped)")

    println("== do-while: continue jumps to the CONDITION ==")
    var k = 0
    do {
        k++
        if (k < 3) continue
        print("body-tail-at-$k ")
    } while (k < 5)
    println()

    println("== break in a when inside a loop ==")
    for (n in listOf(1, 2, 3, 4)) {
        when (n) {
            3 -> break
            else -> print(n)
        }
    }
    println()

    println("== they are expressions of type Nothing ==")
    for (n in 1..5) {
        val doubled = if (n == 3) continue else n * 2
        print("$doubled ")
    }
    println()

    println("== there is no break out of forEach ==")
    run loop@{
        listOf(1, 2, 3, 4, 5).forEach {
            if (it == 3) return@loop
            print(it)
        }
        print(" never reached")
    }
    println(" done with nested loop")

    println("== return often beats break ==")
    println(firstFactorOf(91))
    println(firstFactorOf(97))

    println("== the stdlib alternatives ==")
    val numbers = listOf(4, 8, 15, 16, 23, 42)
    println(numbers.takeWhile { it < 16 })
    println(numbers.dropWhile { it < 16 })
    println(numbers.filterNot { it % 2 == 0 })
    println(numbers.first { it > 10 })
    println(numbers.any { it > 40 })
}
