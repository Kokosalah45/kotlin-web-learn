// Examples for /language-basics/introduction-to-kotlin
// Adapted from https://kotlinlang.org/docs/getting-started.html
package examples.languagebasics

data class Customer(val name: String, val email: String)

fun main() {
    println("== hello world ==")
    println("Hello, world!")

    println("== the compiler and runtime this page was built with ==")
    println("Kotlin stdlib: ${KotlinVersion.CURRENT}")
    println("JVM target: ${System.getProperty("java.specification.version")}")

    println("== a data class replaces a page of boilerplate ==")
    val a = Customer("Kodee", "kodee@example.com")
    val b = Customer("Kodee", "kodee@example.com")
    println(a)
    println(a == b)
    println(a.hashCode() == b.hashCode())
    println(a.copy(email = "new@example.com"))
    val (name, email) = a
    println("$name / $email")

    println("== null safety is in the type system ==")
    val present: String? = "here"
    val absent: String? = null
    println(present?.length)
    println(absent?.length)
    println(absent?.length ?: -1)

    println("== expressions, not statements ==")
    val n = 7
    val label = if (n % 2 == 0) "even" else "odd"
    println(label)
    println(when {
        n < 0 -> "negative"
        n == 0 -> "zero"
        else -> "positive"
    })

    println("== extension functions ==")
    fun String.initials() = split(" ").joinToString("") { it.take(1).uppercase() }
    println("kotlin programming language".initials())

    println("== collections in the standard library ==")
    val words = listOf("one", "two", "three", "four")
    println(words.filter { it.length > 3 }.map { it.uppercase() })
    println(words.groupBy { it.length })
    println(words.sumOf { it.length })

    println("== interop: this is running on the JVM ==")
    println(java.util.UUID.nameUUIDFromBytes("kotlin".toByteArray()))
    println(StringBuilder("kotlin").reverse().toString())
}
