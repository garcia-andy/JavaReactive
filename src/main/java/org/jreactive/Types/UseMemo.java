package org.jreactive.Types;

import org.jreactive.Utils.Cache;

import java.util.function.Function;

public class UseMemo {
    private static class MemoizedFunction<T,R> implements Function<T, R> {
        private final Cache<T,R> _cache = new Cache<>(1000);
        private final Function<T,R> _impl;

        public MemoizedFunction(Function<T,R> f){
            _impl = f;
        }

        public R run(T argv)
        { return _impl.apply(argv); }

        public R apply(T argv){
            if( _cache.have(argv) )
                return _cache.get(argv);
            return _cache.set(argv,run(argv));
        }
    }

    public <T,R>Function<T,R> useMemo(Function<T,R> func){
        return new MemoizedFunction<T, R>(func);
    }

    public <T,R>R memo(Function<T,R> func, T argv){
        return (useMemo(func)).apply(argv);
    }

}
