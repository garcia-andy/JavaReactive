package org.jreactive.Types;

import org.jreactive.Implementations.Subject;
import org.jreactive.Utils.Generator;
import org.jreactive.Utils.Pair;

import java.util.LinkedList;
import java.util.function.Function;


public class Signal<T> implements Subject<T> {
    private final State<T> _internalState;
    private final LinkedList<Pair<State<Object>, Function<State<T>, Object>>> _computeds = 
            new LinkedList<Pair<State<Object>, Function<State<T>, Object>>>();

    public Signal(T init){
        _internalState = new State<T>(init);
        _internalState.subscribe(this);
    }

    public Signal<T> set(T newValue){
        if( !_internalState.get().equals(newValue) )
            _internalState.set(newValue);
        return this;
    }

    public Signal<T> set(Generator<T> newValue)
    { return set( newValue.run() ); }

    public State<T> get()
    { return _internalState; }

    public T value()
    { return _internalState.get(); }

    public State<Object> computed(Function<State<T>,Object> callback){
        var value = new Pair<>( 
            new State<Object>( callback.apply(_internalState) ),
            callback
        );
        _computeds.add(value);
        return value.first();
    }

    @Override
    public synchronized void update(T ctx){
        for(Pair<State<Object>, Function<State<T>, Object>> p: _computeds)
            p.first().set( p.second().apply(_internalState) );
    }

    @Override
    public synchronized void update()
    { update(_internalState.get()); }

}
