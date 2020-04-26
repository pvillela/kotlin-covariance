package tryout


/**
 * Class intended to break Map<*, *> extension get function.
 * See https://discuss.kotlinlang.org/t/map-get-not-calling-get-method-on-concrete-map-class/17337
 */
class TrickyMap : Map<Int, Int> {

    override fun get(key: Int): Int? {
        println("Executed TrickyMap.get for key=$key")
        val value = key * 10
        return if (key in setOf(1, 2)) value
        else null
    }

    override val entries: Set<Map.Entry<Int, Int>>
        get() = TODO("Not yet implemented")
    override val keys: Set<Int>
        get() = TODO("Not yet implemented")
    override val size: Int
        get() = TODO("Not yet implemented")
    override val values: Collection<Int>
        get() = TODO("Not yet implemented")
    override fun containsKey(key: Int): Boolean =
            TODO("Not yet implemented")
    override fun containsValue(value: Int): Boolean =
            TODO("Not yet implemented")
    override fun isEmpty(): Boolean =
            TODO("Not yet implemented")
}

fun main() {
    val map: Map<*, *> = TrickyMap()
    println(map[1])  // prints 10
    println(map[3])  // prints null
    println(map["a"])  // should throw a ClassCastException but it prints null
}
