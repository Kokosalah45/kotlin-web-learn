// Examples for /collection-operations/collection-specific-operations/list-operations
// Adapted from https://kotlinlang.org/docs/list-operations.html
package examples.collectionoperations

import kotlin.math.sign

data class Product(val name: String, val price: Double)

private fun priceComparison(product: Product, price: Double) = sign(product.price - price).toInt()

fun main() {
    println("== index access ==")
    val numbers = listOf(1, 2, 3, 4)
    println(numbers.get(0))
    println(numbers[0])
    println(numbers.getOrNull(5))
    println(numbers.getOrElse(5) { it })
    println(runCatching { numbers[5] }.exceptionOrNull()?.let { it::class.simpleName })

    println("== subList is a view ==")
    val upTo13 = (0..13).toList()
    println(upTo13.subList(3, 6))
    val mutableSource = mutableListOf(0, 1, 2, 3, 4, 5)
    val view = mutableSource.subList(1, 4)
    println(view)
    mutableSource[2] = 99
    println("view after source change: $view")

    println("== indexOf / lastIndexOf ==")
    val dupes = listOf(1, 2, 3, 4, 2, 5)
    println(dupes.indexOf(2))
    println(dupes.lastIndexOf(2))
    println(dupes.indexOf(99))

    println("== indexOfFirst / indexOfLast ==")
    val four = mutableListOf(1, 2, 3, 4)
    println(four.indexOfFirst { it > 2 })
    println(four.indexOfLast { it % 2 == 1 })

    println("== binarySearch ==")
    val words = mutableListOf("one", "two", "three", "four")
    words.sort()
    println(words)
    println(words.binarySearch("two"))
    println(words.binarySearch("z"))
    println(words.binarySearch("two", 0, 2))

    println("== binarySearch on an unsorted list is undefined ==")
    println(listOf(5, 1, 4, 2, 3).binarySearch(4))

    println("== comparator binary search ==")
    val productList = listOf(
        Product("WebStorm", 49.0),
        Product("AppCode", 99.0),
        Product("DotTrace", 129.0),
        Product("ReSharper", 149.0)
    )
    println(productList.binarySearch(Product("AppCode", 99.0), compareBy<Product> { it.price }.thenBy { it.name }))

    val colors = listOf("Blue", "green", "ORANGE", "Red", "yellow")
    println(colors.binarySearch("RED", String.CASE_INSENSITIVE_ORDER))

    println("== comparison binary search ==")
    println(productList.binarySearch { priceComparison(it, 99.0) })

    println("== indexed add ==")
    val adding = mutableListOf("one", "five", "six")
    adding.add(1, "two")
    adding.addAll(2, listOf("three", "four"))
    println(adding)

    println("== set and fill ==")
    val updating = mutableListOf("one", "five", "three")
    updating[1] = "two"
    println(updating)
    val filling = mutableListOf(1, 2, 3, 4)
    filling.fill(3)
    println(filling)

    println("== removeAt ==")
    val removing = mutableListOf(1, 2, 3, 4, 3)
    removing.removeAt(1)
    println(removing)

    println("== in-place sorting ==")
    val sorting = mutableListOf("one", "two", "three", "four")
    sorting.sort()
    println("Sort into ascending: $sorting")
    sorting.sortDescending()
    println("Sort into descending: $sorting")
    sorting.sortBy { it.length }
    println("Sort into ascending by length: $sorting")
    sorting.sortByDescending { it.last() }
    println("Sort into descending by the last letter: $sorting")
    sorting.sortWith(compareBy<String> { it.length }.thenBy { it })
    println("Sort by Comparator: $sorting")
    sorting.reverse()
    println("Reverse: $sorting")
}
