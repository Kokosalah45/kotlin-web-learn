// Examples for /functions/function-types/higher-order-functions
// Adapted from https://kotlinlang.org/docs/lambdas.html#higher-order-functions
// and https://kotlinlang.org/docs/inline-functions.html
package examples.functions

// The stdlib's fold, written out: `combine` has the function type (R, T) -> R.
fun <T, R> Collection<T>.myFold(
    initial: R,
    combine: (acc: R, nextElement: T) -> R,
): R {
    var accumulator: R = initial
    for (element: T in this) {
        accumulator = combine(accumulator, element)
    }
    return accumulator
}

// Taking a function...
fun applyTwice(x: Int, f: (Int) -> Int): Int = f(f(x))

// ...and returning one.
fun multiplierOf(factor: Int): (Int) -> Int = { it * factor }

fun notInlined(block: () -> Unit) {
    block()
}

inline fun inlined(block: () -> Unit) {
    block()
}

// A bare `return` in a lambda passed to a non-inline function is forbidden;
// in a lambda passed to an inline function it leaves the *enclosing* function.
fun firstNegativeOrNull(ints: List<Int>): Int? {
    ints.forEach {
        if (it < 0) return it // non-local return, out of firstNegativeOrNull
    }
    return null
}

// `continue` inside a lambda works for the same reason.
fun sumNonNull(values: List<Int?>): Int {
    var total = 0
    for (value in values) {
        val n = value ?: run {
            println("  skipping null")
            continue
        }
        total += n
    }
    return total
}

inline fun <reified T> countOf(items: List<Any>): Int = items.count { it is T }

inline fun <reified T> typeNameOf(): String = T::class.simpleName ?: "?"

fun whereAmI(label: String) {
    // The top frame below `whereAmI` tells us whether the lambda became its own class.
    val frame = Thread.currentThread().stackTrace[2]
    println("$label -> ${frame.className.substringAfterLast('.')}.${frame.methodName}")
}


// --- noinline: an inlined lambda has no object, so it cannot be stored ---

// `storeOne` is public and inline, so everything it touches must be public API
// too. @PublishedApi is the documented escape hatch for an internal declaration.
@PublishedApi
internal var stored: (() -> Unit)? = null

// `inlined` is pasted into the call site; `notInlined` stays a real object we can keep.
inline fun storeOne(inlined: () -> Unit, noinline notInlined: () -> Unit) {
    inlined()
    stored = notInlined
}

// --- Inline properties: only legal on accessors with no backing field ---

class Config(@PublishedApi internal val raw: Map<String, String>) {
    // Whole property inlined: both accessors.
    inline val isEmpty: Boolean get() = size == 0

    // A single accessor inlined.
    val size: Int
        inline get() = raw.size
}

fun main() {
    println("== fold, written out ==")
    val items = listOf(1, 2, 3, 4, 5)
    val sum = items.myFold(0) { acc: Int, i: Int ->
        print("acc = $acc, i = $i, ")
        val result = acc + i
        println("result = $result")
        result
    }
    println(sum)

    println("== the lambda's parameter types are usually inferable ==")
    println(items.myFold("Elements:") { acc, i -> acc + " " + i })

    println("== a function reference works too ==")
    println(items.myFold(1, Int::times))

    println("== taking and returning functions ==")
    println(applyTwice(5) { it + 3 })
    val triple = multiplierOf(3)
    println(triple(7))
    println(multiplierOf(10)(7))
    println(items.map(multiplierOf(2)))

    println("== inline removes the lambda object ==")
    notInlined { whereAmI("non-inline") }
    inlined { whereAmI("inline") }

    println("== non-local return ==")
    println(firstNegativeOrNull(listOf(1, 2, -3, 4)))
    println(firstNegativeOrNull(listOf(1, 2, 3)))

    println("== non-local continue ==")
    println(sumNonNull(listOf(1, null, 2, null, 3)))

    println("== reified type parameters ==")
    val mixed = listOf(1, "two", 3.0, "four", 5)
    println(countOf<String>(mixed))
    println(countOf<Int>(mixed))
    println(typeNameOf<StringBuilder>())

    println("== noinline lets a lambda outlive the call ==")
    storeOne({ println("  inlined body runs immediately") }, { println("  stored body runs later") })
    println("  stored is null? ${stored == null}")
    stored?.invoke()

    println("== inline properties ==")
    val config = Config(mapOf("a" to "1", "b" to "2"))
    println(config.size)
    println(config.isEmpty)
    println(Config(emptyMap()).isEmpty)
}
