// Examples for /collection-operations/collection-specific-operations/set-operations
// Adapted from https://kotlinlang.org/docs/set-operations.html
package examples.collectionoperations

fun main() {
    println("== union: operand order shows in the result ==")
    val numbers = setOf("one", "two", "three")
    println(numbers union setOf("four", "five"))
    println(setOf("four", "five") union numbers)

    println("== intersect / subtract ==")
    println(numbers intersect setOf("two", "one"))
    println(numbers subtract setOf("three", "four"))
    println(numbers subtract setOf("four", "three"))

    println("== symmetric difference ==")
    val numbers2 = setOf("three", "four")
    println((numbers - numbers2) union (numbers2 - numbers))

    println("== on lists, the result is always a Set ==")
    val list1 = listOf(1, 1, 2, 3, 5, 8, -1)
    val list2 = listOf(1, 1, 2, 2, 3, 5)
    println(list1 intersect list2)
    println(list1 union list2)
    println((list1 union list2)::class.simpleName)
}
