// Examples for /language-basics/basic-types/unsigned-integers
// Adapted from https://kotlinlang.org/docs/unsigned-integer-types.html
package examples.languagebasics

data class Color(val representation: UInt)

@OptIn(ExperimentalUnsignedTypes::class)
fun byteOrderMark(): UByteArray = ubyteArrayOf(0xEFu, 0xBBu, 0xBFu)

fun main() {
    println("== the four types and their ranges ==")
    println("UByte  ${UByte.SIZE_BITS} bits: ${UByte.MIN_VALUE}..${UByte.MAX_VALUE}")
    println("UShort ${UShort.SIZE_BITS} bits: ${UShort.MIN_VALUE}..${UShort.MAX_VALUE}")
    println("UInt   ${UInt.SIZE_BITS} bits: ${UInt.MIN_VALUE}..${UInt.MAX_VALUE}")
    println("ULong  ${ULong.SIZE_BITS} bits: ${ULong.MIN_VALUE}..${ULong.MAX_VALUE}")

    println("== literals ==")
    val b: UByte = 1u
    val s: UShort = 1u
    val l: ULong = 1u
    val a1 = 42u
    val a2 = 0xFFFF_FFFF_FFFFu
    val a3 = 1UL
    println("$b ${b::class.simpleName}")
    println("$s ${s::class.simpleName}")
    println("$l ${l::class.simpleName}")
    println("$a1 ${a1::class.simpleName}")
    println("$a2 ${a2::class.simpleName}")
    println("$a3 ${a3::class.simpleName}")

    println("== the full bit range ==")
    val yellow = Color(0xFFCC00CCu)
    println(yellow)
    println(yellow.representation)
    println(0xFFCC00CCu.toInt())

    println("== arithmetic wraps, it does not go negative ==")
    val zero: UInt = 0u
    println(zero - 1u)
    println(UInt.MAX_VALUE + 1u)
    println(1u - 2u)
    println(3u / 2u)
    println(3u % 2u)

    println("== signed and unsigned are not subtypes of each other ==")
    val signed: Int = -1
    println(signed.toUInt())
    println(signed.toUInt().toInt())
    println(255u.toByte())
    println((255u).toUByte())
    println(UInt.MAX_VALUE.toLong())
    println(ULong.MAX_VALUE.toLong())

    println("== comparison and conversion of the same bits ==")
    println(4294967295u > 1u)
    println((-1).toUInt() == UInt.MAX_VALUE)

    println("== unsigned ranges ==")
    println((1u..5u).toList())
    println((1u..10u step 3).toList())
    println(5u in 1u..10u)
    println((1u..5u)::class.simpleName)

    println("== unsigned arrays (Beta, opt-in required) ==")
    @OptIn(ExperimentalUnsignedTypes::class)
    println(byteOrderMark().joinToString { it.toString(16) })

    println("== toString on an unsigned value ==")
    println(UInt.MAX_VALUE.toString())
    println(UInt.MAX_VALUE.toString(16))
    println(UByte.MAX_VALUE.toString(2))

    println("== what they are at runtime ==")
    println(a1::class.qualifiedName)
    println(a1::class.java.isPrimitive)
}
