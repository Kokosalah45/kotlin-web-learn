// Examples for /language-basics/main-function
// Adapted from https://kotlinlang.org/docs/basic-syntax.html#program-entry-point
package examples.languagebasics

// The JVM entry point this file is run with. The other `main` shapes below are
// renamed so they can live in the same file and be called directly.
fun main() {
    println("== the simplest program ==")
    helloWorld()

    println("== main with arguments ==")
    mainWithArgs(arrayOf())
    mainWithArgs(arrayOf("one", "two"))

    println("== the facade class this file compiles to ==")
    println(object {}.javaClass.enclosingClass?.name)

    println("== main is an ordinary function ==")
    println(::helloWorld.name)
    val entry: () -> Unit = ::helloWorld
    entry()

    println("== exit codes ==")
    println("a normal return from main exits with 0")
    println("exitProcess(n) would exit immediately with n, skipping the rest of main")

    println("== what a suspend main would do ==")
    println(describeSuspendMain())
}

fun helloWorld() {
    println("Hello world!")
}

fun mainWithArgs(args: Array<String>) {
    println(args.contentToString())
    println("count: ${args.size}")
    if (args.isNotEmpty()) println("first: ${args.first()}")
}

fun describeSuspendMain(): String =
    "suspend fun main() { } is allowed: the compiler wraps the body in runBlocking"
