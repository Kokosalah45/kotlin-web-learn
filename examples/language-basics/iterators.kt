// Examples for /language-basics/collections/iterators
// Adapted from https://kotlinlang.org/docs/iterators.html
package examples.languagebasics

fun main() {
    println("== explicit iterator ==")
    val numbers = listOf("one", "two", "three", "four")
    val numbersIterator = numbers.iterator()
    while (numbersIterator.hasNext()) {
        println(numbersIterator.next())
    }

    println("== an exhausted iterator cannot be reused ==")
    println(numbersIterator.hasNext())
    println(runCatching { numbersIterator.next() }.exceptionOrNull()?.let { it::class.simpleName })

    println("== for loop obtains one implicitly ==")
    for (item in numbers) println(item)

    println("== forEach ==")
    numbers.forEach { println(it) }

    println("== ListIterator goes both ways ==")
    val listIterator = numbers.listIterator()
    while (listIterator.hasNext()) listIterator.next()
    println("Iterating backwards:")
    while (listIterator.hasPrevious()) {
        print("Index: ${listIterator.previousIndex()}")
        println(", value: ${listIterator.previous()}")
    }

    println("== MutableIterator.remove ==")
    val mutable = mutableListOf("one", "two", "three", "four")
    val mutableIterator = mutable.iterator()
    mutableIterator.next()
    mutableIterator.remove()
    println("After removal: $mutable")

    println("== MutableListIterator add and set ==")
    val editing = mutableListOf("one", "four", "four")
    val mutableListIterator = editing.listIterator()
    mutableListIterator.next()
    mutableListIterator.add("two")
    println(editing)
    mutableListIterator.next()
    mutableListIterator.set("three")
    println(editing)

    println("== mutating during a for loop: throws ==")
    val throwing = mutableListOf(1, 2, 3, 4)
    val visitedThrowing = mutableListOf<Int>()
    println(
        runCatching {
            for (n in throwing) { visitedThrowing += n; if (n == 1) throwing.remove(n) }
        }.exceptionOrNull()?.let { it::class.simpleName }
    )
    println("visited: $visitedThrowing, list: $throwing")

    println("== mutating during a for loop: silently stops early ==")
    val silent = mutableListOf(1, 2, 3)
    val visitedSilent = mutableListOf<Int>()
    println(
        runCatching {
            for (n in silent) { visitedSilent += n; if (n == 2) silent.remove(n) }
        }.exceptionOrNull()?.let { it::class.simpleName }
    )
    println("visited: $visitedSilent, list: $silent")
}
