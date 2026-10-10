// Examples for /functions/standard-functions/varargs
// Adapted from https://kotlinlang.org/docs/functions.html#variable-number-of-arguments-varargs
package examples.functions

fun <T> asList(vararg ts: T): List<T> {
    val result = ArrayList<T>()
    for (t in ts) // ts is an Array<out T>
        result.add(t)
    return result
}

fun mergeStrings(vararg strings: String): String = strings.joinToString("-")

// A vararg that is not last: everything after it must be named at the call site.
fun tag(vararg values: Int, label: String): String = "$label${values.toList()}"

fun describeVararg(vararg ts: Int) {
    println("${ts::class.simpleName} of size ${ts.size}")
}

// Does the spread operator hand the function the caller's array, or a copy?
fun clobber(vararg ts: Int) {
    ts[0] = -999
}

fun main() {
    println("== basics ==")
    println(asList(1, 2, 3))
    println(asList<String>())
    println(mergeStrings("a", "b", "c"))
    println(mergeStrings(strings = arrayOf("a", "b", "c")))

    println("== vararg before another parameter ==")
    println(tag(1, 2, 3, label = "ids="))

    println("== the spread operator ==")
    val a = arrayOf(1, 2, 3)
    println(asList(-1, 0, *a, 4))
    val primitives = intArrayOf(1, 2, 3)
    println(asList(-1, 0, *primitives.toTypedArray(), 4))

    println("== what the parameter actually is ==")
    describeVararg(1, 2, 3)
    describeVararg(*primitives)

    println("== does the spread copy? ==")
    val mine = intArrayOf(1, 2, 3)
    clobber(*mine)
    println(mine.toList())
}
