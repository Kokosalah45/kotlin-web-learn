// Examples for /control-flow/conditional-expressions/when
// Adapted from https://kotlinlang.org/docs/control-flow.html#when-expressions-and-statements
package examples.controlflow

sealed interface Animal {
    data class Cat(val mouseHunter: Boolean, val hungry: Boolean = true) : Animal
    data class Dog(val breed: String) : Animal
}

fun feedDog() = println("Feeding a dog")
fun feedCat() = println("Feeding a cat")

fun feedAnimal(animal: Animal) {
    when (animal) {
        // Branch with only a primary condition
        is Animal.Dog -> feedDog()
        // Branch with a primary condition and a guard
        is Animal.Cat if !animal.mouseHunter -> feedCat()
        else -> println("Unknown animal")
    }
}

fun hasPrefix(input: Any): Boolean = when (input) {
    is String -> input.startsWith("ID-")
    else -> false
}

fun Int.isOdd() = this % 2 != 0
fun Int.isEven() = this % 2 == 0

class RoleUser(val name: String, val role: String)

fun processUserRole(user: RoleUser) {
    when (user.role) {
        "admin" -> println("${user.name} is an admin.")
        "editor" -> println("${user.name} is an editor.")
        "viewer" -> println("${user.name} is a viewer.")
        else -> error("Undefined role: ${user.role}")
    }
}

open class Shape
class Circle : Shape()
class Rectangle : Shape()
class Triangle : Shape()

fun countCorners(shape: Shape) = when (shape) {
    is Circle -> 0
    is Rectangle -> 4
    is Triangle -> 3
    else -> -1
}

fun main() {
    println("== with a subject ==")
    val userRole = "Editor"
    when (userRole) {
        "Viewer" -> println("User has read-only access")
        "Editor" -> println("User can edit content")
        else -> println("User role is not recognized")
    }

    println("== without a subject ==")
    val localFileSize = 1200
    val remoteFileSize = 1200
    val message = when {
        localFileSize > remoteFileSize -> "Local file is larger than remote file"
        localFileSize < remoteFileSize -> "Local file is smaller than remote file"
        else -> "Local and remote files are the same size"
    }
    println(message)

    println("== grouping conditions with a comma ==")
    for (ticketPriority in listOf("Low", "Medium", "High")) {
        when (ticketPriority) {
            "Low", "Medium" -> println("$ticketPriority: standard response time")
            else -> println("$ticketPriority: high-priority handling")
        }
    }

    println("== arbitrary expressions as conditions ==")
    val storedPin = "1234"
    val enteredPin = 1234
    when (enteredPin) {
        storedPin.toInt() -> println("PIN is correct")
        else -> println("Incorrect PIN")
    }

    println("== in and !in ==")
    val x = 7
    val validNumbers = setOf(15, 16, 17)
    when (x) {
        in 1..10 -> println("x is in the range")
        in validNumbers -> println("x is valid")
        !in 10..20 -> println("x is outside the range")
        else -> println("none of the above")
    }

    println("== branches are tried in order ==")
    for (n in listOf(5, 16, 25, 15)) {
        val which = when (n) {
            in 1..10 -> "first branch (1..10)"
            in validNumbers -> "second branch (validNumbers)"
            !in 10..20 -> "third branch (!in 10..20)"
            else -> "else"
        }
        println("$n -> $which")
    }

    println("== is and !is, with a smart cast ==")
    println(hasPrefix("ID-98345"))
    println(hasPrefix(98345))
    val typed: Any = listOf(1, 2, 3)
    println(
        when (typed) {
            is String -> "a String of length ${typed.length}"
            is List<*> -> "a List of size ${typed.size}"
            else -> "something else"
        }
    )

    println("== replacing an if/else if chain ==")
    val a = 5
    val b = 8
    when {
        a.isOdd() -> println("a is odd")
        b.isEven() -> println("b is even")
        else -> println("a+b is odd")
    }

    println("== capturing the subject ==")
    val captured = when (val input = "maybe") {
        "yes" -> "You said yes"
        "no" -> "You said no"
        else -> "Unrecognized input: $input"
    }
    println(captured)

    println("== guard conditions ==")
    val animals = listOf(
        Animal.Dog("Beagle"),
        Animal.Cat(mouseHunter = false),
        Animal.Cat(mouseHunter = true)
    )
    animals.forEach { feedAnimal(it) }

    println("== a guard is only evaluated if the primary condition matched ==")
    fun loudGuard(value: Boolean): Boolean {
        println("  guard evaluated")
        return value
    }
    val subject: Animal = Animal.Dog("Collie")
    when (subject) {
        is Animal.Cat if loudGuard(true) -> println("cat")
        is Animal.Dog -> println("dog")
        else -> println("neither")
    }

    println("== else() with error() ==")
    processUserRole(RoleUser("Alice", "admin"))
    println(runCatching { processUserRole(RoleUser("Bob", "guest")) }.exceptionOrNull()?.let {
        "${it::class.simpleName}: ${it.message}"
    })

    println("== nullable subjects ==")
    val maybeText: String? = null
    println(
        when (maybeText) {
            null -> "it was null"
            "" -> "it was empty"
            else -> "it was '$maybeText'"
        }
    )
    val flag: Boolean? = true
    println(
        when (flag) {
            true -> "yes"
            false -> "no"
            null -> "unknown"
        }
    )

    println("== type switch ==")
    println(listOf(Circle(), Rectangle(), Triangle()).map { countCorners(it) })

    println("== a sealed or enum subject must be exhaustive even as a statement ==")
    val shape: Animal = Animal.Cat(mouseHunter = true)
    when (shape) {
        is Animal.Cat -> println("a cat")
        is Animal.Dog -> println("a dog")
    }

    println("== the subject is evaluated exactly once ==")
    fun noisySubject(): Int {
        println("  subject evaluated")
        return 3
    }
    when (noisySubject()) {
        1 -> println("one")
        2 -> println("two")
        3 -> println("three")
        else -> println("other")
    }
}
