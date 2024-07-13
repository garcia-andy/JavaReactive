package org.jreactive.Types;
import java.util.function.Function;

import org.jreactive.Utils.Generator;

public class UseSignal {

    public static <T> Signal<T> useSignal(T init)
    { return new Signal<T>(init); }

    public static <T>Signal<T> useSignal(Generator<T> genInit)
    { return useSignal( genInit.run() ); }

    public static <T>Signal<T> useSignal()
    { return useSignal( () -> null ); }

    public static <T>State<Object> useComputed(
        Signal<T> subject, 
        Function<State<T>,Object> computation
    ){ return subject.computed(computation); }

    
}
