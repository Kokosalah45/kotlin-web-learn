// Examples for /language-basics/comments
// Adapted from https://kotlinlang.org/docs/basic-syntax.html#comments
// and https://kotlinlang.org/docs/kotlin-doc.html
package examples.languagebasics

// This is an end-of-line comment

/* This is a block comment
   on multiple lines. */

/* The comment starts here
/* contains a nested comment */
and ends here. */

/**
 * A group of *members*.
 *
 * This class has no useful logic; it's just a documentation example.
 *
 * @param T the type of a member in this group.
 * @property name the name of this group.
 * @constructor Creates an empty group.
 */
class Group<T>(val name: String) {
    private val members = mutableListOf<T>()

    /**
     * Adds a [member] to this group.
     * @return the new size of the group.
     */
    fun add(member: T): Int {
        members.add(member)
        return members.size
    }

    /** The number of members, as a one-line KDoc comment. */
    val size: Int get() = members.size
}

/**
 * Divides [a] by [b].
 *
 * @throws IllegalArgumentException if [b] is zero.
 * @see Group
 * @since 1.0
 */
fun divide(a: Int, b: Int): Int {
    require(b != 0) { "b must not be zero" }
    return a / b
}

@Suppress("unused")
private fun documentedButUnused() = Unit

fun main() {
    println("== comments are not output ==")
    // println("this line never runs")
    /* neither does this
    println("nor this")
    */
    println("only this ran")

    println("== the nested block comment above compiled ==")
    println("a nested /* ... */ inside a block comment is legal in Kotlin")

    println("== the documented class works like any other ==")
    val group = Group<String>("admins")
    println(group.add("sarah"))
    println(group.add("mike"))
    println(group.size)
    println(group.name)

    println("== KDoc does not change behaviour ==")
    println(divide(10, 2))
    println(runCatching { divide(10, 0) }.exceptionOrNull()?.message)

    println("== a trailing comment on a line of code ==")
    val answer = 42 // the answer
    println(answer)

    println("== a comment inside a string is just text ==")
    println("not a comment: // or /* */")
    println("not a comment: // or /* */".length)
}
