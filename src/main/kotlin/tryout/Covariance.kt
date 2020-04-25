package tryout


/**
 * How to add to an immutable type functions that have the type parameter in `in` position by
 * using extension functions.
 */
object Covariance {

    class Foo<out T>(val x: T) {
        fun get(): T = x
//    fun puta(y: T): Foo<T> = Foo(y)  // Type parameter T is declared as 'out' but occurs in 'in' position in type T
    }

    fun <T> Foo<T>.put(y: T): Foo<T> = Foo(y)

    @JvmStatic
    fun main(args: Array<String>) {
        val foo = Foo(1)
        val bar: Foo<Int> = foo.put(2)
        val baz: Foo<Any> = bar
        println(foo.get())
        println(bar.get())
        println(baz.get())
    }
}
