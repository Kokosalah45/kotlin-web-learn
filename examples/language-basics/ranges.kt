// Examples for /language-basics/ranges and /language-basics/progressions
// Adapted from https://kotlinlang.org/docs/ranges.html
package examples.languagebasics

fun main() {
    println("== closed-ended vs open-ended ==")
    println(4 in 1..4)
    println(4 in 1..<4)

    println("== iterating ==")
    for (i in 1..4) print(i)
    println()
    for (i in 4 downTo 1) print(i)
    println()

    println("== step ==")
    for (i in 0..8 step 2) print(i)
    println()
    for (i in 0..<8 step 2) print(i)
    println()
    for (i in 8 downTo 0 step 2) print(i)
    println()

    println("== the last element is not always the end value ==")
    for (i in 1..9 step 3) print(i)
    println()
    val progression = 1..9 step 3
    println("first=${progression.first} last=${progression.last} step=${progression.step}")

    println("== progressions are Iterable ==")
    println((1..10).filter { it % 2 == 0 })
    println((1..10).map { it * it }.take(4))

    println("== char and long ranges ==")
    println(('a'..'e').toList())
    println((1L..5L).sum())

    println("== an empty range ==")
    val empty = 5..1
    println(empty.toList())
    println(empty.isEmpty())

    println("== downTo needs no reversed step sign ==")
    println((5 downTo 1).toList())
    println((5 downTo 1 step 2).toList())

    println("== ranges of non-integral types support `in` but not iteration ==")
    val doubles = 1.0..2.0
    println(1.5 in doubles)
    println(doubles::class.simpleName)

    println("== rangeTo / rangeUntil as functions ==")
    println(1.rangeTo(4).toList())
    println(1.rangeUntil(4).toList())
}
