// Examples for /control-flow/exceptions/catching-exceptions
// Adapted from https://kotlinlang.org/docs/exceptions.html#handle-exceptions-using-try-catch-blocks
package examples.controlflow

open class WithdrawalException(message: String) : Exception(message)
class InsufficientFundsException(message: String) : WithdrawalException(message)

fun processWithdrawal(amount: Double, availableFunds: Double) {
    if (amount > availableFunds) {
        throw InsufficientFundsException("Insufficient funds for the withdrawal.")
    }
    if (amount < 1 || amount % 1 != 0.0) {
        throw WithdrawalException("Invalid withdrawal amount.")
    }
    println("Withdrawal processed")
}

fun countWith(a: Int): Int = 10 / a

fun divideOrNull(a: Int): Int {
    try {
        val b = 44 / a
        println("try block: Executing division: $b")
        return b
    } catch (e: ArithmeticException) {
        println("catch block: Encountered ArithmeticException $e")
        return -1
    } finally {
        println("finally block: The finally block is always executed")
    }
}

// What happens when `finally` itself returns?
@Suppress("ControlFlowWithEmptyBody")
fun finallyWins(): String {
    try {
        return "from try"
    } finally {
        return "from finally"
    }
}

class MockResource {
    fun use() {
        println("Resource being used")
        val result = 100 / 0
        println("Result: $result")
    }

    fun close() {
        println("Resource closed")
    }
}

class Loud(val name: String) : AutoCloseable {
    fun work() = println("$name working")
    override fun close() = println("$name closed")
}

fun main() {
    println("== try-catch as an expression ==")
    val num: Int = try {
        countWith(0)
    } catch (e: ArithmeticException) {
        -1
    }
    println("Result: $num")
    println(
        try {
            countWith(5)
        } catch (e: ArithmeticException) {
            -1
        }
    )

    println("== ignoring the exception instance ==")
    val userSettings = try {
        java.io.File("user-settings-that-does-not-exist.json").readText()
    } catch (_: java.io.IOException) {
        "{}"
    }
    println(userSettings)

    println("== several catch blocks, most specific first ==")
    for (amount in listOf(500.5, 600.0, 400.0)) {
        try {
            processWithdrawal(amount, 500.0)
        } catch (e: InsufficientFundsException) {
            println("Caught an InsufficientFundsException: ${e.message}")
        } catch (e: WithdrawalException) {
            println("Caught a WithdrawalException: ${e.message}")
        }
    }

    println("== a general catch swallows the specific one ==")
    try {
        processWithdrawal(600.0, 500.0)
    } catch (e: WithdrawalException) {
        println("Caught as WithdrawalException, but it really is ${e::class.simpleName}")
    }

    println("== finally always runs ==")
    divideOrNull(0)
    divideOrNull(4)

    println("== a return in finally overrides the one in try ==")
    println(finallyWins())

    println("== try with finally and no catch ==")
    val resource = MockResource()
    println(runCatching {
        try {
            resource.use()
        } finally {
            resource.close()
        }
    }.exceptionOrNull()?.let { it::class.simpleName })
    println("the exception still propagated")

    println("== use() instead of finally ==")
    Loud("writer").use { it.work() }
    println(runCatching {
        Loud("failing").use {
            it.work()
            throw IllegalStateException("boom")
        }
    }.exceptionOrNull()?.message)

    println("== runCatching ==")
    println(runCatching { countWith(0) }.getOrNull())
    println(runCatching { countWith(0) }.getOrDefault(-1))
    println(runCatching { countWith(0) }.exceptionOrNull()?.let { it::class.simpleName })
    println(runCatching { countWith(5) }.map { it * 100 }.getOrNull())

    println("== runCatching catches Throwable, not just Exception ==")
    fun blowTheStack(n: Int): Int = blowTheStack(n + 1)
    println(runCatching { blowTheStack(0) }.exceptionOrNull()?.let { it::class.simpleName })

    println("== catching Exception would not have ==")
    println(
        try {
            blowTheStack(0)
        } catch (e: Exception) {
            "caught as Exception"
        } catch (e: StackOverflowError) {
            "caught as StackOverflowError"
        }
    )

    println("== the suppressed exception from use() ==")
    class FailingOnClose : AutoCloseable {
        override fun close() = throw IllegalStateException("close failed")
    }
    val thrown = runCatching {
        FailingOnClose().use { throw IllegalArgumentException("body failed") }
    }.exceptionOrNull()!!
    println("thrown: ${thrown::class.simpleName}: ${thrown.message}")
    println("suppressed: ${thrown.suppressed.joinToString { "${it::class.simpleName}: ${it.message}" }}")
}
