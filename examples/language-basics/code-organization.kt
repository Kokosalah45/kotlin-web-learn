// Examples for /language-basics/code-organization
// Adapted from https://kotlinlang.org/docs/packages.html
// and https://kotlinlang.org/docs/coding-conventions.html
package examples.languagebasics

import kotlin.math.abs
import kotlin.collections.List as KList

const val VERSION = "1.0"

fun printMessage() = println("a top-level function in examples.languagebasics")

class Message(val messageText: String)

object Config {
    const val DEFAULT_TIMEOUT = 30
    fun loadSettings() = "settings loaded"
}

class Factory private constructor(val label: String) {
    companion object {
        fun create(label: String) = Factory(label)
    }
}

enum class Colour { RED, GREEN, BLUE }

class Outer {
    class Nested(val n: Int)
}

private fun fileLocalHelper() = "only visible inside this file"

fun main() {
    println("== a package is part of the fully qualified name ==")
    println(::printMessage.name)
    println(Message::class.qualifiedName)
    println(Message::class.simpleName)
    println(Colour.RED::class.java.name)

    println("== top-level declarations need no class ==")
    printMessage()
    println(VERSION)
    println(fileLocalHelper())

    println("== what you can import ==")
    println(Config.DEFAULT_TIMEOUT)
    println(Config.loadSettings())
    println(Factory.create("f1").label)
    println(Colour.GREEN)
    println(Outer.Nested(7).n)

    println("== an import alias ==")
    val numbers: KList<Int> = listOf(1, 2, 3)
    println(numbers)
    println(numbers::class.simpleName)

    println("== kotlin.math is NOT a default import ==")
    println(abs(-1.5))

    println("== default imports really are available ==")
    println(listOf(1, 2).size)
    println("text".uppercase())
    println((1..3).sum())
    println(sequenceOf(1, 2).toList())
    println(compareValues(1, 2))

    println("== naming conventions ==")
    println(
        listOf(
            "packages: lower case, no underscores (org.example.project)",
            "classes and objects: UpperCamelCase",
            "functions, properties, locals: lowerCamelCase",
            "const and top-level immutables: SCREAMING_SNAKE_CASE",
            "backing properties: a leading underscore (_users)"
        ).joinToString("\n")
    )
}
