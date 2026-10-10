// Examples for /functions/local-functions
// Adapted from https://kotlinlang.org/docs/functions.html#local-functions
package examples.functions

class Friend(val name: String) {
    val friends = mutableListOf<Friend>()
}

class SocialGraph(val people: List<Friend>)

// The local dfs() hides the recursion and keeps `visited` out of the public signature.
fun dfs(graph: SocialGraph) {
    fun dfs(current: Friend, visited: MutableSet<Friend>) {
        if (!visited.add(current)) return
        println("Visited ${current.name}")
        for (friend in current.friends)
            dfs(friend, visited)
    }
    dfs(graph.people[0], HashSet())
}

// A local function closes over the outer function's locals, so `visited`
// does not have to be threaded through the parameter list at all.
fun dfsWithClosure(graph: SocialGraph) {
    val visited = HashSet<Friend>()
    fun dfs(current: Friend) {
        if (!visited.add(current)) return
        println("Visited ${current.name}")
        for (friend in current.friends)
            dfs(friend)
    }
    dfs(graph.people[0])
}

fun buildGraph(): SocialGraph {
    val alice = Friend("Alice")
    val bob = Friend("Bob")
    val charlie = Friend("Charlie")
    alice.friends += bob
    bob.friends += charlie
    charlie.friends += alice
    return SocialGraph(listOf(alice, bob, charlie))
}

fun main() {
    println("== a local function ==")
    dfs(buildGraph())

    println("== closing over the outer local ==")
    dfsWithClosure(buildGraph())

    println("== captured vars are writable ==")
    var calls = 0
    fun tick(): Int {
        calls++
        return calls
    }
    println(tick())
    println(tick())
    println("calls = $calls")

    println("== a local function can shadow a top-level one ==")
    fun mergeStrings(vararg strings: String): String = strings.joinToString("+")
    println(mergeStrings("a", "b", "c"))
    println(examples.functions.mergeStrings("a", "b", "c"))

    println("== a local function can be tailrec ==")
    tailrec fun countDownLocal(n: Int): Int = if (n == 0) 0 else countDownLocal(n - 1)
    println(countDownLocal(5_000_000))

    println("== local extensions and local classes ==")
    fun String.shout() = "${uppercase()}!"
    println("hello".shout())
    class Box(val n: Int) {
        fun doubled() = n * 2
    }
    println(Box(21).doubled())
}
