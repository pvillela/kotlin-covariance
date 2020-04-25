package tryout


interface PersistentList<out T> {
    abstract val head: T?
    abstract val tail: PersistentList<T>?
    abstract fun <U> map(f: (T) -> U): PersistentList<U>
    // Can't define polymorphic add method
//    abstract fun add(x: T): PersistentList<T>  // Type parameter T is declared as 'out' but occurs in 'in' position in type T
}


sealed class PersistentLinkedList<out T> : PersistentList<T> {
    override abstract val head: T?
    override abstract val tail: PersistentLinkedList<T>?
    override abstract fun <U> map(f: (T) -> U): PersistentLinkedList<U>
}


data class Cons<out T>(override val head: T, override val tail: PersistentLinkedList<T>)
    : PersistentLinkedList<T>() {
    override fun <U> map(f: (T) -> U): PersistentLinkedList<U> =
            Cons(f(head), tail.map(f))
}


object Nil : PersistentLinkedList<Nothing>() {
    override val head: Nothing? = null
    override val tail: PersistentLinkedList<Nothing>? = null
    override fun <U> map(f: (Nothing) -> U): PersistentLinkedList<U> = this
    override fun toString(): String = "Nil"
}


// Defines add method only for this implementation of PersistentList
fun <T> PersistentLinkedList<T>.add(x: T): PersistentLinkedList<T> =
        Cons(x, this)


fun main(args: Array<String>) {
    val lst: PersistentLinkedList<Int> = Nil.add(1).add(20).add(300).add(4000)
    println(lst)
    val lst1 = lst.add(50000)
    println(lst1)
    val lst2 = lst1.tail
    println(lst2 == lst)
    val lst3 = lst.map { it + 1 }
    println(lst3)
}
