// Examples for /language-basics/data-types
// Adapted from https://kotlinlang.org/docs/types-overview.html
package examples.languagebasics

interface Vehicle {
    val wheels: Int
}

class Car : Vehicle {
    override val wheels = 4
}

class Bike : Vehicle {
    override val wheels = 2
}

fun main() {
    println("== static type versus runtime class ==")
    val asVehicle: Vehicle = Car()
    println(asVehicle.wheels)
    println(asVehicle::class.simpleName)

    println("== Any is the root ==")
    val values: List<Any> = listOf(1, "two", 3.0, true, 'c', listOf(1), Car())
    println(values.map { it::class.simpleName })
    println(values.all { it is Any })

    println("== Any's three members ==")
    val car = Car()
    println(car.toString().substringBefore('@'))
    println(car == car)
    println(car.hashCode() == car.hashCode())

    println("== nullability is part of the type ==")
    val present: String = "here"
    val absent: String? = null
    println(present.length)
    println(absent?.length)
    println(absent is String)
    println(absent is String?)
    println(listOf<String?>("a", null).size)

    println("== Nothing has no values ==")
    val empty: List<Nothing> = emptyList()
    println(empty)
    println(empty.size)
    fun fail(): Nothing = throw IllegalStateException("no value")
    // `fail()` has type Nothing, so this still type-checks as String.
    val chosen: String = present ?: fail()
    println(chosen)
    println(runCatching { absent ?: fail() }.exceptionOrNull()?.message)
}
