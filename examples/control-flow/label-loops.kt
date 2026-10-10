// Examples for /control-flow/loops/label-loops
// Adapted from https://kotlinlang.org/docs/returns.html#break-and-continue-labels
package examples.controlflow

fun findPairSummingTo(target: Int, xs: List<Int>): Pair<Int, Int>? {
    outer@ for (a in xs) {
        for (b in xs) {
            if (a + b == target) return a to b
            if (a + b > target) continue@outer
        }
    }
    return null
}

fun main() {
    println("== unlabelled break leaves only the inner loop ==")
    for (i in 1..3) {
        for (j in 1..3) {
            if (j == 2) break
            print("$i$j ")
        }
    }
    println()

    println("== break@loop leaves the labelled one ==")
    loop@ for (i in 1..3) {
        for (j in 1..3) {
            if (j == 2) break@loop
            print("$i$j ")
        }
    }
    println()

    println("== continue@loop goes to the labelled loop's next iteration ==")
    loop2@ for (i in 1..3) {
        for (j in 1..3) {
            if (j == 2) continue@loop2
            print("$i$j ")
        }
        print("(end of i=$i) ")
    }
    println()

    println("== a label on the inner loop behaves like no label at all ==")
    for (i in 1..3) {
        inner@ for (j in 1..3) {
            if (j == 2) break@inner
            print("$i$j ")
        }
    }
    println()

    println("== three levels ==")
    top@ for (a in 1..2) {
        for (b in 1..2) {
            for (c in 1..2) {
                if (c == 2 && b == 2) break@top
                print("$a$b$c ")
            }
        }
    }
    println()

    println("== a practical use ==")
    println(findPairSummingTo(9, listOf(1, 3, 4, 6, 8)))
    println(findPairSummingTo(100, listOf(1, 3, 4, 6, 8)))

    println("== labels on a while loop ==")
    var i = 0
    outer@ while (i < 4) {
        i++
        var j = 0
        while (j < 4) {
            j++
            if (j == 2) continue@outer
            print("$i$j ")
        }
    }
    println()

    println("== any expression can carry a label ==")
    val total = run sum@{
        var acc = 0
        for (n in 1..10) {
            acc += n
            if (acc > 20) return@sum acc
        }
        acc
    }
    println(total)

    println("== the implicit label on a lambda ==")
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return@forEach
        print(it)
    }
    println()

    println("== an explicit label on a lambda ==")
    listOf(1, 2, 3, 4, 5).forEach lit@{
        if (it == 3) return@lit
        print(it)
    }
    println()

    println("== simulating break out of forEach ==")
    run loop@{
        listOf(1, 2, 3, 4, 5).forEach {
            if (it == 3) return@loop
            print(it)
        }
    }
    println()

    println("== a label lets you return from the OUTER of two nested lambdas ==")
    val found = listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6)).let search@{ rows ->
        rows.forEach { row ->
            row.forEach { cell ->
                if (cell == 4) return@search "found $cell"
            }
        }
        "not found"
    }
    println(found)
}
