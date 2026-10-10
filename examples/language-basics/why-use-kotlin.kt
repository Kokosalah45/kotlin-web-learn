// Examples for /language-basics/introduction-to-kotlin/why-use-kotlin
// Adapted from https://kotlinlang.org/docs/comparison-to-java.html
// and https://kotlinlang.org/docs/faq.html
package examples.languagebasics

// What Java needs a constructor, getters, equals, hashCode and toString for.
data class Account(val id: String, val balance: Double)

// A sealed hierarchy plus an exhaustive `when` replaces the visitor pattern.
sealed interface Shipment {
    data class Pending(val orderId: String) : Shipment
    data class Shipped(val orderId: String, val tracking: String) : Shipment
    data object Cancelled : Shipment
}

fun describe(shipment: Shipment): String = when (shipment) {
    is Shipment.Pending -> "order ${shipment.orderId} is waiting"
    is Shipment.Shipped -> "order ${shipment.orderId} shipped as ${shipment.tracking}"
    Shipment.Cancelled -> "cancelled"
}

fun main() {
    println("== no checked exceptions ==")
    println("Kotlin has none: nothing in a signature declares what it throws")

    println("== null is part of the type ==")
    val present: String? = "here"
    val absent: String? = null
    println(present?.length)
    println(absent?.length)
    println(absent?.length ?: 0)
    println(runCatching { absent!!.length }.exceptionOrNull()?.let { it::class.simpleName })

    println("== data classes ==")
    val a = Account("A-1", 10.0)
    val b = Account("A-1", 10.0)
    println(a)
    println(a == b)
    println(a.copy(balance = 20.0))

    println("== no raw types, and arrays are invariant ==")
    val strings: List<String> = listOf("a")
    val anything: List<Any> = strings
    println(anything)
    println("Array<String> is NOT an Array<Any>, which List<String> -> List<Any> is")

    println("== expressions instead of statements ==")
    val score = 72
    val grade = when {
        score >= 90 -> "A"
        score >= 70 -> "B"
        else -> "C"
    }
    println(grade)

    println("== sealed types make the compiler check the cases ==")
    listOf(
        Shipment.Pending("1"),
        Shipment.Shipped("2", "TRK-9"),
        Shipment.Cancelled
    ).forEach { println(describe(it)) }

    println("== extension functions on types you do not own ==")
    fun String.isPalindrome(): Boolean {
        val cleaned = lowercase().filter { it.isLetterOrDigit() }
        return cleaned == cleaned.reversed()
    }
    println("A man, a plan, a canal: Panama".isPalindrome())
    println("kotlin".isPalindrome())

    println("== operator overloading, but only on a fixed set of names ==")
    println(listOf(1, 2) + listOf(3))
    println(mapOf("a" to 1) + mapOf("b" to 2))

    println("== what Kotlin deliberately does NOT have ==")
    println(
        listOf(
            "checked exceptions",
            "primitive types that are not classes",
            "static members (use companion objects or top-level declarations)",
            "wildcard types (use declaration-site variance and type projections)",
            "ternary operator a ? b : c (if is an expression)"
        ).joinToString("\n")
    )

    println("== and what Java has that Kotlin adds ==")
    println(
        listOf(
            "expression bodies, string templates, data and sealed classes",
            "null safety, smart casts, extension functions",
            "operator overloading, inline functions, coroutines"
        ).joinToString("\n")
    )
}
