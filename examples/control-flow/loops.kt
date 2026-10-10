// Examples for /control-flow/loops
// Adapted from https://kotlinlang.org/docs/control-flow.html#for-loops
package examples.controlflow

class Booklet(val totalPages: Int) : Iterable<Int> {
    override fun iterator(): Iterator<Int> {
        return object : Iterator<Int> {
            var current = 1
            override fun hasNext() = current <= totalPages
            override fun next() = current++
        }
    }
}

// An `operator fun iterator()` is enough; the type need not be Iterable.
class Countdown(val from: Int) {
    operator fun iterator(): Iterator<Int> = object : Iterator<Int> {
        var current = from
        override fun hasNext() = current >= 0
        override fun next() = current--
    }
}

// An extension `iterator()` works too, so a type you cannot change becomes iterable.
class Page(val number: Int)
class Chapter(val pages: List<Page>)

operator fun Chapter.iterator(): Iterator<Page> = pages.iterator()

fun main() {
    println("== for over a collection ==")
    val shoppingList = listOf("Milk", "Bananas", "Bread")
    println("Things to buy:")
    for (item in shoppingList) {
        println("- $item")
    }

    println("== for over a range ==")
    for (i in 1..6) print(i)
    println()
    for (i in 1..<6) print(i)
    println()
    for (i in 6 downTo 0 step 2) print(i)
    println()

    println("== while and do-while ==")
    var carsInGarage = 0
    val maxCapacity = 3
    while (carsInGarage < maxCapacity) {
        println("Car entered. Cars now in garage: ${++carsInGarage}")
    }
    println("Garage is full!")

    var attempts = 0
    do {
        attempts++
        println("Attempt $attempts")
    } while (attempts < 1)

    println("== a custom Iterable ==")
    for (page in Booklet(3)) println("Reading page $page")

    println("== just an operator fun iterator() ==")
    for (n in Countdown(3)) print(n)
    println()

    println("== an extension iterator ==")
    for (page in Chapter(listOf(Page(1), Page(2)))) print("p${page.number} ")
    println()

    println("== break and continue ==")
    for (i in 1..10) {
        if (i % 2 == 0) continue
        if (i > 7) break
        print(i)
    }
    println()

    println("== loops are statements, not expressions ==")
    val firstLong = shoppingList.firstOrNull { it.length > 5 }
    println(firstLong)
}
