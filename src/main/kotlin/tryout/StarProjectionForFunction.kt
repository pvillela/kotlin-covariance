package tryout

import java.util.function.Function as JFunction


// See https://kotlinlang.org/docs/reference/generics.html#star-projections

// Sometimes you want to say that you know nothing about the type argument, but still want to use it in a safe way. The safe way here is to define such a projection of the generic type, that every concrete instantiation of that generic type would be a subtype of that projection.
// Kotlin provides so called star-projection syntax for this:
//
//    For Foo<out T : TUpper>, where T is a covariant type parameter with the upper bound TUpper, Foo<*> is equivalent to Foo<out TUpper>. It means that when the T is unknown you can safely read values of TUpper from Foo<*>.
//    For Foo<in T>, where T is a contravariant type parameter, Foo<*> is equivalent to Foo<in Nothing>. It means there is nothing you can write to Foo<*> in a safe way when T is unknown.
//    For Foo<T : TUpper>, where T is an invariant type parameter with the upper bound TUpper, Foo<*> is equivalent to Foo<out TUpper> for reading values and to Foo<in Nothing> for writing values.

//    Function<*, String> means Function<in Nothing, String>
//    Function<Int, *> means Function<Int, out Any?>
//    Function<*, *> means Function<in Nothing, out Any?>

//    Map<K, out V>
//    Map<*, *> means Map<out Any? / in Nothing, out Any?>

/**
 * Explores the star projection for function interfaces.
 */
object StarProjectionForFunction {

    interface Function<in S, out T> {
        fun apply(s: S): T

        companion object {
            operator fun <S, T> invoke(f: (S) -> T): Function<S, T> = object : Function<S, T> {
                override fun apply(s: S): T = f(s)
            }
        }
    }

    // This interface behaves differently than java.util.function.Function.
    // For java.util.function.Function, type parameter Any is accepted where type Any? would
    // be required according to the definition of the star projection.
//    interface JFunction<S, T> {
//        fun apply(s: S): T?
//
//        companion object {
//            operator fun <S, T> invoke(f: (S) -> T?): JFunction<S, T> = object : JFunction<S, T> {
//                override fun apply(s: S): T? = f(s)
//            }
//        }
//    }


    val `Expression to check types` = "" +

            fun(
                    // These types are equivalent
                    x0: Function<*, String>,
                    x1: Function<Nothing, String>,

                    y1: Function<String, String>
            ) {
                val x0x1: Function<*, String> = x1
                val x1x0: Function<Nothing, String> = x0

                val x0y1: Function<*, String> = y1
//                val y1x0: Function<String, String> = x0  // Compilation error
            } +

            fun(
                    // These types are equivalent
                    x0: Function<String, *>,
                    x1: Function<String, Any?>,

                    y1: Function<String, String>
            ) {
                val x0x1: Function<String, *> = x1
                val x1x0: Function<String, Any?> = x0

                val x0y1: Function<String, *> = y1
//                val y1x0: Function<String, String> = x0  // Compilation error
            } +

            fun(
                    // These types are equivalent
                    x0: JFunction<*, String>,
                    x1: JFunction<out Any?, String>,
                    x2: JFunction<out Any, String>,
                    x3: JFunction<in Nothing, String>,

                    y1: JFunction<Any?, String>,
                    y2: JFunction<Any, String>,
                    y3: JFunction<Nothing, String>,
                    y4: JFunction<String, String>
            ) {
                val x0x1: JFunction<*, String> = x1
                val x1x0: JFunction<out Any?, String> = x0
                val x0x2: JFunction<*, String> = x2
                val x2x0: JFunction<out Any, String> = x0
                val x0x3: JFunction<*, String> = x3
                val x3x0: JFunction<in Nothing, String> = x0

                val x0y1: JFunction<*, String> = y1
                val x0y2: JFunction<*, String> = y2
                val x0y3: JFunction<*, String> = y3
                val x0y4: JFunction<*, String> = y4
//                val y1x0: JFunction<Any?, String> = x0  // Compilation error
//                val y2x0: JFunction<Any, String> = x0  // Compilation error
//                val y3x0: JFunction<Nothing, String> = x0  // Compilation error
//                val y4x0: JFunction<String, String> = x0  // Compilation error

                val foo: JFunction<*, String> = JFunction<String, String> { "foo" }
//                val res1: () -> String = { foo.apply("bar") }  // Required Nothing, found String
                val res2: () -> String = { foo.apply(TODO()) }
            } +

            fun(
                    // These types are equivalent
                    x0: JFunction<String, *>,
                    x1: JFunction<String, out Any?>,
                    x2: JFunction<String, out Any>,
                    x3: JFunction<String, in Nothing>,

                    y1: JFunction<String, Any?>,
                    y2: JFunction<String, Any>,
                    y3: JFunction<String, Nothing>,
                    y4: JFunction<String, String>
            ) {
                val x0x1: JFunction<String, *> = x1
                val x1x0: JFunction<String, out Any?> = x0
                val x0x2: JFunction<String, *> = x2
                val x2x0: JFunction<String, out Any> = x0
                val x0x3: JFunction<String, *> = x3
                val x3x0: JFunction<String, in Nothing> = x0

                val x0y1: JFunction<String, *> = y1
                val x0y2: JFunction<String, *> = y2
                val x0y3: JFunction<String, *> = y3
                val x0y4: JFunction<String, *> = y4
//                val y1x0: JFunction<String, Any?> = x0  // Compilation error
//                val y2x0: JFunction<String, Any> = x0  // Compilation error
//                val y3x0: JFunction<String, Nothing> = x0  // Compilation error
//                val y4x0: JFunction<String, String> = x0  // Compilation error

                val foo: JFunction<String, *> = JFunction<String, String> { "foo" }
                val res1: () -> Any? = { foo.apply("bar") }
                val res2: () -> Any? = { foo.apply(TODO()) }
            }
}
