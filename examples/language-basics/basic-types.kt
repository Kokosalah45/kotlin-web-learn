// Examples for /language-basics/basic-types
// Adapted from https://kotlinlang.org/docs/types-overview.html
package examples.languagebasics

fun main() {
    println("== everything is an object ==")
    println(1.toString())
    println(1.plus(2))
    println('a'.code)
    println(true.not())
    println(3.14.compareTo(2.71))
    println(1 is Any)
    println('a' is Any)
    println(true is Any)

    println("== the seven basic types ==")
    val anInt: Int = 42
    val aUInt: UInt = 42u
    val aDouble: Double = 42.0
    val aBoolean: Boolean = true
    val aChar: Char = 'K'
    val aString: String = "Kotlin"
    val anArray: Array<Int> = arrayOf(1, 2, 3)
    for (value in listOf<Any>(anInt, aUInt, aDouble, aBoolean, aChar, aString, anArray)) {
        println("${value::class.simpleName.toString().padEnd(10)} $value")
    }

    println("== sizes ==")
    println("Byte=${Byte.SIZE_BITS} Short=${Short.SIZE_BITS} Int=${Int.SIZE_BITS} Long=${Long.SIZE_BITS}")
    println("Float=${Float.SIZE_BITS} Double=${Double.SIZE_BITS}")
    println("UByte=${UByte.SIZE_BITS} UShort=${UShort.SIZE_BITS} UInt=${UInt.SIZE_BITS} ULong=${ULong.SIZE_BITS}")

    println("== primitive at runtime, class in the source ==")
    val plain: Int = 1
    val nullable: Int? = 1
    println(plain::class.java)
    println(nullable!!::class.java)
    println(plain::class.javaPrimitiveType)
    println(plain::class.javaObjectType)

    println("== every type is non-nullable by default ==")
    val required: String = "present"
    val optional: String? = null
    println(required.length)
    println(optional?.length)
    println(optional is String)
    println(null is String?)

    println("== Any, Unit and Nothing ==")
    val anything: Any = 1
    println(anything)
    println(Unit)
    println(Unit::class.simpleName)
    fun doNothing() {}
    println(doNothing() === Unit)
    fun stop(): Nothing = throw IllegalStateException("stopped")
    println(runCatching { stop() }.exceptionOrNull()?.message)
    println(emptyList<Nothing>())

    println("== no implicit conversion between numeric types ==")
    val small: Int = 1
    println(small.toDouble())
    println(small.toLong())
    println(1 == 1L.toInt())

    println("== but mixed arithmetic widens ==")
    println((1 + 1L)::class.simpleName)
    println((1 + 1.0)::class.simpleName)
    println((1.0f + 1.0)::class.simpleName)
    println(('a' + 1)::class.simpleName)
}
