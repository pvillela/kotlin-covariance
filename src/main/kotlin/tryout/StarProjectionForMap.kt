package tryout

import kotlin.reflect.full.allSupertypes

// See https://kotlinlang.org/docs/reference/generics.html#star-projections

/**
 * Explores the star projection for Map and similar interfaces.
 */
object StarProjectionForMap {

    fun Any.objectId() = System.identityHashCode(this)

    interface MyMap<K, out V> {
        operator fun get(key: K): V?
    }

    class MyMapImpl<K, out V>(vararg private val associations: Pair<K, V>) : MyMap<K, V> {
        override fun get(key: K): V? {
            println("Executed MyMapImpl.get: this=$this, objectId()=${this.objectId()}, key=$key")
            return associations.toList().findLast { (k, _) -> k == key }?.second
        }
    }

    // Similar to extension function for Map interface. When MyMap<S, V> is the target and K is the
    // type of key, S must be a subtype of K. In other words, K must be a supertype of S.
    operator fun <K, V> MyMap<out K, V>.get(key: K): V? {
        println("Executed MyMap's extension get: this=$this, objectId()=${this.objectId()}, key=$key")
        val value = @Suppress("UNCHECKED_CAST") (this as MyMap<K, V>).get(key)
        return value
    }

    // Shadowing Map's get extension function in Standard Library.
    operator fun <K, V> Map<out K, V>.get(key: K): V? {
        println("Executed this file's extension get: this=$this, objectId()=${this.objectId()}, key=$key")
        val value = @Suppress("UNCHECKED_CAST") (this as Map<K, V>).get(key)
        return value
    }

    /**
     * Class intended to break Map<*, *> extension get function.
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

    /**
     * Class to break MyMap<*, *> extension get function.
     */
    class TrickyMyMap : MyMap<Int, Int> {

        override fun get(key: Int): Int? {
            println("Executed TrickyMyMap.get for key=$key")
            val value = key * 10
            return if (key in setOf(1, 2)) value
            else null
        }
    }


    // Interfaces to demonstrate type matching with extension get function
    interface Foo
    interface Bar : Foo


    val `Expression to check types` = "" +

            fun(
                    // These types are equivalent
                    x0: Map<*, String>,
                    x1: Map<out Any?, String>,
                    x2: Map<in Nothing, String>,

                    y1: Map<Any?, String>,
                    y2: Map<Nothing, String>,
                    y3: Map<String, String>
            ) {
                val x0x1: Map<*, String> = x1
                val x1x0: Map<out Any?, String> = x0
                val x0x2: Map<*, String> = x2
                val x2x0: Map<out Any?, String> = x0

                val x0y1: Map<*, String> = y1
                val x0y2: Map<*, String> = y2
                val x0y3: Map<*, String> = y3
//                val y1x0: Map<Any?, String> = x0  // Compilation error
//                val y2x0: Map<Nothing, String> = x0  // Compilation error
//                val y3x0: Map<String, String> = x0  // Compilation error
            } +

            fun(
                    // These types are equivalent
                    x0: MyMap<*, String>,
                    x1: MyMap<out Any?, String>,
                    x2: MyMap<in Nothing, String>,

                    y1: MyMap<Any?, String>,
                    y2: MyMap<Nothing, String>,
                    y3: MyMap<String, String>
            ) {
                val x0x1: MyMap<*, String> = x1
                val x1x0: MyMap<out Any?, String> = x0
                val x0x2: MyMap<*, String> = x2
                val x2x0: MyMap<out Any?, String> = x0

                val x0y1: MyMap<*, String> = y1
                val x0y2: MyMap<*, String> = y2
                val x0y3: MyMap<*, String> = y3
//                val y1x0: MyMap<Any?, String> = x0  // Compilation error
//                val y2x0: MyMap<Nothing, String> = x0  // Compilation error
//                val y3x0: MyMap<String, String> = x0  // Compilation error
            } +

