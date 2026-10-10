// Examples for /collection-operations/aggregate-operations
// Adapted from https://kotlinlang.org/docs/collection-aggregate.html
package examples.collectionoperations

fun main() {
    println("== basic aggregates ==")
    val numbers = listOf(6, 42, 10, 4)
    println("Count: ${numbers.count()}")
    println("Max: ${numbers.maxOrNull()}")
    println("Min: ${numbers.minOrNull()}")
    println("Average: ${numbers.average()}")
    println("Sum: ${numbers.sum()}")

    println("== on an empty collection ==")
    val empty = emptyList<Int>()
    println("maxOrNull: ${empty.maxOrNull()}")
    println("sum: ${empty.sum()}")
    println("average: ${empty.average()}")
    println("count: ${empty.count()}")

    println("== minBy / maxWith ==")
    val five = listOf(5, 42, 10, 4)
    println(five.minByOrNull { it % 3 })
    val strings = listOf("one", "two", "three", "four")
    println(strings.maxWithOrNull(compareBy { it.length }))

    println("== minBy vs minOf ==")
    println(five.minByOrNull { it % 3 })
    println(five.minOfOrNull { it % 3 })

    println("== sumOf ==")
    println(five.sumOf { it * 2 })
    println(five.sumOf { it.toDouble() / 2 })

    println("== fold vs reduce ==")
    val four = listOf(5, 2, 10, 4)
    println(four.reduce { sum, element -> sum + element })
    println(four.fold(0) { sum, element -> sum + element * 2 })
    println(four.reduce { sum, element -> sum + element * 2 })

    println("== foldRight ==")
    println(four.foldRight(0) { element, sum -> sum + element * 2 })

    println("== indexed variants ==")
    println(four.foldIndexed(0) { idx, sum, element -> if (idx % 2 == 0) sum + element else sum })
    println(four.foldRightIndexed(0) { idx, element, sum -> if (idx % 2 == 0) sum + element else sum })

    println("== reduce on empty throws, reduceOrNull does not ==")
    println(runCatching { empty.reduce { a, b -> a + b } }.exceptionOrNull()?.let { it::class.simpleName })
    println(empty.reduceOrNull { a, b -> a + b })
    println(empty.fold(0) { a, b -> a + b })

    println("== runningFold / runningReduce ==")
    val sequence = listOf(0, 1, 2, 3, 4, 5)
    println(sequence.runningReduce { sum, item -> sum + item })
    println(sequence.runningFold(10) { sum, item -> sum + item })
}
