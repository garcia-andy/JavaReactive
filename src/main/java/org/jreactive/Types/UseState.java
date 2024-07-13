package org.jreactive.Types;

import java.util.function.Function;

import org.jreactive.Implementations.Subject;
import org.jreactive.Utils.Generator;

public class UseState {

    /**
    * The implemented Creation default all
    * @return The State Object
    */
    public static <T> State<T> useState()
    { return useState((T)null); }

    /**
    * The implemented Creation
    * @param init the init data for the State
    * @return The State Object
    */
    public static <T>State<T> useState(T init)
    { return new State<>(init); }

    /**
    * The implemented Creation
    * @param init the init data from the Generator for the State
    * @return The State Object
    */
    public static <T>State<T> useState(Generator<T> init)
    { return useState(init.run()); }

    /**
    * The fullest implemented UseState
    * @param init the init data from the Generator for the State
    * @param obj Observer to subscribe to state
    * @return The State Object
    */
    public static <T,O extends Subject<T>>State<T> useState(Generator<T> init, O obj){
        State<T> state = useState(init);
        state.subscribe(obj);
        return state;
    }

}
