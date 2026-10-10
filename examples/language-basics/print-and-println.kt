// Examples for /language-basics/printing-data/print-and-println
// Adapted from https://kotlinlang.org/docs/basic-syntax.html#print-to-the-standard-output
// and https://kotlinlang.org/docs/read-standard-input.html
package examples.languagebasics

fun main() {
    println("== print leaves the cursor where it is ==")
    print("Hello ")
    print("world!")
    println()

    println("== println adds the line break ==")
    println("Hello world!")
    println(42)

    println("== building a line piece by piece ==")
    for (n in 1..5) print("$n ")
    println()
    for (n in 1..5) print(if (n == 5) "$n\n" else "$n, ")

    println("== println() with no argument prints an empty line ==")
    println("before")
    println()
    println("after")

    println("== both accept any type, via toString() ==")
    println(1)
    println(1.5)
    println('c')
    println(true)
    println(listOf(1, 2))
    println(null)

    println("== a nullable prints the text null ==")
    val absent: String? = null
    println(absent)
    println("value: $absent")
    println("value: ${absent ?: "unknown"}")

    println("== an array needs help ==")
    val numbers = arrayOf(1, 2, 3)
    println(numbers.toString().substringBefore('@'))
    println(numbers.contentToString())
    println(numbers.joinToString())

    println("== print is not println with a newline argument ==")
    print("a\n")
    println("b")

    println("== they write to System.out ==")
    println(System.out === System.out)
    System.out.print("via System.out")
    System.out.println()

    println("== stderr is a different stream ==")
    System.err.println("this line goes to stderr, not stdout")
    println("back on stdout")

    println("== flushing ==")
    print("println flushes; print does not guarantee it")
    println()

    println("== reading a line ==")
    println("readln(): reads one line, throws RuntimeException at end of input")
    println("readlnOrNull(): returns null at end of input")
    println("readln().toInt(): throws NumberFormatException on bad input")
    println("readln().toIntOrNull(): returns null on bad input")
    println("readln().split(' ').map { it.toInt() }: several values on one line")
    println("generateSequence(::readlnOrNull).joinToString(\"\\n\"): all remaining input")
}