            fun(
                    // These types are equivalent
                    x0: Map<*, *>,
                    x1: Map<out Any?, Any?>,
                    x2: Map<in Nothing, Any?>,

                    y1: Map<Any?, String>,
                    y2: Map<Nothing, String>,
                    y3: Map<String, String>
            ) {
                val x0x1: Map<*, *> = x1
                val x1x0: Map<out Any?, Any?> = x0
                val x0x2: Map<*, *> = x2
                val x2x0: Map<out Any?, Any?> = x0

                val x0y1: Map<*, *> = y1
                val x0y2: Map<*, *> = y2
                val x0y3: Map<*, *> = y3
//                val y1x0: Map<Any?, String> = x0  // Compilation error
//                val y2x0: Map<Nothing, String> = x0  // Compilation error
//                val y3x0: Map<String, String> = x0  // Compilation error
            } +

            fun(
                    stringMap: Map<String, String>,
                    starMap: Map<*, String>,
                    starStarMap: Map<*, *>,
                    fooMap: Map<Foo, String>,
                    barMap: Map<Bar, String>,
                    foo: Foo,
                    bar: Bar
            ) {
                run {
                    // Binds to interface get method
                    val res1 = stringMap.get("a")

                    // Binds to extension function. The condition String extends Any is satisfied.
                    val res2 = stringMap.get(Any())

                    // Binds to extension function. The condition String extends Int is not satisfied
                    // and they are not hierarchically related.
//                    val res3 = stringMap.get(42)

                    // Binds to extension function. The condition String extends Any is satisfied.
                    val res4 = stringMap.get(42 as Any)
                }

                run {
                    // Map<*, String> is equivalent to Map<out Any?, String> or Map<in Nothing, String>.
                    // When binding to the extension function, the form Map<out Any?> makes sense
                    // given the signature of the extension function.

                    // Binds to extension function. The condition Any? extends String is not satisfied
                    // but they are hierarchically related, so the type parameter
                    // of get is widened to Any?.
                    val res = starMap.get("a")

                    // Binds to extension function. The condition Any? extends Any is not satisfied
                    // but they are hierarchically related, so the type parameter
                    // of get is widened to Any?.
                    val res1 = starMap.get(Any())

                    // Binds to extension function. The condition Any? extends Int is not satisfied
                    // but they are hierarchically related, so the type parameter
                    // of get is widened to Any?.
                    val res2 = starMap.get(42)

                    // Binds to extension function. The condition Any? extends Any is not satisfied
                    // but they are hierarchically related, so the type parameter
                    // of get is widened to Any?.
                    val res3 = starMap.get(42 as Any)
                }

                run {
                    // There are no compilation errors as Map<*, *> is Map<out Any?, Any?>, so the
                    // situation is similar to that of starMap above.

                    val res1 = starStarMap.get("a")
                    val res2 = starStarMap.get(Any())
                    val res3 = starStarMap.get(42)
                    val res4 = starStarMap.get(42 as Any)
                }

                run {
                    // Binds to extension function. The condition Foo extends String is not satisfied
                    // and they are not hierarchically related. This type checking failure is
                    // engendered by the funky annotations in the Kotlin library's get extension
                    // function.
//                    val res1 = fooMap.get("a")

                    // Binds to extension function. The condition Foo extends Any is satisfied.
                    val res2 = fooMap.get(Any())

                    // Binds to extension function. The condition Foo extends Int is not satisfied
                    // and they are not hierarchically related. This type checking failure is
                    // engendered by the funky annotations in the Kotlin library's get extension
                    // function.
//                    val res3 = fooMap.get(42)

                    // Binds to extension function. The condition Foo extends Any is satisfied.
                    val res4 = fooMap.get(42 as Any)

                    // Binds to extension function. The condition Foo extends Bar is not satisfied
                    // but they are hierarchically related as Bar extends Foo, so the type parameter
                    // of get is widened to Foo.
                    val res5 = fooMap.get(bar)
                }

                run {
                    // Binds to extension function. The condition Bar extends Foo is satisfied.
                    val res5 = barMap.get(foo)
                }
            } +

