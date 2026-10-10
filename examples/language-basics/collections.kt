// Examples for /language-basics/collections and its subtopics
// Adapted from https://kotlinlang.org/docs/collections-overview.html
package examples.languagebasics

data class Person(var name: String, var age: Int)

private fun printAll(strings: Collection<String>) {
    for (s in strings) print("$s ")
    println()
}

private fun List<String>.getShortWordsTo(shortWords: MutableList<String>, maxLength: Int) {
    this.filterTo(shortWords) { it.length <= maxLength }
    val articles = setOf("a", "A", "an", "An", "the", "The")
    shortWords -= articles
}

fun main() {
    println("== a val mutable collection still mutates ==")
    val numbers = mutableListOf("one", "two", "three", "four")
    numbers.add("five")
    println(numbers)

    println("== Collection as a common parameter type ==")
    printAll(listOf("one", "two", "one"))
    printAll(setOf("one", "two", "three"))

    println("== MutableCollection as a destination ==")
    val words = "A long time ago in a galaxy far far away".split(" ")
    val shortWords = mutableListOf<String>()
    words.getShortWordsTo(shortWords, 3)
    println(shortWords)

    println("== List: indices and duplicates ==")
    val listNumbers = listOf("one", "two", "three", "four")
    println("Number of elements: ${listNumbers.size}")
    println("Third element: ${listNumbers.get(2)}")
    println("Fourth element: ${listNumbers[3]}")
    println("Index of element \"two\" ${listNumbers.indexOf("two")}")
    println("lastIndex: ${listNumbers.lastIndex}")

    println("== list equality is structural and positional ==")
    val bob = Person("Bob", 31)
    val people = listOf(Person("Adam", 20), bob, bob)
    val people2 = listOf(Person("Adam", 20), Person("Bob", 31), bob)
    println(people == people2)
    bob.age = 32
    println(people == people2)

    println("== default implementations ==")
    println(mutableListOf(1)::class.java.simpleName)
    println(mutableSetOf(1)::class.java.simpleName)
    println(mutableMapOf(1 to 1)::class.java.simpleName)

    println("== Set: uniqueness, and order does not affect equality ==")
    val setNumbers = setOf(1, 2, 3, 4)
    println("Number of elements: ${setNumbers.size}")
    if (setNumbers.contains(1)) println("1 is in the set")
    val numbersBackwards = setOf(4, 3, 2, 1)
    println("The sets are equal: ${setNumbers == numbersBackwards}")

    println("== but iteration order is insertion order ==")
    println(setNumbers.first() == numbersBackwards.first())
    println(setNumbers.first() == numbersBackwards.last())

    println("== a Set holds at most one null ==")
    println(setOf(null, null, 1).size)

    println("== Map ==")
    val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 1)
    println("All keys: ${numbersMap.keys}")
    println("All values: ${numbersMap.values}")
    if ("key2" in numbersMap) println("Value by key \"key2\": ${numbersMap["key2"]}")
    if (1 in numbersMap.values) println("The value 1 is in the map")

    println("== map equality ignores entry order ==")
    val anotherMap = mapOf("key2" to 2, "key1" to 1, "key4" to 1, "key3" to 3)
    println("The maps are equal: ${numbersMap == anotherMap}")

    println("== ArrayDeque ==")
    val deque = ArrayDeque(listOf(1, 2, 3))
    deque.addFirst(0)
    deque.addLast(4)
    println(deque)
    println(deque.first())
    println(deque.last())
    deque.removeFirst()
    deque.removeLast()
    println(deque)

    println("== covariance: a List<Rectangle> is a List<Shape> ==")
    val rectangles: List<Rectangle> = listOf(Rectangle(), Rectangle())
    val shapes: List<Shape> = rectangles
    println(shapes.size)
}

open class Shape

class Rectangle : Shape()
