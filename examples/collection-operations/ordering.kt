// Examples for /collection-operations/ordering
// Adapted from https://kotlinlang.org/docs/collection-ordering.html
package examples.collectionoperations

class Version(val major: Int, val minor: Int) : Comparable<Version> {
    override fun compareTo(other: Version): Int = when {
        this.major != other.major -> this.major compareTo other.major
        this.minor != other.minor -> this.minor compareTo other.minor
        else -> 0
    }

    override fun toString() = "$major.$minor"
}

data class Person(val name: String, val age: Int)

fun main() {
    println("== Comparable / natural order ==")
    println(Version(1, 2) > Version(1, 3))
    println(Version(2, 0) > Version(1, 5))

    println("== Comparator ==")
    val lengthComparator = Comparator { str1: String, str2: String -> str1.length - str2.length }
    println(listOf("aaa", "bb", "c").sortedWith(lengthComparator))
    println(listOf("aaa", "bb", "c").sortedWith(compareBy { it.length }))

    println("== multiple criteria with thenBy ==")
    println(
        listOf("aaa", "bb", "c", "b", "a", "aa", "ccc")
            .sortedWith(compareBy<String> { it.length }.thenBy { it })
    )

    println("== sorted / sortedDescending ==")
    val numbers = listOf("one", "two", "three", "four")
    println("Sorted ascending: ${numbers.sorted()}")
    println("Sorted descending: ${numbers.sortedDescending()}")

    println("== sortedBy / sortedByDescending ==")
    println("Sorted by length ascending: ${numbers.sortedBy { it.length }}")
    println("Sorted by the last letter descending: ${numbers.sortedByDescending { it.last() }}")

    println("== sortedWith ==")
    println("Sorted by length ascending: ${numbers.sortedWith(compareBy { it.length })}")

    println("== thenByDescending ==")
    val people = listOf(
        Person("Ada", 36), Person("Bob", 36), Person("Cy", 29), Person("Dee", 41)
    )
    println(people.sortedWith(compareByDescending<Person> { it.age }.thenBy { it.name }))

    println("== nullsFirst / nullsLast ==")
    val withNulls = listOf("bb", null, "a", null, "ccc")
    println(withNulls.sortedWith(nullsFirst(compareBy { it?.length })))
    println(withNulls.sortedWith(nullsLast(compareBy { it?.length })))

    println("== reversed vs asReversed on a read-only list ==")
    println(numbers.reversed())
    println(numbers.asReversed())

    println("== asReversed is a live view ==")
    val mutable = mutableListOf("one", "two", "three", "four")
    val reversedView = mutable.asReversed()
    val reversedCopy = mutable.reversed()
    println(reversedView)
    mutable.add("five")
    println("view after add:  $reversedView")
    println("copy after add:  $reversedCopy")

    println("== checking sorted order ==")
    println(listOf(1, 2, 3, 4).isSorted())
    println(listOf(4, 3, 2, 1).isSortedDescending())
    println(people.isSortedBy(Person::age))
    println(emptyList<Int>().isSorted())
    println(listOf(1).isSorted())

    println("== shuffled is random, so only the size is stable ==")
    println(numbers.shuffled().size)

    println("== sorted() does not mutate ==")
    val original = listOf(3, 1, 2)
    original.sorted()
    println(original)
}
