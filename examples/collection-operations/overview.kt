// Examples for /collection-operations/overview
// Adapted from https://kotlinlang.org/docs/collection-operations.html
package examples.collectionoperations

fun main() {
    println("== results are lost unless you keep them ==")
    val numbers = listOf("one", "two", "three", "four")
    numbers.filter { it.length > 3 }
    println("numbers are still $numbers")
    val longerThan3 = numbers.filter { it.length > 3 }
    println("numbers longer than 3 chars are $longerThan3")

    println("== destination (*To) functions ==")
    val filterResults = mutableListOf<String>()
    numbers.filterTo(filterResults) { it.length > 3 }
    numbers.filterIndexedTo(filterResults) { index, _ -> index == 0 }
    println(filterResults)

    println("== a destination can be created inline ==")
    val result = numbers.mapTo(HashSet()) { it.length }
    println("distinct item lengths are $result")

    println("== in-place vs copying ==")
    val mutable = mutableListOf("one", "two", "three", "four")
    val sortedNumbers = mutable.sorted()
    println(mutable == sortedNumbers)
    mutable.sort()
    println(mutable == sortedNumbers)
}
