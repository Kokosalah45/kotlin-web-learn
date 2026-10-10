// Examples for /collection-operations/collection-specific-operations/map-operations
// Adapted from https://kotlinlang.org/docs/map-operations.html
package examples.collectionoperations

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    println("== retrieve ==")
    val numbersMap = mapOf("one" to 1, "two" to 2, "three" to 3)
    println(numbersMap.get("one"))
    println(numbersMap["one"])
    println(numbersMap.getOrDefault("four", 10))
    println(numbersMap["five"])
    println(numbersMap.getOrElse("five") { -1 })
    println(runCatching { numbersMap.getValue("six") }.exceptionOrNull()?.let { it::class.simpleName })

    println("== nullable values: missing vs null ==")
    val nullableMap = mapOf("one" to 1, "two" to null)
    println(nullableMap["two"])
    println(nullableMap["nope"])
    println(nullableMap.getOrElseIfNull("two") { 0 })
    println(nullableMap.getOrElseIfMissing("two") { 0 })

    println("== keys and values ==")
    println(numbersMap.keys)
    println(numbersMap.values)

    println("== filter ==")
    val keyed = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
    println(keyed.filter { (key, value) -> key.endsWith("1") && value > 10 })
    println(keyed.filterKeys { it.endsWith("1") })
    println(keyed.filterValues { it < 10 })

    println("== plus: right-hand side wins ==")
    println(numbersMap + Pair("four", 4))
    println(numbersMap + Pair("one", 10))
    println(numbersMap + mapOf("five" to 5, "one" to 11))

    println("== minus takes keys ==")
    println(numbersMap - "one")
    println(numbersMap - listOf("two", "four"))

    println("== put / putAll ==")
    val putting = mutableMapOf("one" to 1, "two" to 2)
    putting.put("three", 3)
    println(putting)
    putting.putAll(setOf("four" to 4, "five" to 5))
    println(putting)

    println("== put returns the previous value ==")
    val updating = mutableMapOf("one" to 1, "two" to 2)
    val previousValue = updating.put("one", 11)
    println("value associated with 'one', before: $previousValue, after: ${updating["one"]}")

    println("== operator forms ==")
    val operators = mutableMapOf("one" to 1, "two" to 2)
    operators["three"] = 3
    operators += mapOf("four" to 4, "five" to 5)
    println(operators)

    println("== getOrPut ==")
    val counts = mutableMapOf<String, Int>()
    for (word in listOf("a", "b", "a", "c", "a")) {
        counts[word] = counts.getOrPut(word) { 0 } + 1
    }
    println(counts)

    println("== getOrPutIfNull vs getOrPutIfMissing ==")
    val mapForNull = mutableMapOf<String, Int?>("one" to null)
    val mapForMissing = mutableMapOf<String, Int?>("one" to null)
    mapForNull.getOrPutIfNull("one") { 1 }
    println(mapForNull)
    mapForMissing.getOrPutIfMissing("one") { 1 }
    println(mapForMissing)

    println("== remove ==")
    val removing = mutableMapOf("one" to 1, "two" to 2, "three" to 3)
    removing.remove("one")
    println(removing)
    removing.remove("three", 4)
    println(removing)

    println("== remove via keys / values ==")
    val viaViews = mutableMapOf("one" to 1, "two" to 2, "three" to 3, "threeAgain" to 3)
    viaViews.keys.remove("one")
    println(viaViews)
    viaViews.values.remove(3)
    println(viaViews)

    println("== -= ==")
    val minusing = mutableMapOf("one" to 1, "two" to 2, "three" to 3)
    minusing -= "two"
    println(minusing)
    minusing -= "five"
    println(minusing)
}
