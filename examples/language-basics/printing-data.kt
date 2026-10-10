// Examples for /language-basics/printing-data
// Adapted from https://kotlinlang.org/docs/basic-syntax.html#print-to-the-standard-output
package examples.languagebasics

data class Point(val x: Int, val y: Int)

class Plain(val x: Int)

fun main() {
    println("== print and println ==")
    print("Hello ")
    print("world!")
    println()
    println("Hello world!")
    println(42)

    println("== println with no argument ==")
    print("a")
    println()
    print("b")
    println()

    println("== what gets printed is toString() ==")
    println(Point(1, 2))
    println(Plain(1).toString().substringBefore('@'))
    println(listOf(1, 2, 3))
    println(mapOf("a" to 1))
    println(arrayOf(1, 2, 3).contentToString())
    println(null)
    val maybe: String? = null
    println(maybe)

    println("== string templates ==")
    val name = "Kotlin"
    println("Hello, $name")
    println("The length of $name is ${name.length}")
    println("${name}Lang")

    println("== every println takes exactly one argument ==")
    println("a" + "b" + "c")
    println("$name has ${name.length} characters")

    println("== stdout versus stderr ==")
    System.out.println("this goes to stdout")
    System.err.println("this goes to stderr")
    println("recorded output only contains stdout")

    println("== reading input ==")
    println("readln() reads a whole line and throws at end of input")
    println("readlnOrNull() returns null instead")
    println("readln().toIntOrNull() is the safe way to parse it")
}
