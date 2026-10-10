// Examples for /functions/member-functions
// Adapted from https://kotlinlang.org/docs/functions.html#member-functions
// and https://kotlinlang.org/docs/classes.html
package examples.functions

class Stream(private val bytes: ByteArray) {
    private var position = 0

    // A member function: declared inside the class body.
    fun read(): Int {
        if (position >= bytes.size) return -1
        return bytes[position++].toInt()
    }

    fun remaining() = bytes.size - position

    // Members see each other and the instance's state without a qualifier,
    // but `this` is always available.
    fun summary() = "read ${this.position} of ${bytes.size}, ${remaining()} left"
}

open class Greeter(val name: String) {
    open fun greet() = "Hello, $name"
    fun greetTwice() = "${greet()} / ${greet()}"
}

class LoudGreeter(name: String) : Greeter(name) {
    override fun greet() = "HELLO, ${name.uppercase()}"
}

class Account private constructor(val id: String) {
    fun describe() = "Account($id)"

    companion object {
        var created = 0
            private set

        fun open(id: String): Account {
            created++
            return Account(id)
        }
    }
}

object Registry {
    private val names = mutableListOf<String>()
    fun register(name: String) = names.add(name)
    fun all(): List<String> = names
}

class Conflict {
    fun describe() = "member function"
}

fun Conflict.describe() = "extension function"
fun Conflict.describe(index: Int) = "extension function #$index"

fun main() {
    println("== calling member functions ==")
    val stream = Stream(byteArrayOf(10, 20, 30))
    println(stream.read())
    println(stream.read())
    println(stream.summary())
    println(stream.read())
    println(stream.read())

    println("== dispatch is virtual ==")
    val greeters: List<Greeter> = listOf(Greeter("Kodee"), LoudGreeter("Kodee"))
    for (g in greeters) println(g.greet())
    for (g in greeters) println(g.greetTwice())

    println("== companion object ==")
    val a = Account.open("A-1")
    val b = Account.open("B-2")
    println(a.describe())
    println(b.describe())
    println(Account.created)

    println("== object declaration ==")
    Registry.register("first")
    Registry.register("second")
    println(Registry.all())

    println("== members win over extensions ==")
    println(Conflict().describe())
    println(Conflict().describe(1))
}
