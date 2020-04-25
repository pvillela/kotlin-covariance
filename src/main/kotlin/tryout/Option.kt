package tryout


sealed class Option<out T> {
    abstract fun <U> map(f: (T) -> U): Option<U>
    abstract fun get(): T?
}


data class Some<out T>(private val x: T) : Option<T>() {
    override fun <U> map(f: (T) -> U) = Some(f(x))
    override fun get(): T? = x
}


object None : Option<Nothing>() {
    override fun <U> map(f: (Nothing) -> U) = this
    override fun get(): Nothing? = null
    override fun toString() = "None"
}


fun main(args: Array<String>) {
    val some: Option<Int> = Some(1)
    val none: Option<Int> = None
    val f = { x: Int -> x + 1}
    println(some.get())
    println(some.map(f))
    println(none.get())
    println(none.map(f))
}
