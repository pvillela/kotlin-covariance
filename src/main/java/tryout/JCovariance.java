package tryout;

// See https://kotlinlang.org/docs/reference/generics.html#variance

/**
 * @see KCovariance
 */
class JCovariance {

    interface Source<T> {
        T nextT();
    }

    void demo(Source<String> strs) {
//        Source<Object> objects = strs; // !!! Not allowed in Java
        Source<? extends Object> objects = strs; // OK
        Source<? super String> foo = strs;
        // ...
    }
}
