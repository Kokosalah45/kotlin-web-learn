// Examples for /collection-operations/constructing-collections
// Adapted from https://kotlinlang.org/docs/constructing-collections.html
package examples.collectionoperations

import java.util.LinkedList

class Human(var name: String) {
    override fun toString() = name
}

fun main() {
    println("== from elements ==")
    println(setOf("one", "two", "three", "four"))
    println(mutableSetOf<String>())
    println(mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 1))

    println("== apply() instead of `to` pairs ==")
    println(mutableMapOf<String, String>().apply { this["one"] = "1"; this["two"] = "2" })

    println("== builder functions ==")
    val map = buildMap {
        put("a", 1)
        put("b", 0)
        put("c", 4)
    }
    println(map)
    println(buildList { add(1); addAll(listOf(2, 3)) })

    println("== empty collections ==")
    println(emptyList<String>())

    println("== List(size) initializer ==")
    println(List(3) { it * 2 })

    println("== concrete types ==")
    println(LinkedList<String>(listOf("one", "two", "three")))
    println(HashSet<Int>(32).size)

    println("== copies are shallow ==")
    val alice = Human("Alice")
    val sourceList = mutableListOf(alice, Human("Bob"))
    val copyList = sourceList.toList()
    sourceList.add(Human("Charles"))
    alice.name = "Alicia"
    println("First item's name is: ${sourceList[0].name} in source and ${copyList[0].name} in copy")
    println("List size is: ${sourceList.size} in source and ${copyList.size} in copy")

    println("== converting between types ==")
    val ints = mutableListOf(1, 2, 3)
    val copySet = ints.toMutableSet()
    copySet.add(3)
    copySet.add(4)
    println(copySet)

    println("== a reference is not a copy ==")
    val referenced = mutableListOf(1, 2, 3)
    val referenceList = referenced
    referenceList.add(4)
    println("Source size: ${referenced.size}")

    println("== restricting mutability with a read-only reference ==")
    val backing = mutableListOf(1, 2, 3)
    val readOnly: List<Int> = backing
    backing.add(4)
    println(readOnly)
}
