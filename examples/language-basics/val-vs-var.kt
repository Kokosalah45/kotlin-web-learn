// Examples for /language-basics/variables/val-vs-var
// Adapted from https://kotlinlang.org/docs/basic-syntax.html#variables
// and https://kotlinlang.org/docs/properties.html
package examples.languagebasics

class Rect(val width: Int, val height: Int) {
    // A val with a custom getter: recomputed on every read, no stored value.
    val area: Int
        get() = width * height
}

class Clock {
    var reads = 0
    val now: Int
        get() {
            reads++
            return reads * 10
        }
}

class BankAccount(initialBalance: Int) {
    var balance: Int = initialBalance
        // Only the class can modify the balance
        private set

    fun deposit(amount: Int) {
        if (amount > 0) balance += amount
    }

    fun withdraw(amount: Int) {
        if (amount > 0 && amount <= balance) balance -= amount
    }
}

class Scoreboard {
    var score: Int = 0
        set(value) {
            field = value
            println("Score updated to $field")
        }
}

class UserDirectory {
    private val _users = mutableListOf("sarah", "mike", "emma")

    val users: List<String>
        get() = _users.sorted()

    fun addUser(username: String) {
        _users.add(username)
    }
}

fun main() {
    println("== the difference ==")
    val readOnly = 5
    var reassignable = 5
    reassignable += 1
    println("$readOnly / $reassignable")
    // readOnly = 6 // Error: val cannot be reassigned

    println("== val is not deep immutability ==")
    val list = mutableListOf(1, 2, 3)
    list.add(4)
    list[0] = 99
    println(list)
    // list = mutableListOf() // Error: val cannot be reassigned

    println("== read-only type versus read-only reference ==")
    var reassignableList: List<Int> = listOf(1, 2, 3)
    reassignableList = listOf(4, 5)
    println(reassignableList)
    // reassignableList.add(6) // Error: no add() on List

    println("== a val with a custom getter is recomputed ==")
    val rect = Rect(3, 4)
    println("Width=${rect.width}, height=${rect.height}, area=${rect.area}")
    val clock = Clock()
    println(clock.now)
    println(clock.now)
    println(clock.now)
    println("reads = ${clock.reads}")

    println("== a private setter ==")
    val account = BankAccount(100)
    println("Initial balance: ${account.balance}")
    account.deposit(50)
    println("After deposit: ${account.balance}")
    account.withdraw(70)
    println("After withdrawal: ${account.balance}")
    // account.balance = 1000 // Error: cannot assign, setter is private

    println("== a custom setter and the backing field ==")
    val board = Scoreboard()
    board.score = 10
    board.score = 20
    println(board.score)

    println("== the backing-property pattern ==")
    val directory = UserDirectory()
    directory.addUser("alex")
    println(directory.users)

    println("== smart casts need a val ==")
    val maybe: Any = "text"
    if (maybe is String) println(maybe.length)

    var reassignableAny: Any = "text"
    if (reassignableAny is String) println(reassignableAny.length)
    reassignableAny = 1
    println(reassignableAny)

    println("== which to reach for ==")
    var total = 0
    for (n in 1..5) total += n
    println(total)
    println((1..5).sum())
}
