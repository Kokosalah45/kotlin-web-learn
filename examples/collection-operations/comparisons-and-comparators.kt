// Examples for /collection-operations/comparisons-and-comparators
// Signatures verified against kotlin v2.4.21
// libraries/stdlib/src/kotlin/comparisons/Comparisons.kt
package examples.collectionoperations

import java.time.LocalDate

data class Employee(val name: String, val age: Int, val salary: Double)

data class PersonName(val lastName: String, val firstName: String, val age: Int) :
    Comparable<PersonName> {
    override fun compareTo(other: PersonName): Int =
        compareValuesBy(this, other, PersonName::lastName, PersonName::firstName, PersonName::age)
}

fun main() {
    println("== compareValues is null-safe ==")
    println(compareValues(1, 2))
    println(compareValues(2, 1))
    println(compareValues(1, 1))
    println(compareValues(null, 1))
    println(compareValues(1, null))
    println(compareValues<Int>(null, null))

    println("== compareValuesBy with several selectors ==")
    val a = PersonName("Smith", "Ada", 36)
    val b = PersonName("Smith", "Bob", 29)
    println(compareValuesBy(a, b, PersonName::lastName))
    println(compareValuesBy(a, b, PersonName::lastName, PersonName::firstName))

    println("== Comparable drives the operators ==")
    println(a < b)
    println(listOf(b, a).sorted())

    println("== compareBy / thenByDescending ==")
    val employees = listOf(
        Employee("Ada", 36, 120.0),
        Employee("Bob", 36, 95.0),
        Employee("Cy", 29, 95.0)
    )
    println(employees.sortedWith(compareBy<Employee> { it.age }.thenByDescending { it.salary }))

    println("== compareBy with a vararg of selectors ==")
    println(employees.sortedWith(compareBy({ it.age }, { it.name })))

    println("== naturalOrder / reverseOrder ==")
    println(listOf("b", "a", "c").sortedWith(naturalOrder()))
    println(listOf("b", "a", "c").sortedWith(reverseOrder()))
    println(listOf("b", "a", "c").sortedWith(naturalOrder<String>().reversed()))

    println("== nullsFirst / nullsLast with comparing dates ==")
    val d1: LocalDate? = LocalDate.of(2026, 1, 5)
    val d2: LocalDate? = null
    println(compareValues(d1, d2))
    val dates = listOf(LocalDate.of(2026, 3, 1), null, LocalDate.of(2025, 12, 31))
    println(dates.sortedWith(nullsFirst(naturalOrder())))
    println(dates.sortedWith(nullsLast(naturalOrder())))

    println("== a Comparator built by subtraction can overflow ==")
    val naive = Comparator { x: Int, y: Int -> x - y }
    println(naive.compare(Int.MIN_VALUE, 1))
    println(compareValues(Int.MIN_VALUE, 1))
}
