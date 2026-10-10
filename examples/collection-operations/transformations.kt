// Examples for /collection-operations/transformations
// Adapted from https://kotlinlang.org/docs/collection-transformations.html
package examples.collectionoperations

data class FullName(val firstName: String, val lastName: String)

data class StringContainer(val values: List<String>)

fun main() {
    println("== map ==")
    val numbers = setOf(1, 2, 3)
    println(numbers.map { it * 3 })
    println(numbers.mapIndexed { idx, value -> value * idx })

    println("== mapNotNull ==")
    println(numbers.mapNotNull { if (it == 2) null else it * 3 })
    println(numbers.mapIndexedNotNull { idx, value -> if (idx == 0) null else value * idx })

    println("== mapKeys / mapValues ==")
    val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
    println(numbersMap.mapKeys { it.key.uppercase() })
    println(numbersMap.mapValues { it.value + it.key.length })

    println("== zip ==")
    val colors = listOf("red", "brown", "grey")
    val animals = listOf("fox", "bear", "wolf")
    println(colors zip animals)
    val twoAnimals = listOf("fox", "bear")
    println(colors.zip(twoAnimals))

    println("== zip with transform ==")
    println(colors.zip(animals) { color, animal -> "The ${animal.replaceFirstChar { it.uppercase() }} is $color" })

    println("== unzip ==")
    val numberPairs = listOf("one" to 1, "two" to 2, "three" to 3, "four" to 4)
    println(numberPairs.unzip())

    println("== associateWith ==")
    val words = listOf("one", "two", "three", "four")
    println(words.associateWith { it.length })

    println("== associateBy ==")
    println(words.associateBy { it.first().uppercaseChar() })
    println(words.associateBy(keySelector = { it.first().uppercaseChar() }, valueTransform = { it.length }))

    println("== associate ==")
    val names = listOf("Alice Adams", "Brian Brown", "Clara Campbell")
    println(names.associate { name -> parseFullName(name).let { it.lastName to it.firstName } })

    println("== flatten ==")
    val numberSets = listOf(setOf(1, 2, 3), setOf(4, 5, 6), setOf(1, 2))
    println(numberSets.flatten())

    println("== flatMap ==")
    val containers = listOf(
        StringContainer(listOf("one", "two", "three")),
        StringContainer(listOf("four", "five", "six")),
        StringContainer(listOf("seven", "eight"))
    )
    println(containers.flatMap { it.values })

    println("== joinToString / joinTo ==")
    println(words)
    println(words.joinToString())
    val listString = StringBuffer("The list of numbers: ")
    words.joinTo(listString)
    println(listString)

    println("== joinToString parameters ==")
    println(words.joinToString(separator = " | ", prefix = "start: ", postfix = ": end"))
    println((1..100).toList().joinToString(limit = 10, truncated = "<...>"))
    println(words.joinToString { "Element: ${it.uppercase()}" })
}

private fun parseFullName(fullName: String): FullName {
    val nameParts = fullName.split(" ")
    if (nameParts.size == 2) return FullName(nameParts[0], nameParts[1])
    throw Exception("Wrong name format")
}
