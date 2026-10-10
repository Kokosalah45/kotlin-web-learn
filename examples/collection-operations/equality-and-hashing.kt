// Examples for /collection-operations/equality-and-hashing
// Adapted from https://kotlinlang.org/docs/equality.html
package examples.collectionoperations

class Point(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false
        return this.x == other.x && this.y == other.y
    }

    override fun hashCode(): Int = 31 * x + y
}

class PlainPoint(val x: Int, val y: Int)

data class DataPoint(val x: Int, val y: Int)

data class MutableKey(var id: Int)

fun main() {
    println("== structural equality ==")
    val a = "hello"
    val b = "hello"
    val c: String? = null
    val d: String? = null
    val e = d
    println(a == b)
    println(a == c)
    println(c == e)

    println("== referential equality ==")
    val s1 = "Hello"
    val s2 = s1
    val s3 = "world"
    val s4 = "world"
    println(s1 === s2)
    println(s1 === s3)
    println(s3 === s4)

    println("== data class vs plain class ==")
    println(DataPoint(1, 2) == DataPoint(1, 2))
    println(PlainPoint(1, 2) == PlainPoint(1, 2))
    println(Point(1, 2) == Point(1, 2))

    println("== equality drives Set and Map ==")
    println(setOf(DataPoint(1, 2), DataPoint(1, 2)).size)
    println(setOf(PlainPoint(1, 2), PlainPoint(1, 2)).size)

    println("== mutating a key after insertion ==")
    val set = HashSet<MutableKey>()
    val key = MutableKey(1)
    set.add(key)
    println("found before mutation: ${key in set}")
    key.id = 2
    println("found after mutation:  ${key in set}")
    println("still reported size:   ${set.size}")
    println("iteration still shows: ${set.toList()}")

    println("== list equality depends on order ==")
    println(listOf(1, 2, 3) == listOf(1, 2, 3))
    println(listOf(1, 2, 3) == listOf(3, 2, 1))
    println(setOf(1, 2, 3) == setOf(3, 2, 1))

    println("== arrays need contentEquals ==")
    println(arrayOf(1, 2, 3) == arrayOf(1, 2, 3))
    println(arrayOf(1, 2, 3).contentEquals(arrayOf(1, 2, 3)))

    println("== floating point ==")
    val nan = Double.NaN
    println(nan == nan)
    val boxed: Any = Double.NaN
    println(boxed == Double.NaN)
    println(0.0 == -0.0)
    println(listOf(0.0) == listOf(-0.0))
}
