// Examples for /functions/standard-functions/parameters
// Adapted from https://kotlinlang.org/docs/functions.html#parameters
package examples.functions

class Counter(var value: Int) {
    override fun toString() = "Counter(value=$value)"
}

// The reference is copied, so the function cannot rebind `counter`,
// but it can change the object the reference points at.
fun reset(counter: Counter) {
    counter.value = 0
    // counter = Counter(0) // Error: 'val' cannot be reassigned
}

fun readBytes(
    b: ByteArray,
    off: Int = 0,
    len: Int = b.size, // default computed from an earlier parameter
) {
    println("off=$off len=$len")
}

// The default value is an expression with a side effect.
fun traced(
    b: Int,
    print: Unit? = println("No argument passed for 'print'"),
) {
    println(b)
}

// A mutable default: is the list created once, or once per call?
fun collect(value: Int, into: MutableList<Int> = mutableListOf()): MutableList<Int> {
    into.add(value)
    return into
}

fun greeting(
    userId: Int = 0,
    message: String,
) {
    println("$userId: $message")
}

fun reformat(
    str: String,
    normalizeCase: Boolean = true,
    upperCaseFirstLetter: Boolean = true,
    divideByCamelHumps: Boolean = false,
    wordSeparator: Char = ' ',
) {
    println("$str | $normalizeCase $upperCaseFirstLetter $divideByCamelHumps '$wordSeparator'")
}

fun log(
    level: Int = 0,
    code: Int = 1,
    action: () -> Unit,
) {
    println("level=$level code=$code")
    action()
}

open class Shape {
    open fun draw(width: Int = 10, height: Int = 5) {
        println("drawing ${width}x$height")
    }
}

class Rectangle : Shape() {
    // Defaults may not be repeated here; the base values are used.
    override fun draw(width: Int, height: Int) {
        println("rectangle ${width}x$height")
    }
}

fun main() {
    println("== parameters are read-only, objects are not ==")
    val counter = Counter(7)
    println(counter)
    reset(counter)
    println(counter)

    println("== defaults computed from other parameters ==")
    readBytes(ByteArray(4))
    readBytes(ByteArray(4), 2)
    readBytes(ByteArray(4), 1, 3)

    println("== defaults are evaluated lazily ==")
    traced(1)
    traced(1, null)

    println("== a mutable default ==")
    println(collect(1))
    println(collect(2))
    println(collect(3))

    println("== default before required needs a named argument ==")
    greeting(message = "Hello!")

    println("== named arguments ==")
    reformat("This is a long String!")
    reformat("This is a short String!", upperCaseFirstLetter = false, wordSeparator = '_')
    reformat("String!", normalizeCase = false, upperCaseFirstLetter = false, divideByCamelHumps = true, '_')

    println("== trailing lambda skips the defaults ==")
    log(1) { println("Connection established") }
    log(action = { println("Connection established") })
    log { println("Connection established") }

    println("== overrides inherit the base defaults ==")
    val shape: Shape = Rectangle()
    shape.draw()
    Shape().draw()
}
