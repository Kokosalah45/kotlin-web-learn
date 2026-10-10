// Examples for /collection-operations/filtering
// Adapted from https://kotlinlang.org/docs/collection-filtering.html
package examples.collectionoperations

fun main() {
    val numbers = listOf("one", "two", "three", "four")

    println("== filter ==")
    println(numbers.filter { it.length > 3 })
    val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
    println(numbersMap.filter { (key, value) -> key.endsWith("1") && value > 10 })

    println("== filterIndexed / filterNot ==")
    println(numbers.filterIndexed { index, s -> (index != 0) && (s.length < 5) })
    println(numbers.filterNot { it.length <= 3 })

    println("== filterIsInstance ==")
    val mixed = listOf(null, 1, "two", 3.0, "four")
    mixed.filterIsInstance<String>().forEach { println(it.uppercase()) }

    println("== filterNotNull ==")
    val withNulls = listOf(null, "one", "two", null)
    withNulls.filterNotNull().forEach { println(it.length) }

    println("== partition ==")
    val (match, rest) = numbers.partition { it.length > 3 }
    println(match)
    println(rest)

    println("== any / none / all ==")
    println(numbers.any { it.endsWith("e") })
    println(numbers.none { it.endsWith("a") })
    println(numbers.all { it.endsWith("e") })
    println(emptyList<Int>().all { it > 5 })

    println("== any / none without predicate ==")
    val empty = emptyList<String>()
    println(numbers.any())
    println(empty.any())
    println(numbers.none())
    println(empty.none())
}
