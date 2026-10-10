// Examples for /collection-operations/retrieving-single-elements
// Adapted from https://kotlinlang.org/docs/collection-elements.html
package examples.collectionoperations

import java.util.SortedSet

fun main() {
    println("== elementAt ==")
    val linked = linkedSetOf("one", "two", "three", "four", "five")
    println(linked.elementAt(3))
    val sorted: SortedSet<String> = sortedSetOf("one", "two", "three", "four")
    println(sorted.elementAt(0))

    println("== first / last ==")
    val numbers = listOf("one", "two", "three", "four", "five")
    println(numbers.first())
    println(numbers.last())

    println("== safe positional access ==")
    println(numbers.elementAtOrNull(5))
    println(numbers.elementAtOrElse(5) { index -> "The value for index $index is undefined" })

    println("== first / last with a predicate ==")
    val six = listOf("one", "two", "three", "four", "five", "six")
    println(six.first { it.length > 3 })
    println(six.last { it.startsWith("f") })

    println("== firstOrNull ==")
    println(six.firstOrNull { it.length > 6 })

    println("== find / findLast ==")
    val ints = listOf(1, 2, 3, 4)
    println(ints.find { it % 2 == 0 })
    println(ints.findLast { it % 2 == 0 })

    println("== firstNotNullOf ==")
    val mixed = listOf<Any>(0, "true", false)
    println(mixed.firstNotNullOf { item -> item.toString().takeIf { it.length >= 4 } })

    println("== what first() throws on no match ==")
    println(runCatching { six.first { it.length > 99 } }.exceptionOrNull()?.let { it::class.simpleName })

    println("== contains / in / containsAll ==")
    println(six.contains("four"))
    println("zero" in six)
    println(six.containsAll(listOf("four", "two")))
    println(six.containsAll(listOf("one", "zero")))

    println("== isEmpty / isNotEmpty ==")
    println(six.isEmpty())
    println(six.isNotEmpty())
    val empty = emptyList<String>()
    println(empty.isEmpty())
    println(empty.isNotEmpty())

    println("== randomOrNull on an empty collection ==")
    println(empty.randomOrNull())
}
