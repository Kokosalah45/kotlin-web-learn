// Examples for /collection-operations/grouping
// Adapted from https://kotlinlang.org/docs/collection-grouping.html
package examples.collectionoperations

fun main() {
    val numbers = listOf("one", "two", "three", "four", "five")

    println("== groupBy ==")
    val groupedByFirstLetter = numbers.groupBy { it.first().uppercase() }
    println(groupedByFirstLetter)

    println("== iterating the groups ==")
    for ((key, value) in groupedByFirstLetter) {
        println("Key: $key, Values: $value")
    }

    println("== groupBy with valueTransform ==")
    val groupedAndTransformed =
        numbers.groupBy(keySelector = { it.first() }, valueTransform = { it.uppercase() })
    println(groupedAndTransformed)

    println("== groupingBy + eachCount ==")
    val grouped = numbers.groupingBy { it.first() }.eachCount()
    for ((key, count) in grouped) {
        println("Key: $key, Count: $count")
    }

    println("== groupingBy + fold ==")
    val totalLengths = numbers.groupingBy { it.first() }
        .fold(0) { acc, element -> acc + element.length }
    println(totalLengths)

    println("== groupBy keeps every element, associateBy does not ==")
    println(numbers.groupBy { it.first() })
    println(numbers.associateBy { it.first() })
}
