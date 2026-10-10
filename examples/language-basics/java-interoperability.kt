// Examples for /language-basics/introduction-to-kotlin/java-interoperability
// Adapted from https://kotlinlang.org/docs/java-interop.html
// and https://kotlinlang.org/docs/java-to-kotlin-interop.html
package examples.languagebasics

import java.util.ArrayList
import java.util.Locale

class JvmNames {
    @JvmField
    val plainField = "readable as a Java field"

    val withGetter = "readable as getWithGetter()"

    @JvmName("renamedOnTheJvm")
    fun originalName() = "called renamedOnTheJvm() from Java"

    companion object {
        @JvmStatic
        fun staticFromJava() = "callable as JvmNames.staticFromJava()"

        const val CONSTANT = "compiled to a static final field"
    }
}

@Throws(java.io.IOException::class)
fun mayThrow(fail: Boolean) {
    if (fail) throw java.io.IOException("declared for Java callers")
}

fun main() {
    println("== calling the Java standard library ==")
    val list = ArrayList<String>()
    list.add("kotlin")
    list.add("java")
    println(list)
    println(list::class.java.name)
    println("KOTLIN".lowercase(Locale.ROOT))
    println(java.lang.Integer.toBinaryString(10))
    println(java.lang.Math.floorMod(-5, 3))

    println("== Kotlin's own types map onto Java's ==")
    println(listOf(1, 2)::class.java.name)
    println(mutableListOf(1, 2)::class.java.name)
    println("text"::class.java.name)
    println(1::class.java.name)
    println((1 as Any)::class.java.name)
    println(intArrayOf(1)::class.java.name)
    println(arrayOf(1)::class.java.name)

    println("== getters and setters become properties ==")
    val builder = StringBuilder("kotlin")
    println(builder.length)
    builder.setLength(3)
    println(builder.toString())
    val now = java.util.Date(0)
    println(now.time)

    println("== platform types: Java nullability is unknown ==")
    val fromJava: String = System.getProperty("java.specification.version")
    println(fromJava)
    println("System.getProperty returns String! - usable as String or String?")
    val missing = System.getProperty("definitely.not.set")
    println(missing)
    println(runCatching { missing!!.length }.exceptionOrNull()?.let { it::class.simpleName })

    println("== mapped collection types are views, not copies ==")
    val javaList: MutableList<String> = list
    javaList.add("scala")
    println(list)

    println("== a Kotlin List may still be mutable underneath ==")
    val readOnlyView: List<String> = javaList
    println(readOnlyView.size)
    javaList.add("groovy")
    println(readOnlyView.size)

    println("== SAM conversion ==")
    val runnable = Runnable { println("ran as a lambda") }
    runnable.run()
    val comparator = Comparator<String> { x, y -> x.length - y.length }
    println(listOf("three", "a", "two").sortedWith(comparator))

    println("== annotations that shape the Java-facing API ==")
    val names = JvmNames()
    println(names.plainField)
    println(names.withGetter)
    println(names.originalName())
    println(JvmNames.staticFromJava())
    println(JvmNames.CONSTANT)
    println(JvmNames::class.java.getDeclaredMethod("renamedOnTheJvm").name)
    println(JvmNames::class.java.getDeclaredField("plainField").name)

    println("== @Throws for checked exceptions ==")
    mayThrow(false)
    println(runCatching { mayThrow(true) }.exceptionOrNull()?.message)
    println(::mayThrow.name)

    println("== Kotlin classes and their JVM shape ==")
    println(JvmNames::class.java.simpleName)
    println(object {}.javaClass.enclosingClass?.simpleName)
    println(Unit::class.java.name)
}
