package tryout


sealed class Xor<out L, out R> {
    abstract fun <U> map(f: (R) -> U): Xor<L, U>
    open val left: L? get() = null
    open val right: R? get() = null
    fun isLeft(): Boolean = left != null
    fun isRight(): Boolean = right != null
}


data class Right<out L, out R>(private val x: R) : Xor<L, R>() {
    override fun <U> map(f: (R) -> U) = Right<L, U>(f(x))
    override val right: R? = x
}


data class Left<out L, out R>(private val x: L) : Xor<L, R>() {
    override fun <U> map(f: (R) -> U) = Left<L, U>(x)
    override val left: L? = x
}


fun main(args: Array<String>) {
    val right: Xor<String, Int> = Right(1)
    val left: Xor<String, Int> = Left("abc")
    val f = { x: Int -> x + 1}
    println(right.right)
    println(right.map(f))
    println(left.left)
    println(left.map(f))
}
