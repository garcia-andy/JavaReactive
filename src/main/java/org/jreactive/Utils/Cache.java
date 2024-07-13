package org.jreactive.Utils;

import java.util.*;

public class Cache<K, V> {

    private Map<K, V> _cache;
    private int top;

    public Cache() {
        this(100);
    }

    public Cache(int topContent) {
        top = topContent;
        _cache = Collections.synchronizedMap(
                new LinkedHashMap<K, V>(top * 4 / 3, 0.80f, true) {
            @Override
            protected boolean removeEldestEntry(
                    Map.Entry<K, V> eldest
            ) {
                return size() > top;
            }
        }
        );
    }

    public boolean have(K k) {
        return _cache.containsKey(k);
    }

    public V set(K k, V v) {
        V ret = v;
        if (_cache.containsKey(k)) {
            ret = _cache.get(k);
            _cache.replace(k, v);
        } else {
            _cache.put(k, v);
        }
        return ret;
    }

    public V get(K k) {
        return _cache.getOrDefault(k, null);
    }

    public V get(K k, V _default) {
        return _cache.getOrDefault(k, _default);
    }

}
