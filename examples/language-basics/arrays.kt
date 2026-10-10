// Examples for /language-basics/basic-types/arrays
// Adapted from https://kotlinlang.org/docs/arrays.html
package examples.languagebasics

fun printArr(arr: Array<out Any>) {
    for (item in arr) print("$item, ")
    println()
}

fun printAllStrings(vararg strings: String) {
    for (string in strings) print(string)
    println()
}

fun main() {
    println("== creating ==")
    val simpleArray = arrayOf(1, 2, 3)
    println(simpleArray.joinToString())
    println(simpleArray::class.simpleName)

    val emptyArrayRight = emptyArray<String>()
    val emptyArrayLeft: Array<String> = emptyArray()
    println("${emptyArrayRight.size} ${emptyArrayLeft.size}")

    val nullArray: Array<Int?> = arrayOfNulls(3)
    println(nullArray.joinToString())

    val zeroes = Array<Int>(3) { 0 }
    println(zeroes.joinToString())
    val squares = Array(5) { i -> i * i }
    println(squares.joinToString())

    println("== nested ==")
    val twoDArray = Array(2) { Array<Int>(2) { 0 } }
    println(twoDArray.contentDeepToString())
    val threeDArray = Array(3) { Array(3) { Array<Int>(3) { 0 } } }
    println(threeDArray.contentDeepToString())

    println("== the Array constructor runs its lambda per index ==")
    val shared = Array(3) { mutableListOf<Int>() }
    shared[0].add(1)
    println(shared.map { it.size })

    println("== primitive-type arrays ==")
    val primitiveTypeArray = IntArray(5)
    println(primitiveTypeArray.joinToString())
    val initialized = IntArray(5) { i -> i * i }
    println(initialized.joinToString())
    println(intArrayOf(1, 2, 3, 4, 5).joinToString())
    println(charArrayOf('K', 't', 'l').joinToString())
    println(doubleArrayOf(0.22, 4.16, 0.5).joinToString())
    println(IntArray(3)::class.simpleName)
    println(Array(3) { 0 }::class.simpleName)
    val asAny: Any = IntArray(0)
    println(asAny is Array<*>)
    println(asAny is IntArray)

    println("== converting between the two ==")
    println(intArrayOf(1, 2, 3).toTypedArray()::class.simpleName)
    println(arrayOf(1, 2, 3).toIntArray()::class.simpleName)

    println("== access and modify ==")
    simpleArray[0] = 10
    twoDArray[0][0] = 2
    println(simpleArray[0])
    println(twoDArray[0][0])
    println(runCatching { simpleArray[5] }.exceptionOrNull()?.let { it::class.simpleName })
    println(simpleArray.getOrNull(5))

    println("== val does not make the contents read-only ==")
    val fixed = arrayOf(1, 2, 3)
    fixed[0] = 99
    println(fixed.joinToString())

    println("== fill ==")
    val arr = IntArray(3)
    println(arr.joinToString())
    arr.fill(1)
    println(arr.joinToString())
    arr.fill(0, 0, 2)
    println(arr.joinToString())

    println("== size, indices, lastIndex ==")
    val letters = arrayOf("a", "b", "c", "c")
    println("${letters.size} ${letters.indices} ${letters.lastIndex}")
    println("${letters.first()} ${letters.last()}")
    println("${letters.isEmpty()} ${letters.isNotEmpty()}")
    println(letters.contains("b"))
    println("b" in letters)

    println("== arrays are invariant ==")
    printArr(arrayOf("k", "t", "n"))

    println("== adding means copying ==")
    var grow = intArrayOf(0, 1, 2)
    grow = grow.copyOf(grow.size + 1)
    println(grow.joinToString())
    grow[grow.lastIndex] = 3
    println(grow.joinToString())

    var plus = intArrayOf(0, 1, 2)
    plus += 3
    println(plus.joinToString())
    plus = plus + intArrayOf(4, 5)
    println(plus.joinToString())

    println("== == compares references, not contents ==")
    val first = arrayOf(1, 2, 3)
    val second = arrayOf(1, 2, 3)
    println(first == second)
    println(first.contentEquals(second))
    println(first contentEquals second)
    println(first.toList() == second.toList())
    val nestedA = arrayOf(arrayOf(1, 2), arrayOf(3))
    val nestedB = arrayOf(arrayOf(1, 2), arrayOf(3))
    println(nestedA.contentEquals(nestedB))
    println(nestedA.contentDeepEquals(nestedB))

    println("== toString, and the one that works ==")
    println(arrayOf(1, 2, 3).toString().substringBefore('@'))
    println(arrayOf(1, 2, 3).contentToString())
    println(nestedA.contentDeepToString())
    println(intArrayOf(1, 2, 3).contentToString())

    println("== arrays as hash keys ==")
    val key1 = arrayOf(1, 2)
    val key2 = arrayOf(1, 2)
    val byArray = hashMapOf(key1 to "first")
    println(byArray[key2])
    println(byArray[key1])

    println("== transformations ==")
    println(arrayOf(1, 2, 3).sum())
    println(arrayOf(3, 1, 2).sortedArray().joinToString())
    val toSort = arrayOf(3, 1, 2)
    toSort.sort()
    println(toSort.joinToString())
    println(arrayOf(1, 2, 3).map { it * 2 })
    println(arrayOf(1, 2, 3).filter { it > 1 })

    println("== spread operator ==")
    val lettersArray = arrayOf("c", "d")
    printAllStrings("a", "b", *lettersArray)

    println("== converting to collections copies ==")
    println(letters.toSet())
    println(letters.toList())
    val backing = arrayOf("a", "b", "c")
    val copied = backing.toList()
    val wrapped = backing.asList()
    backing[0] = "d"
    println("copied: $copied")
    println("wrapped: $wrapped")

    println("== toMap keeps the last duplicate ==")
    val pairArray = arrayOf("apple" to 120, "banana" to 150, "cherry" to 90, "apple" to 140)
    println(pairArray.toMap())
}