            fun(
                    stringMyMap: MyMap<String, String>,
                    starMyMap: MyMap<*, String>,
                    fooMyMap: MyMap<Foo, String>,
                    barMyMap: MyMap<Bar, String>,
                    foo: Foo,
                    bar: Bar
            ) {
                // The story for MyMap is similar to that for Map, with one key difference: there
                // are no compilation errors when using the extension get function because the
                // implementation above does not include the funky annotations in the Map get
                // extension function in the Kotlin standard library. Thus, the type parameter
                // of the extension get function gets widened as needed, regardless of whether
                // the first type parameter of the MyMap class is hierarchically related to it or not.

                run {
                    val res1 = stringMyMap.get("a")
                    val res2 = stringMyMap.get(Any())
                    val res3 = stringMyMap.get(42)
                    val res4 = stringMyMap.get(42 as Any)
                }

                run {
                    val res = starMyMap.get("a")
                    val res1 = starMyMap.get(Any())
                    val res2 = starMyMap.get(42)
                    val res3 = starMyMap.get(42 as Any)
                }

                run {
                    val res1 = fooMyMap.get("a")
                    val res2 = fooMyMap.get(Any())
                    val res3 = fooMyMap.get(42)
                    val res4 = fooMyMap.get(42 as Any)
                    val res5 = fooMyMap.get(bar)
                }

                run {
                    val res5 = barMyMap.get(foo)
                }
            }

    @JvmStatic
    fun main(args: Array<String>) {

        // MyMap examples

        val stringMyMap: MyMap<String, String> = MyMapImpl("a" to "1", "b" to "2")
        val starMyMap: MyMap<*, String> = stringMyMap
        val foo: Foo = object : Foo {}
        val bar: Bar = object : Bar {}
        val fooMyMap: MyMap<Foo, String> = MyMapImpl(foo to "foo")
        val barMyMap: MyMap<Bar, String> = MyMapImpl(bar to "bar")

        fun <K, V> printAll(header: String, myMap: MyMap<K, V>) {
            println("\n$header")
            run {
                val res = myMap.get("a")
                println(res)
            }
            run {
                val res = myMap.get(Any())
                println(res)
            }
            run {
                val res = myMap.get(42)
                println(res)
            }
            run {
                val res = myMap.get(42 as Any)
                println(res)
            }
            run {
                val res = myMap.get(foo)
                println(res)
            }
            run {
                val res = myMap.get(bar)
                println(res)
            }
        }

        printAll("*** stringMyMap ***", stringMyMap)
        printAll("*** starMyMap ***", starMyMap)
        printAll("*** fooMyMap ***", fooMyMap)
        printAll("*** barMyMap ***", barMyMap)

        // Map<*, *> example intended to throw ClassCastException for map["a"] but it doesn't.
        // See explanation here: https://discuss.kotlinlang.org/t/map-get-not-calling-get-method-on-concrete-map-class/17337/2?u=pvillela
        
        run {
            println("\n*** TrickyMap as Map<*, *> ***")
            val map: Map<*, *> = TrickyMap()
            println(map[1])
            println(map[2])
            println(map[3])
            println(map["a"])
        }
        
        run {
            println("\n*** TrickyMap as Map<Int, Int> ***")
            val map: Map<Int, Int> = TrickyMap()
            println(map[1])
            println(map[2])
            println(map[3])

            // Produces compilation error when Map's extension get function is not shadowed by
            // implementation in this file.
            println(map["a"])
        }

        // MyMap<*, *> example that doesn't break
        run {
            println("\n*** MyMapImpl ***")
            val starStarMyMap: MyMap<*, *> = MyMapImpl(1 to 10, 2 to 20)
            println(starStarMyMap[1])
            println(starStarMyMap[2])
            println(starStarMyMap[3])
            println(starStarMyMap["a"])
        }

        // MyMap<*, *> breaking example
        run {
            println("\n*** TrickyMyMap ***")
            val starStarMyMap: MyMap<*, *> = TrickyMyMap()
            println(starStarMyMap[1])
            println(starStarMyMap[2])
            println(starStarMyMap[3])
            println(starStarMyMap["a"])
        }
    }
}
