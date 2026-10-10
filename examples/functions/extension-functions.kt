// Examples for /functions/extension-functions
// Adapted from https://kotlinlang.org/docs/extensions.html
package examples.functions

fun String.truncate(maxLength: Int): String {
    return if (this.length <= maxLength) this else take(maxLength - 3) + "..."
}

interface SiteUser {
    val name: String
    val email: String
}

fun SiteUser.displayInfo(): String = "User(name=$name, email=$email)"

class RegularUser(override val name: String, override val email: String) : SiteUser

fun Map<String, Int>.mostVoted(): String? = maxByOrNull { (_, value) -> value }?.key

fun <T> List<T>.endpoints(): Pair<T, T> = first() to last()

// A nullable receiver: `this` may be null inside the body.
fun Any?.describeOrNull(): String {
    if (this == null) return "null"
    return "${this::class.simpleName}($this)"
}

open class Shape2
class Rectangle2 : Shape2()

fun Shape2.getName() = "Shape2"
fun Rectangle2.getName() = "Rectangle2"

fun printShapeName(shape: Shape2) {
    println(shape.getName())
}

data class Resident(val firstName: String, val lastName: String)

val Resident.emailUsername: String
    get() = "${firstName.lowercase()}.${lastName.lowercase()}"

data class House(val streetName: String)

val houseNumbers = mutableMapOf<House, Int>()
var House.number: Int
    get() = houseNumbers[this] ?: 1
    set(value) {
        println("Setting house number for ${this.streetName} to $value")
        houseNumbers[this] = value
    }

class Logger {
    companion object
}

fun Logger.Companion.logStartupMessage() {
    println("Application started.")
}

class Host(val hostname: String) {
    fun printHostname() { print(hostname) }
}

class Connection(val host: Host, val port: Int) {
    fun printPort() { print(port) }

    // Host is the extension receiver, Connection the dispatch receiver.
    fun Host.printConnectionString() {
        printHostname()
        print(":")
        printPort()
        println()
    }

    fun connect() {
        host.printConnectionString()
    }
}

private fun removeWhitespace(input: String): String = input.replace("\\s".toRegex(), "")

fun String.cleaned(): String = removeWhitespace(this)

fun main() {
    println("== a stdlib extension ==")
    val builder = StringBuilder()
        .appendLine("Hello")
        .appendLine()
        .appendLine("World")
    print(builder.toString())

    println("== your own ==")
    println("KotlinFan42".truncate(15))
    println("JetBrainsLoverForever".truncate(15))

    println("== on an interface ==")
    println(RegularUser("Alice", "alice@example.com").displayInfo())

    println("== on a generic type ==")
    println(mapOf("Cats" to 37, "Dogs" to 58, "Birds" to 22).mostVoted())
    println(emptyMap<String, Int>().mostVoted())
    println(listOf("Paris", "London", "Berlin", "Prague").endpoints())
    println(listOf(21.0, 19.5, 22.3).endpoints())

    println("== nullable receiver ==")
    val number: Int? = 42
    val nothing: Any? = null
    println(number.describeOrNull())
    println(nothing.describeOrNull())

    println("== extensions are dispatched statically ==")
    printShapeName(Rectangle2())
    println(Rectangle2().getName())
    val asShape: Shape2 = Rectangle2()
    println(asShape.getName())

    println("== extension properties ==")
    println(Resident("Mickey", "Mouse").emailUsername)
    val house = House("Maple Street")
    println("Default number: ${house.number} ${house.streetName}")
    house.number = 99
    println("Updated number: ${house.number} ${house.streetName}")

    println("== companion object extensions ==")
    Logger.logStartupMessage()

    println("== two implicit receivers ==")
    Connection(Host("kotl.in"), 443).connect()

    println("== visibility ==")
    val rawEmail = "  user @example. com  "
    println("Raw:     '$rawEmail'")
    println("Cleaned: '${rawEmail.cleaned()}'")
}
