package org.jreactive.Types;

import org.jreactive.Implementations.Observable;
import org.jreactive.Implementations.Subject;
import org.jreactive.Utils.Cache;
import org.jreactive.Utils.Generator;

import java.util.function.Function;

public class State<T> extends Observable<T> implements Subject<T> {
    private T value;
    
    public State(Generator<T> init)
    { this(init.run()); }

    public State(T initValue){
        value = initValue;
    }

    @Override
    public synchronized void update(T ctx) 
    { value = ctx; }

    @Override
    public synchronized void update()
    { update(value); }
    
    public synchronized T set(T newVal){
        value = newVal;
        notifyEvent(value);
        return value;
    }

    public synchronized T set(Generator<T> newVal){
        value = newVal.run();
        notifyEvent(value);
        return value;
    }

    public synchronized T get(){
        return value;
    }

    public synchronized Generator<T> getGenerator()
    { return () -> value; }

}
