// Examples for /language-basics/basic-types/characters
// Adapted from https://kotlinlang.org/docs/characters.html
package examples.languagebasics

fun main() {
    println("== declaring ==")
    val letter: Char = 'a'
    val digit = '1'
    val symbol = '!'
    val space = ' '
    val separator = ':'
    println("$letter $digit $symbol [$space] $separator")
    println(digit::class.simpleName)

    println("== escape sequences ==")
    println("tab:[\t] newline below:")
    println("line1\nline2")
    println("quote \" apostrophe ' backslash \\ dollar \$")
    val newLine = '\n'
    val dollar = '\$'
    val backslash = '\\'
    println("codes: ${newLine.code} ${dollar.code} ${backslash.code}")

    println("== Unicode escapes ==")
    val unicodeNumber = '\u0031'
    println(unicodeNumber)
    println(unicodeNumber == '1')
    val eAcute = '\u00E9'
    println(eAcute.code)
    println(eAcute == 'e')
    println('\uFFFF'.code)
    println('\u0000'.code)

    println("== a Char is a UTF-16 code unit, not a character ==")
    // U+1F966 BROCCOLI, outside the Basic Multilingual Plane
    val emoji = "\uD83E\uDD66"
    println(emoji.length)
    println(emoji[0].code)
    println(emoji[1].code)
    println(emoji[0].isHighSurrogate())
    println(emoji[1].isLowSurrogate())
    println(emoji.codePointCount(0, emoji.length))
    println(emoji.codePointAt(0))
    println(emoji.codePointAt(0) > 0xFFFF)

    println("== comparison is by Unicode value ==")
    println('a' < 'b')
    println('c' > 'd')
    println('A' == 'a')
    println('A' == 'A')
    println('A' < 'a')
    println('Z' < 'a')

    println("== inspection and case ==")
    val myChar = 'A'
    println(myChar.isDigit())
    println(myChar.isUpperCase())
    println(myChar.isLetter())
    println(myChar.isWhitespace())
    println(myChar.lowercaseChar())
    println('1'.isDigit())
    // U+00DF LATIN SMALL LETTER SHARP S
    println('\u00DF'.uppercase())
    println('\u00DF'.uppercase().length)
    println('\u00DF'.uppercaseChar().code)
    println('\u00DF'.code)

    println("== arithmetic on Chars ==")
    val a = 'a'
    println(a + 1)
    println(a + 2)
    println(a - 32)
    println('z' - 'a')
    println(('a'..'e').toList())

    println("== increment and decrement ==")
    var c = 'A'
    c += 10
    println(c)
    println(++c)
    println(c++)
    println(c)
    println(--c)
    println(c--)
    println(c)

    println("== conversion ==")
    val upperA = 'A'
    println(upperA.code)
    println(65.toChar())
    val seven = '7'
    println(seven.digitToInt())
    println(seven.code)
    println('f'.digitToInt(16))
    println('x'.digitToIntOrNull())
    println(runCatching { 'x'.digitToInt() }.exceptionOrNull()?.let { it::class.simpleName })

    println("== the digit trap ==")
    println('7'.code)
    println('7'.digitToInt())
    println('7'.toString().toInt())

    println("== nullable Char ==")
    val maybeAbsent: Char? = null
    println(maybeAbsent)
    println(maybeAbsent?.code)
}
