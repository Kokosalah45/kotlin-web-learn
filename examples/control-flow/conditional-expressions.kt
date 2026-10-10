// Examples for /control-flow/conditional-expressions
// Adapted from https://kotlinlang.org/docs/control-flow.html
package examples.controlflow

enum class Bit { ZERO, ONE }

fun main() {
    val heightAlice = 160
    val heightBob = 175

    println("== if as a statement ==")
    var taller = heightAlice
    if (heightAlice < heightBob) taller = heightBob
    println(taller)

    println("== if as an expression ==")
    val tallerExpr = if (heightAlice > heightBob) heightAlice else heightBob
    println(tallerExpr)

    println("== when as a statement ==")
    val userRole = "Editor"
    when (userRole) {
        "Viewer" -> println("User has read-only access")
        "Editor" -> println("User can edit content")
        else -> println("User role is not recognized")
    }

    println("== when as an expression ==")
    val x = 2
    val text = when (x) {
        1 -> "x == 1"
        2 -> "x == 2"
        else -> "x is neither 1 nor 2"
    }
    println(text)

    println("== a statement need not be exhaustive ==")
    val deliveryStatus = "OutForDelivery"
    when (deliveryStatus) {
        "Pending" -> println("Your order is being prepared")
        "Shipped" -> println("Your order is on the way")
    }
    println("nothing matched, and that is not an error")

    println("== an expression must be ==")
    val numericValue = when (Bit.ONE) {
        // No else branch is needed because all cases are covered
        Bit.ZERO -> 0
        Bit.ONE -> 1
    }
    println(numericValue)

    println("== both branch kinds can be blocks ==")
    val chosen = if (heightAlice > heightBob) {
        println("Choose Alice")
        heightAlice
    } else {
        println("Choose Bob")
        heightBob
    }
    println(chosen)

    println("== the same thing, two ways ==")
    fun describeIf(n: Int): String =
        if (n < 0) "negative" else if (n == 0) "zero" else "positive"

    fun describeWhen(n: Int): String = when {
        n < 0 -> "negative"
        n == 0 -> "zero"
        else -> "positive"
    }
    for (n in listOf(-1, 0, 1)) println("${describeIf(n)} / ${describeWhen(n)}")
}
