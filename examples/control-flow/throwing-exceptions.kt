// Examples for /control-flow/exceptions/throwing-exceptions
// Adapted from https://kotlinlang.org/docs/exceptions.html#throw-exceptions
package examples.controlflow

fun getIndices(count: Int): List<Int> {
    require(count >= 0) { "Count must be non-negative. You set count to $count." }
    return List(count) { it + 1 }
}

fun printNonNullLength(str: String?) {
    // Nullability check; after it, `str` is smart cast to a non-nullable String
    require(str != null)
    println(str.length)
}

class StateHolder {
    var someState: String? = null

    fun getStateValue(): String {
        val state = checkNotNull(someState) { "State must be set beforehand!" }
        check(state.isNotEmpty()) { "State must be non-empty!" }
        return state
    }
}

class ThrowUser(val name: String, val role: String)

fun processUserRole2(user: ThrowUser): String = when (user.role) {
    "admin" -> "${user.name} is an admin."
    "editor" -> "${user.name} is an editor."
    "viewer" -> "${user.name} is a viewer."
    else -> error("Undefined role: ${user.role}")
}

fun withdraw(amount: Double, availableFunds: Double) {
    if (amount > availableFunds) {
        val cause = IllegalStateException("Original cause: illegal state")
        throw IllegalArgumentException("Input must not exceed $availableFunds", cause)
    }
    println("Withdrawal processed")
}

// `throw` is an expression of type Nothing, so it fits anywhere a value does.
fun ageOf(input: String): Int = input.toIntOrNull() ?: throw NumberFormatException("not a number: $input")

fun describe(t: Throwable) = "${t::class.simpleName}: ${t.message}"

fun main() {
    println("== throw ==")
    println(runCatching { throw IllegalArgumentException() }.exceptionOrNull()?.let { describe(it) })
    println(runCatching { withdraw(600.0, 500.0) }.exceptionOrNull()?.let {
        "${describe(it)} | cause: ${it.cause?.message}"
    })
    withdraw(100.0, 500.0)

    println("== throw is an expression ==")
    println(ageOf("42"))
    println(runCatching { ageOf("forty-two") }.exceptionOrNull()?.let { describe(it) })

    println("== require ==")
    println(getIndices(3))
    println(runCatching { getIndices(-1) }.exceptionOrNull()?.let { describe(it) })
    printNonNullLength("Kodee")
    println(runCatching { printNonNullLength(null) }.exceptionOrNull()?.let { describe(it) })

    println("== check and checkNotNull ==")
    val holder = StateHolder()
    println(runCatching { holder.getStateValue() }.exceptionOrNull()?.let { describe(it) })
    holder.someState = ""
    println(runCatching { holder.getStateValue() }.exceptionOrNull()?.let { describe(it) })
    holder.someState = "non-empty-state"
    println(holder.getStateValue())

    println("== error ==")
    println(processUserRole2(ThrowUser("Alice", "admin")))
    println(runCatching { processUserRole2(ThrowUser("Bob", "guest")) }.exceptionOrNull()?.let { describe(it) })

    println("== which exception each precondition throws ==")
    println(runCatching { require(false) }.exceptionOrNull()?.let { it::class.simpleName })
    println(runCatching { check(false) }.exceptionOrNull()?.let { it::class.simpleName })
    println(runCatching { error("boom") }.exceptionOrNull()?.let { it::class.simpleName })
    println(runCatching { requireNotNull(null) }.exceptionOrNull()?.let { it::class.simpleName })
    println(runCatching { checkNotNull(null) }.exceptionOrNull()?.let { it::class.simpleName })
    println(runCatching { TODO() }.exceptionOrNull()?.let { it::class.simpleName })

    println("== the default messages ==")
    println(runCatching { require(false) }.exceptionOrNull()?.message)
    println(runCatching { check(false) }.exceptionOrNull()?.message)
    println(runCatching { requireNotNull(null) }.exceptionOrNull()?.message)

    println("== the message lambda is only called on failure ==")
    fun loudMessage(): String {
        println("  building the message")
        return "failed"
    }
    require(true) { loudMessage() }
    println(runCatching { require(false) { loudMessage() } }.exceptionOrNull()?.message)

    println("== requireNotNull returns the value ==")
    val maybe: String? = "present"
    val definitely: String = requireNotNull(maybe)
    println(definitely.length)
}
