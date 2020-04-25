package tryout

// See https://kotlinlang.org/docs/reference/generics.html#variance

/**
 * Compare with [JCovariance].
 */
object KCovariance {

    interface Source0<T> {
        fun nextT(): T
    }

    fun demo(strs: Source0<String>) {
//        val objects0: Source<Any> = strs // Type mismatch
        val objects1: Source0<out Any> = strs // OK
        val foo: Source0<in String> = strs
        // ...
    }

    interface Source1<out T> {
        fun nextT(): T
    }

    fun demo(strs: Source1<String>) {
        val objects0: Source1<Any> = strs // OK
        val objects1: Source1<out Any> = strs // Projection is redundant
//        val foo: Source1<in String> = strs  // Projection is conflicting with variance of type parameter
        // ...
    }

    interface Comparable0<T> {
        operator fun compareTo(other: T): Int
    }

    fun demo(x: Comparable0<Number>) {
//        val y0: Comparable0<Double> = x // Type mismatch
        val y1: Comparable0<in Double> = x // OK!
        x.compareTo(1.0) // 1.0 has type Double, which is a subtype of Number
    }

    interface Comparable1<in T> {
        operator fun compareTo(other: T): Int
    }

    fun demo(x: Comparable1<Number>) {
        val y0: Comparable1<Double> = x // OK!
        val y1: Comparable1<in Double> = x // Projection is redundant
        x.compareTo(1.0) // 1.0 has type Double, which is a subtype of Number
    }

    // Below see https://kotlinlang.org/docs/reference/generics.html#type-projections

    fun copy0(from: Array<Any>, to: Array<Any>) {
        assert(from.size == to.size)
        for (i in from.indices) {
            to[i] = from[i]
            from[i] = from[i]  // Just to exercise variance: Allowed
        }
    }

    fun copy0a(from: Array<out Any>, to: Array<Any>) {
        assert(from.size == to.size)
        for (i in from.indices) {
            to[i] = from[i]
//            from[i] = from[i]  // Just to exercise variance: Not allowed
        }
    }

    fun copy0b(from: Array<Any>, to: Array<in Any>) {
        assert(from.size == to.size)
        for (i in from.indices) {
            to[i] = from[i]
            from[i] = from[i]  // Just to exercise variance: Allowed
        }
    }

    fun copy0c(from: Array<out Any>, to: Array<in Any>) {
        assert(from.size == to.size)
        for (i in from.indices) {
            to[i] = from[i]
//            from[i] = from[i]  // Just to exercise variance: Not allowed
        }
    }

    fun <S> copy1(from: Array<S>, to: Array<S>) {
        assert(from.size == to.size)
        for (i in from.indices) {
            to[i] = from[i]
            from[i] = from[i]  // Just to exercise variance: Allowed
        }
    }

    fun <S> copy1a(from: Array<out S>, to: Array<S>) {
        assert(from.size == to.size)
        for (i in from.indices) {
            to[i] = from[i]
//            from[i] = from[i]  // Just to exercise variance: Not allowed
        }
    }

    fun <S> copy1b(from: Array<S>, to: Array<in S>) {
        assert(from.size == to.size)
        for (i in from.indices) {
            to[i] = from[i]
            from[i] = from[i]  // Just to exercise variance: Allowed
        }
    }

    fun <S> copy1c(from: Array<out S>, to: Array<in S>) {
        assert(from.size == to.size)
        for (i in from.indices) {
            to[i] = from[i]
//            from[i] = from[i]  // Just to exercise variance: Not allowed
        }
    }

    fun demoArray() {
        val ints: Array<Int> = arrayOf(1, 2, 3)
        val any = Array<Any>(3) { "" }

//        copy0(ints, any)  // Required Array<Any>, found Array<Int>
        copy0a(ints, any)  // OK
//        copy0b(ints, any)  // Required Array<Any>, found Array<Int>
        copy0c(ints, any)  // OK

//        copy1(ints, any)  // type mismatch
        copy1a(ints, any)  // OK
        copy1b(ints, any)  // OK
        copy1c(ints, any)  // OK
    }
}
