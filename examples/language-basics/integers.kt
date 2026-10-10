// Examples for /language-basics/basic-types/integers
// Adapted from https://kotlinlang.org/docs/numbers.html#integer-types
package examples.languagebasics

fun List<Any>.logTypes() {
    println(joinToString(" | ") { it::class.simpleName ?: "Unknown" })
}

fun main() {
    println("== the four types and their ranges ==")
    println("Byte  ${Byte.SIZE_BITS} bits: ${Byte.MIN_VALUE}..${Byte.MAX_VALUE}")
    println("Short ${Short.SIZE_BITS} bits: ${Short.MIN_VALUE}..${Short.MAX_VALUE}")
    println("Int   ${Int.SIZE_BITS} bits: ${Int.MIN_VALUE}..${Int.MAX_VALUE}")
    println("Long  ${Long.SIZE_BITS} bits: ${Long.MIN_VALUE}..${Long.MAX_VALUE}")

    println("== literal forms ==")
    val one: Int = 1
    val oneBillion: Long = 1_000_000_000
    val hexBytes: Int = 0x7F_EC_DE_5E
    val bytes: Int = 0b01010010_01101001_10010100_10010010
    val oneLong = 1L
    println(one)
    println(oneBillion)
    println(hexBytes)
    println(bytes)
    println(oneLong)
    println(0x0F)
    println(0b00001011)

    println("== what gets inferred ==")
    val million = 1_000_000
    val threeBillion = 3_000_000_000
    println("${million::class.simpleName} / ${threeBillion::class.simpleName}")

    println("== integer division truncates ==")
    println(5 / 2)
    println(5L / 2)
    println(-5 / 2)
    println(5 % 2)
    println(-5 % 2)
    println(5 / 2.0)
    println(5 / 2.toDouble())

    println("== conversions are explicit ==")
    val intValue: Int = 1
    println(intValue.toDouble())
    println(intValue.toLong())
    println(1.5.toLong())
    println((-1.5).toLong())

    println("== mixed expressions widen ==")
    val intNumber: Int = 1
    val longNumber: Long = 1000
    val result = intNumber + longNumber
    println("$result is a ${result::class.simpleName}")
    println((1.toByte() + 1.toByte())::class.simpleName)

    println("== integer literal types ==")
    listOf(1, 2).logTypes()
    listOf(1L, 2L).logTypes()
    listOf(1, 2L).logTypes()
    listOf(1.toInt(), 2L).logTypes()
    val longValues: List<Long> = listOf(1, 2)
    longValues.logTypes()

    println("== overflow in arithmetic ==")
    val maxInt: Int = 2147483647
    println(maxInt + 1)
    println(Int.MAX_VALUE + Int.MAX_VALUE)
    println(Long.MAX_VALUE + 1)

    println("== overflow in negation ==")
    val min = Int.MIN_VALUE
    println(-min)
    println(Math.abs(Int.MIN_VALUE))

    println("== narrowing conversions lose data ==")
    val large: Int = 130
    println(large.toByte())
    println(300.toByte())
    println(65536.toShort())
    println(1_000_000_000_000L.toInt())

    println("== bitwise operations ==")
    val x = 1
    println(x shl 2)
    println(x shl 31)
    println(x and 0x000FF000)
    println(0b1100 and 0b1010)
    println(0b1100 or 0b1010)
    println(0b1100 xor 0b1010)
    println(x.inv())
    println(-8 shr 1)
    println(-8 ushr 1)

    println("== boxing and the JVM's small-integer cache ==")
    val score: Int = 100
    val savedScore: Int? = score
    val displayedScore: Int? = score
    println(savedScore === displayedScore)
    println(savedScore == displayedScore)

    val big: Int = 10000
    val savedBig: Int? = big
    val displayedBig: Int? = big
    println(savedBig === displayedBig)
    println(savedBig == displayedBig)

    println("== where the cache boundary is ==")
    for (n in listOf(126, 127, 128, 129)) {
        val a: Int? = n
        val b: Int? = n
        println("$n: === is ${a === b}")
    }

    println("== nullable integers ==")
    val maybeAbsent: Int? = null
    println(maybeAbsent)
    println(maybeAbsent?.plus(1))
}
