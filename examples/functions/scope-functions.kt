// Examples for /functions/scope-functions
// Adapted from https://kotlinlang.org/docs/scope-functions.html
package examples.functions

data class Resident2(var name: String, var age: Int, var city: String) {
    fun moveTo(newCity: String) { city = newCity }
    fun incrementAge() { age++ }
}

class MultiportService(var url: String, var port: Int) {
    fun prepareRequest(): String = "Default request"
    fun query(request: String): String = "Result for query '$request'"
}

fun writeToLog(message: String) {
    println("INFO: $message")
}

fun processNonNullString(str: String) {
    println("processing '$str'")
}

fun main() {
    println("== let gives the object a short name ==")
    Resident2("Alice", 20, "Amsterdam").let {
        println(it)
        it.moveTo("London")
        it.incrementAge()
        println(it)
    }

    println("== this vs it ==")
    val str = "Hello"
    str.run { println("The string's length: $length") }
    str.let { println("The string's length is ${it.length}") }

    println("== what each one returns ==")
    val target = mutableListOf("one", "two", "three")
    println(target.let { it.size })
    println(target.run { size })
    println(with(target) { size })
    println(target.apply { add("four") })
    println(target.also { it.add("five") })

    println("== apply configures and returns the object ==")
    val adam = Resident2("Adam", 0, "").apply {
        age = 32
        city = "London"
    }
    println(adam)

    println("== also and apply chain as side steps ==")
    val numberList = mutableListOf<Double>()
    numberList.also { println("Populating the list") }
        .apply {
            add(2.71)
            add(3.14)
            add(1.0)
        }
        .also { println("Sorting the list") }
        .sort()
    println(numberList)

    println("== let on a nullable value ==")
    val maybe: String? = "Hello"
    val length = maybe?.let {
        println("let() called on $it")
        processNonNullString(it)
        it.length
    }
    println(length)
    val absent: String? = null
    println(absent?.let { it.length })

    println("== naming the lambda argument ==")
    val first = listOf("one", "two", "three", "four").first().let { firstItem ->
        println("The first item of the list is '$firstItem'")
        if (firstItem.length >= 5) firstItem else "!" + firstItem + "!"
    }.uppercase()
    println("First item after modifications: '$first'")

    println("== with groups calls on an object ==")
    val numbers = mutableListOf("one", "two", "three")
    with(numbers) {
        println("'with' is called with argument $this")
        println("It contains $size elements")
    }
    val firstAndLast = with(numbers) {
        "The first element is ${first()}," + " the last element is ${last()}"
    }
    println(firstAndLast)

    println("== run initializes and computes ==")
    val service = MultiportService("https://example.kotlinlang.org", 80)
    val result = service.run {
        port = 8080
        query(prepareRequest() + " to port $port")
    }
    println(result)
    val letResult = service.let {
        it.port = 8081
        it.query(it.prepareRequest() + " to port ${it.port}")
    }
    println(letResult)

    println("== non-extension run ==")
    val hexNumberRegex = run {
        val digits = "0-9"
        val hexDigits = "A-Fa-f"
        val sign = "+-"
        Regex("[$sign]?[$digits$hexDigits]+")
    }
    for (match in hexNumberRegex.findAll("+123 -FFFF !%*& 88 XYZ")) {
        println(match.value)
    }

    println("== also for effects on the way through ==")
    fun doubledWithLog(n: Int): Int = (n * 2).also { writeToLog("doubled $n to $it") }
    println(doubledWithLog(21))

    println("== takeIf and takeUnless ==")
    val n = 42
    println(n.takeIf { it % 2 == 0 })
    println(n.takeUnless { it % 2 == 0 })
    println("Hello".takeIf { it.isNotEmpty() }?.uppercase())
    println("".takeIf { it.isNotEmpty() }?.uppercase())

    println("== takeIf plus let ==")
    fun displaySubstringPosition(input: String, sub: String) {
        input.indexOf(sub).takeIf { it >= 0 }?.let {
            println("The substring $sub is found in $input.")
            println("Its start position is $it.")
        }
    }
    displaySubstringPosition("010000011", "11")
    displaySubstringPosition("010000011", "12")

    println("== the trap: takeIf returns the object, however falsy it looks ==")
    println(0.takeIf { it == 0 })
    println(listOf<Int>().takeIf { it.isEmpty() })
    println(false.takeIf { !it })
}
