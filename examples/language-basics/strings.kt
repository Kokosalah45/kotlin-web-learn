// Examples for /language-basics/basic-types/strings
// Adapted from https://kotlinlang.org/docs/strings.html
package examples.languagebasics

fun main() {
    println("== declaring ==")
    val name: String = "Kotlin"
    val message = "Hello, world!"
    println("$name / $message / ${message::class.simpleName}")

    println("== escape sequences in a double-quoted literal ==")
    println("Hello,\nworld!")
    println("Kotlin says, \"Hi\".")

    println("== multiline strings keep the layout, and ignore escapes ==")
    val text = """
Hello,
Kotlin
"""
    print(text)
    val quote = """Kotlin says, "Hi"."""
    println(quote)
    println("""a\nb""")
    println("""a\nb""".length)

    println("== trimIndent ==")
    val indented = """
        Hello,
        Kotlin
    """.trimIndent()
    println(indented)
    println("---")
    val notTrimmed = """
        Hello,
        Kotlin
    """
    println(notTrimmed.lines().map { it.length })

    println("== trimMargin ==")
    println(
        """
        |Hello,
        |Kotlin
        """.trimMargin()
    )
    println(
        """
        >Hello,
        >Kotlin
        """.trimMargin(">")
    )

    println("== immutability ==")
    val original = "Kotlin"
    val upper = original.uppercase()
    println("$original / $upper")

    println("== length and indexing ==")
    println(name.length)
    println(name[0])
    println(name[5])
    println(runCatching { name[6] }.exceptionOrNull()?.let { it::class.simpleName })
    println(name.getOrNull(6))
    for (char in "Kot") println(char)

    println("== substring ==")
    println("Kotlin".substring(1))
    println("Kotlin".substring(1, 5))
    println("Kotlin".subSequence(1, 5))
    println("Kotlin".take(3))
    println("Kotlin".drop(3))

    println("== comparison ==")
    println("kotlin" == "kotlin")
    println("kotlin" == "Kotlin")
    println("abc".compareTo("abd") < 0)
    println("abc".compareTo("ABC") > 0)
    println("abc".compareTo("ABC", ignoreCase = true) == 0)
    println("abc".equals("ABC", ignoreCase = true))

    println("== == is content, === is identity ==")
    val a = "kotlin"
    val b = "kot" + "lin"
    val c = StringBuilder("kot").append("lin").toString()
    println("a == b: ${a == b}, a === b: ${a === b}")
    println("a == c: ${a == c}, a === c: ${a === c}")

    println("== changing content means making a copy ==")
    val padded = "  Hello, Kotlin  "
    println("[${padded.trim()}]")
    println("[${padded.replace("Kotlin", "world")}]")
    println("[${padded.uppercase()}]")
    println("[${padded.lowercase()}]")

    println("== inspecting ==")
    val domain = "kotlinlang.org"
    println(domain.contains("."))
    println(domain.startsWith("kotlin"))
    println(domain.endsWith(".org"))
    println(domain.indexOf("lang"))
    println(domain.indexOf("swift"))

    println("== splitting ==")
    println("one, two, three".split(", "))
    println("one\ntwo\nthree".lines())
    println("a,b,,c".split(","))
    println("a,b,,c".split(",").filter { it.isNotEmpty() })
    println("a1b2c3".split("1", "2"))

    println("== building ==")
    val hasErrors = true
    val hasWarnings = true
    val isComplete = false
    val status = buildString {
        if (hasErrors) append("Errors found")
        if (hasWarnings) {
            if (isNotEmpty()) append("; ")
            append("Warnings found")
        }
        if (isComplete) {
            if (isNotEmpty()) append("; ")
            append("Completed")
        }
        if (isEmpty()) append("OK")
    }
    println(status)

    val builder = StringBuilder("Hello, Kotlin")
    builder.replace(7, 13, "world")
    println(builder.toString())
    println(String.format("Hello, %s", "Kotlin"))
    println(String.format("%05.2f", 3.14159))

    println("== conversion ==")
    println(10.toString())
    println("10".toInt())
    println("1000000000000".toIntOrNull())
    println(runCatching { "1000000000000".toInt() }.exceptionOrNull()?.let { it::class.simpleName })
    println("true".toBooleanStrict())
    println("yes".toBooleanStrictOrNull())
    println("3.5".toDouble())
    println("abc".toDoubleOrNull())

    println("== the empty and blank distinction ==")
    println("".isEmpty())
    println("   ".isEmpty())
    println("   ".isBlank())
    println("".isBlank())
    println("".isNullOrEmpty())
    val nothing: String? = null
    println(nothing.isNullOrEmpty())
    println(nothing.isNullOrBlank())

    println("== joinToString ==")
    println(listOf("a", "b", "c").joinToString())
    println(listOf("a", "b", "c").joinToString(separator = "", prefix = "<", postfix = ">"))
    println((1..10).joinToString(limit = 3, truncated = "..."))
}
