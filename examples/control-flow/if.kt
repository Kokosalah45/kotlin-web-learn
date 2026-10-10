// Examples for /control-flow/conditional-expressions/if
// Adapted from https://kotlinlang.org/docs/control-flow.html#if-expression
package examples.controlflow

fun main() {
    val heightAlice = 160
    val heightBob = 175

    println("== statement form ==")
    var taller = heightAlice
    if (heightAlice < heightBob) taller = heightBob
    println("Taller height is $taller")

    println("== else and else if ==")
    if (heightAlice > heightBob) {
        taller = heightAlice
    } else {
        taller = heightBob
    }
    println("Taller height is $taller")

    println("== expression form: Kotlin's ternary ==")
    taller = if (heightAlice > heightBob) heightAlice else heightBob
    val heightLimit = 150
    val heightOrLimit =
        if (heightLimit > heightAlice) heightLimit
        else if (heightAlice > heightBob) heightAlice
        else heightBob
    println("Taller height is $taller")
    println("Height or limit is $heightOrLimit")

    println("== branches as blocks ==")
    val chosen = if (heightAlice > heightBob) {
        print("Choose Alice\n")
        heightAlice
    } else {
        print("Choose Bob\n")
        heightBob
    }
    println("Taller height is $chosen")

    println("== the type is the common supertype of the branches ==")
    val mixed = if (heightAlice > heightBob) "alice" else 175
    println(mixed)
    println(mixed::class.simpleName)
    val both: Any = mixed
    println(both is Comparable<*>)

    println("== if with a Nothing branch ==")
    fun requirePositive(n: Int): Int = if (n > 0) n else throw IllegalArgumentException("not positive: $n")
    println(requirePositive(5))
    println(runCatching { requirePositive(-5) }.exceptionOrNull()?.message)

    println("== smart cast inside the branch ==")
    val value: Any = "ID-98345"
    if (value is String) {
        println(value.startsWith("ID-"))
        println(value.length)
    }

    println("== null checks and the Elvis operator ==")
    val name: String? = null
    val display = if (name != null) name else "anonymous"
    println(display)
    println(name ?: "anonymous")
}
