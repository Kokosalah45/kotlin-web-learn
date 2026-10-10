// Examples for /collection-operations/write-operations
// Adapted from https://kotlinlang.org/docs/collection-write.html
package examples.collectionoperations

fun main() {
    println("== add ==")
    val numbers = mutableListOf(1, 2, 3, 4)
    numbers.add(5)
    println(numbers)

    println("== addAll ==")
    val more = mutableListOf(1, 2, 5, 6)
    more.addAll(arrayOf(7, 8))
    println(more)
    more.addAll(2, setOf(3, 4))
    println(more)

    println("== += ==")
    val words = mutableListOf("one", "two")
    words += "three"
    println(words)
    words += listOf("four", "five")
    println(words)

    println("== remove ==")
    val dupes = mutableListOf(1, 2, 3, 4, 3)
    dupes.remove(3)
    println(dupes)
    println(dupes.remove(5))
    println(dupes)

    println("== retainAll / clear / removeAll ==")
    val retained = mutableListOf(1, 2, 3, 4)
    println(retained)
    retained.retainAll { it >= 3 }
    println(retained)
    retained.clear()
    println(retained)

    val numbersSet = mutableSetOf("one", "two", "three", "four")
    numbersSet.removeAll(setOf("one", "two"))
    println(numbersSet)

    println("== -= single element vs collection ==")
    val minus = mutableListOf("one", "two", "three", "three", "four")
    minus -= "three"
    println(minus)
    minus -= listOf("four", "five")
    println(minus)

    println("== removeAll with a predicate ==")
    val predicate = mutableListOf(1, 2, 3, 4, 5, 6)
    predicate.removeAll { it % 2 == 0 }
    println(predicate)

    println("== a read-only reference sees mutations ==")
    val backing = mutableListOf("a", "b")
    val readOnlyView: List<String> = backing
    backing.add("c")
    println(readOnlyView)
}
