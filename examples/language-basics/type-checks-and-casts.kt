// Examples for /language-basics/data-types/type-checks-and-casts
// Adapted from https://kotlinlang.org/docs/typecasts.html
package examples.languagebasics

interface Pet {
    val petName: String
    fun speak()
}

class PetDog(override val petName: String) : Pet {
    override fun speak() = println("$petName says: Woof!")
    fun bark() = println("BARK!")
}

class PetCat(override val petName: String) : Pet {
    override fun speak() = println("$petName says: Meow!")
}

fun handleAnimal(animal: Pet) {
    println("Handling animal: ${animal.petName}")
    animal.speak()
    if (animal is PetDog) {
        println("Special care instructions: This is a dog.")
    } else if (animal is PetCat) {
        println("Special care instructions: This is a cat.")
    }
}

fun logMessage(data: Any) {
    // data is automatically cast to String
    if (data is String) {
        println("Received text: ${data.length} characters")
    }
}

fun logMessageEarlyReturn(data: Any) {
    if (data !is String) return
    println("Received text: ${data.length} characters")
}

fun processInput(data: Any) {
    when (data) {
        is Int -> println("Log: Assigned new ID ${data + 1}")
        is String -> println("Log: Received message \"$data\"")
        is IntArray -> println("Log: Processed scores, total = ${data.sum()}")
        else -> println("Log: unrecognised input")
    }
}

sealed interface Status
data class Ok(val currentRoom: String) : Status
data object Failed : Status

class RobotVacuum(val rooms: List<String>) {
    var index = 0

    fun status(): Status = if (index < rooms.size) Ok(rooms[index]) else Failed

    fun clean(): Status {
        println("Finished cleaning ${rooms[index]}")
        index++
        return status()
    }
}

interface Bird {
    fun fly()
}

interface Fish {
    fun swim()
}

class FlyingFish : Bird, Fish {
    override fun fly() = println("gliding")
    override fun swim() = println("swimming")
}

fun describeAnimal(animal: Any) {
    // Infers the intersection of Bird and Fish
    if (animal is Bird && animal is Fish) {
        animal.fly()
        animal.swim()
    }
}

interface Signal {
    fun signal()
}

interface Postponed : Signal
interface Declined : Signal

class PostponedSignal : Postponed {
    override fun signal() = println("postponed")
}

fun signalCheck(signalStatus: Any) {
    if (signalStatus is Postponed || signalStatus is Declined) {
        // smart cast to the common supertype Signal
        signalStatus.signal()
    }
}

class MutableHolder {
    var value: Any = "text"
}

fun testString() {
    var stringInput: String? = null
    stringInput = ""
    try {
        println(stringInput.length)
        stringInput = null
        if (2 > 1) throw Exception()
        stringInput = ""
    } catch (exception: Exception) {
        println(stringInput?.length)
    }
}

fun main() {
    println("== is and !is ==")
    val input: Any = "Hello, Kotlin"
    if (input is String) println("Message length: ${input.length}")
    if (input !is String) println("Input is not a valid message")
    else println("Processing message: ${input.length} characters")

    println("== checking a subtype ==")
    for (pet in listOf<Pet>(PetDog("Buddy"), PetCat("Whiskers"))) {
        handleAnimal(pet)
        println("---")
    }

    println("== smart cast from a positive check ==")
    logMessage("Server started")
    logMessage(404)

    println("== smart cast from a negative check with return ==")
    logMessageEarlyReturn("User signed in")
    logMessageEarlyReturn(true)

    println("== smart cast in when ==")
    processInput(1001)
    processInput("System rebooted")
    processInput(intArrayOf(10, 20, 30))

    println("== smart cast in while ==")
    val robo = RobotVacuum(listOf("Living Room", "Kitchen"))
    var status: Status = robo.status()
    while (status is Ok) {
        println("Cleaning ${status.currentRoom}...")
        status = robo.clean()
    }

    println("== smart cast through a Boolean variable ==")
    val animal: Any = PetDog("Rex")
    val isDog = animal is PetDog
    if (isDog) animal.bark()

    println("== smart cast across && and || ==")
    val x: Any = "abc"
    if (x is String && x.length > 0) println(x.length)
    describeAnimal(FlyingFish())
    signalCheck(PostponedSignal())

    println("== a var property never smart casts ==")
    val holder = MutableHolder()
    if (holder.value is String) {
        println((holder.value as String).length)
    }

    println("== smart cast information reaches catch ==")
    testString()

    println("== as throws, as? returns null ==")
    val rawInput: Any = "user-1234"
    println(rawInput as String)
    println(runCatching { rawInput as Int }.exceptionOrNull()?.let { it::class.simpleName })
    println(rawInput as? String)
    println(rawInput as? Int)

    println("== as with a nullable target still throws ==")
    val config: Map<String, Any?> = mapOf(
        "username" to "kodee",
        "alias" to null,
        "loginAttempts" to 3
    )
    println(config["username"] as String?)
    println(config["alias"] as String?)
    println(runCatching { config["loginAttempts"] as String? }.exceptionOrNull()?.let { it::class.simpleName })
    println(config["loginAttempts"] as? String)

    println("== upcasting needs no operator ==")
    val dog = PetDog("Buddy")
    val asPet: Pet = dog
    asPet.speak()

    println("== downcasting does ==")
    val pet: Pet = PetDog("Rex")
    val maybeDog: PetDog? = pet as? PetDog
    maybeDog?.bark()
    val maybeCat: PetCat? = pet as? PetCat
    println(maybeCat)

    println("== generic type arguments are erased ==")
    val strings: Any = listOf("a", "b")
    println(strings is List<*>)
    @Suppress("UNCHECKED_CAST")
    val asInts = strings as List<Int>
    println(runCatching { asInts.first() + 1 }.exceptionOrNull()?.let { it::class.simpleName })
}
