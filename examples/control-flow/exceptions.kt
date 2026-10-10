// Examples for /control-flow/exceptions
// Adapted from https://kotlinlang.org/docs/exceptions.html
package examples.controlflow

class MyException : Exception("My message")

class NumberTooLargeException : ArithmeticException("My message")

open class MyCustomException(message: String) : Exception(message)
class SpecificCustomException : MyCustomException("Specific error message")

// A sealed base makes the hierarchy exhaustive for `when`.
sealed class AccountException(message: String, cause: Throwable? = null) : Exception(message, cause)

class InvalidAccountCredentialsException : AccountException("Invalid account credentials detected")

class APIKeyExpiredException(message: String = "API key expired", cause: Throwable? = null) :
    AccountException(message, cause)

fun validateAccount(credentialsValid: Boolean, apiKeyExpired: Boolean) {
    if (!credentialsValid) throw InvalidAccountCredentialsException()
    if (apiKeyExpired) {
        val cause = RuntimeException("API key validation failed due to network error")
        throw APIKeyExpiredException(cause = cause)
    }
}

class NamedPerson(val name: String?)

fun fail(message: String): Nothing {
    throw IllegalArgumentException(message)
}

fun notImplementedFunction(): Int {
    TODO("This function is not yet implemented")
}

fun kindOf(t: Throwable): String = when (t) {
    is Error -> "Error (not meant to be handled)"
    is RuntimeException -> "RuntimeException (a programming mistake)"
    is Exception -> "Exception (a condition you may want to handle)"
    else -> "Throwable"
}

fun main() {
    println("== every exception is unchecked ==")
    // Nothing in the signature of this function declares that it throws.
    fun parse(text: String): Int = text.toInt()
    println(parse("42"))
    println(runCatching { parse("x") }.exceptionOrNull()?.let { it::class.simpleName })

    println("== the hierarchy ==")
    for (t in listOf(
        StackOverflowError(),
        OutOfMemoryError(),
        IllegalArgumentException(),
        NumberFormatException(),
        java.io.IOException(),
        MyException()
    )) {
        println("${t::class.simpleName}: ${kindOf(t)}")
    }

    println("== who is a subclass of whom ==")
    val throwables: List<Throwable> = listOf(
        IllegalStateException(),
        StackOverflowError(),
        NumberFormatException()
    )
    for (t in throwables) {
        println(
            "${t::class.simpleName}: Throwable=${t is Throwable} Exception=${t is Exception} " +
                "RuntimeException=${t is RuntimeException} Error=${t is Error}"
        )
    }
    println(NumberFormatException() is IllegalArgumentException)

    println("== custom exceptions ==")
    println(runCatching { throw MyException() }.exceptionOrNull()?.message)
    println(runCatching { throw NumberTooLargeException() }.exceptionOrNull()?.let {
        "${it::class.simpleName} is an ArithmeticException: ${it is ArithmeticException}"
    })
    println(runCatching { throw SpecificCustomException() }.exceptionOrNull()?.let {
        "${it.message} / is MyCustomException: ${it is MyCustomException}"
    })

    println("== a cause chain ==")
    try {
        validateAccount(credentialsValid = true, apiKeyExpired = true)
        println("Operation successful: Account credentials and API key are valid.")
    } catch (e: AccountException) {
        println("Error: ${e.message}")
        e.cause?.let { println("Caused by: ${it.message}") }
    }

    println("== the Nothing type ==")
    val person = NamedPerson(name = null)
    println(runCatching { person.name ?: fail("Name required") }.exceptionOrNull()?.message)
    val named = NamedPerson(name = "Kodee")
    val s: String = named.name ?: fail("Name required")
    println(s)
    println(runCatching { notImplementedFunction() }.exceptionOrNull()?.let {
        "${it::class.simpleName}: ${it.message}"
    })

    println("== common exceptions, and the safe alternative ==")
    println(runCatching { 2 / 0 }.exceptionOrNull()?.let { "${it::class.simpleName}: ${it.message}" })
    println(runCatching { mutableListOf(1, 2, 3).removeAt(3) }.exceptionOrNull()?.let { it::class.simpleName })
    println(listOf(1, 2, 3).getOrNull(3))
    println(runCatching { listOf<Int>().first() }.exceptionOrNull()?.let { it::class.simpleName })
    println(listOf<Int>().firstOrNull())
    println(runCatching { "This is not a number".toInt() }.exceptionOrNull()?.let { it::class.simpleName })
    println("not a number".toIntOrNull())
    val text: String? = null
    println(runCatching { text!!.length }.exceptionOrNull()?.let { it::class.simpleName })

    println("== integer vs floating-point division by zero ==")
    println(runCatching { 2 / 0 }.exceptionOrNull()?.let { it::class.simpleName })
    println(2.0 / 0.0)
    println(-2.0 / 0.0)
    println(0.0 / 0.0)

    println("== a stack trace, trimmed ==")
    val thrown = runCatching { fail("This is an arithmetic exception!") }.exceptionOrNull()!!
    println(thrown.toString())
    println(thrown.stackTrace.first().let { "${it.className}.${it.methodName}" })
}
