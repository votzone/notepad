package wq.ktrs.tmp.ktrevise

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/2
 * Modification History:
 * Why & What modified:
 */
class fieldtest {
}

class User(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println("""
                Address was changed for $name:
                "$field" -> "$value".""".trimIndent())
            field = value
        }
}

data class Person(val name: String, val age: Int)
fun main(args: Array<String>) {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    val names = people.joinToString(separator = " ",
            transform = { p: Person -> p.name })
    println(names)
}