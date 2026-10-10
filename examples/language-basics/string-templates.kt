// Examples for /language-basics/printing-data/string-templates
// Adapted from https://kotlinlang.org/docs/strings.html#string-templates
package examples.languagebasics

data class Cart(val items: List<String>, val total: Double)

class Opaque(val id: Int)

fun main() {
    println("== a simple name ==")
    val name = "Kotlin"
    println("Hello, $name")

    println("== an expression ==")
    val text = "abc"
    println("The length of $text is ${text.length}")
    println("${name}Lang")
    println("2 + 2 = ${2 + 2}")
    println("upper: ${name.uppercase()}")

    println("== when the braces are required ==")
    val count = 3
    println("$count items")
    println("${count}rd")
    val cart = Cart(listOf("apple", "pear"), 4.5)
    println("first item: ${cart.items[0]}")
    println("total: ${cart.total}")

    println("== what $ can attach to without braces ==")
    println("$name.length is not the length: $name.length")
    println("with braces: ${name.length}")

    println("== a template calls toString() ==")
    println("cart: $cart")
    println("opaque: ${Opaque(1).toString().substringBefore('@')}")
    println("list: ${listOf(1, 2)}")
    println("array: ${arrayOf(1, 2).contentToString()}")

    println("== nulls become the text null ==")
    val absent: String? = null
    println("Hello, $absent")
    println("Hello, ${absent ?: "Kotlin"}")

    println("== a literal dollar sign ==")
    println("price: \$9.99")
    val dollar = '\$'
    println("char: $dollar code: ${dollar.code}")
    println("also: ${'\$'}10")

    println("== templates work in multiline strings ==")
    println(
        """
        name:  $name
        upper: ${name.uppercase()}
        """.trimIndent()
    )

    println("== a quoted string inside a template ==")
    val test = "${"test".uppercase()}"
    println(test)
    println(
        """
        Result: ${"OK".lowercase()}
        """.trimIndent()
    )

    println("== multi-dollar interpolation ==")
    val schema = $$"""
    {
      "$schema": "https://json-schema.org/draft/2020-12/schema",
      "title": "$${name}",
      "type": "object"
    }
    """.trimIndent()
    println(schema)

    println("== a raw string cannot escape the dollar at all ==")
    // "\$schema" inside """ ... """ is a literal backslash followed by
    // interpolation of `schema` -- escapes are not processed in a raw string.
    println(
        """
        {
          "\$name": leading backslash, then the value of name
        }
        """.trimIndent()
    )

    println("== so without multi-dollar you need ${'\$'} ==")
    println(
        """
        {
          "${'\$'}schema": "https://json-schema.org/draft/2020-12/schema",
          "title": "$name"
        }
        """.trimIndent()
    )

    println("== templates versus concatenation ==")
    println("Hello, " + name + "! You have " + cart.items.size + " items.")
    println("Hello, $name! You have ${cart.items.size} items.")

    println("== a newline inside a template expression ==")
    println("a${'\n'}b")

    println("== escaping in a template is still string escaping ==")
    println("tab:\tdone")
    println("quote: \"quoted\"")
}
