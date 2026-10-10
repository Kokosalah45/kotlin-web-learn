// Examples for /language-basics/sequences
// Adapted from https://kotlinlang.org/docs/sequences.html
package examples.languagebasics

fun main() {
    println("== generateSequence ==")
    val oddNumbers = generateSequence(1) { it + 2 }
    println(oddNumbers.take(5).toList())

    println("== a finite generateSequence stops at null ==")
    val oddNumbersLessThan10 = generateSequence(1) { if (it < 8) it + 2 else null }
    println(oddNumbersLessThan10.count())

    println("== sequence { } with yield / yieldAll ==")
    val built = sequence {
        yield(1)
        yieldAll(listOf(3, 5))
        yieldAll(generateSequence(7) { it + 2 })
    }
    println(built.take(5).toList())

    println("== eager: Iterable ==")
    val words = "The quick brown fox jumps over the lazy dog".split(" ")
    val lengthsList = words.filter { println("filter: $it"); it.length > 3 }
        .map { println("length: ${it.length}"); it.length }
        .take(4)
    println("Lengths of first 4 words longer than 3 chars:")
    println(lengthsList)

    println("== lazy: Sequence ==")
    val wordsSequence = words.asSequence()
    val lengthsSequence = wordsSequence.filter { println("filter: $it"); it.length > 3 }
        .map { println("length: ${it.length}"); it.length }
        .take(4)
    println("Lengths of first 4 words longer than 3 chars")
    println(lengthsSequence.toList())

    println("== nothing runs without a terminal operation ==")
    val neverRun = words.asSequence().map { println("mapping $it"); it.length }
    println("built the pipeline, nothing printed above")
    println("now terminal: ${neverRun.first()}")

    println("== counting the work ==")
    var eagerOps = 0
    (1..1_000_000).filter { eagerOps++; it % 7 == 0 }.first()
    var lazyOps = 0
    (1..1_000_000).asSequence().filter { lazyOps++; it % 7 == 0 }.first()
    println("eager predicate calls: $eagerOps")
    println("lazy predicate calls:  $lazyOps")
}
