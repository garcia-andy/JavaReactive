package org.jreactive.Types;

import java.util.*;

public class UseEffect {
    private static final HashSet<State<Object>> _effectsSignals =
            new HashSet<>();
    private static final HashSet<Effect> _effectsFunction =
            new HashSet<>();

    public interface Effect{
        void run();
    }

    public static <T, D> void useEffect(Effect c, Collection<D> deps){
        List<State<Object>> states = deps.stream()
                .map(d -> UseSignal.useSignal(d).computed(s -> {
                    c.run();
                    return s.get();
                })).toList();
        if( !_effectsFunction.contains(c) || !_effectsSignals.containsAll(states) ) {
            _effectsSignals.addAll(states);
            _effectsFunction.add(c);
        }
    }

    public static <T,D> void useEffect(Effect c){
        if( !_effectsFunction.contains(c) ){
            c.run();
            _effectsSignals.add( UseSignal.useSignal().computed( s -> {c.run(); return s.get(); } ) );
            _effectsFunction.add(c);
        }
    }

}
