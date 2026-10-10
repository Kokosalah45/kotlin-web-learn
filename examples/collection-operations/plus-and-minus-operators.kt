// Examples for /collection-operations/plus-and-minus-operators
// Adapted from https://kotlinlang.org/docs/collection-plus-minus.html
package examples.collectionoperations

fun main() {
    println("== plus / minus ==")
    val numbers = listOf("one", "two", "three", "four")
    val plusList = numbers + "five"
    val minusList = numbers - listOf("three", "four")
    println(plusList)
    println(minusList)
    println(numbers)

    println("== minus removes the first occurrence of a single element ==")
    val repeated = listOf("a", "b", "a", "c", "a")
    println(repeated - "a")
    println(repeated - listOf("a"))

    println("== += on a var read-only list rebinds ==")
    var readOnly = listOf(1, 2)
    val before = readOnly
    readOnly += 3
    println(readOnly)
    println("same instance as before: ${before === readOnly}")

    println("== += on a val mutable list mutates ==")
    val mutable = mutableListOf(1, 2)
    val alias = mutable
    mutable += 3
    println(mutable)
    println("same instance as alias: ${alias === mutable}")
}
