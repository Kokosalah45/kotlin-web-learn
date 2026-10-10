// Examples for /collection-operations/retrieving-collection-parts
// Adapted from https://kotlinlang.org/docs/collection-parts.html
package examples.collectionoperations

fun main() {
    val numbers = listOf("one", "two", "three", "four", "five", "six")

    println("== slice ==")
    println(numbers.slice(1..3))
    println(numbers.slice(0..4 step 2))
    println(numbers.slice(setOf(3, 5, 0)))

    println("== take / drop ==")
    println(numbers.take(3))
    println(numbers.takeLast(3))
    println(numbers.drop(1))
    println(numbers.dropLast(5))

    println("== take / drop beyond the size ==")
    println(numbers.take(100))
    println(numbers.drop(100))

    println("== takeWhile / dropWhile ==")
    println(numbers.takeWhile { !it.startsWith('f') })
    println(numbers.takeLastWhile { it != "three" })
    println(numbers.dropWhile { it.length == 3 })
    println(numbers.dropLastWhile { it.contains('i') })

    println("== takeWhile stops at the first mismatch ==")
    println(numbers.takeWhile { it.length == 3 })
    println(numbers.filter { it.length == 3 })

    println("== chunked ==")
    val upTo13 = (0..13).toList()
    println(upTo13.chunked(3))
    println(upTo13.chunked(3) { it.sum() })

    println("== windowed ==")
    val words = listOf("one", "two", "three", "four", "five")
    println(words.windowed(3))
    val upTo10 = (1..10).toList()
    println(upTo10.windowed(3, step = 2, partialWindows = true))
    println(upTo10.windowed(3) { it.sum() })

    println("== zipWithNext ==")
    println(words.zipWithNext())
    println(words.zipWithNext { s1, s2 -> s1.length > s2.length })
}
