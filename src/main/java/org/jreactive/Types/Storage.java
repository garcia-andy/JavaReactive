package org.jreactive.Types;

import java.io.*;
import java.util.*;

public class Storage<T> implements Map<String, T>, Closeable, Iterable<String> {
    private UUID _id;
    private LinkedHashMap<String,T> _data = new LinkedHashMap<>();

    public Storage(String storageName) {
        _id = UUID.fromString(storageName);
        loadData();
    }

    public Storage<T> reload(String newStorageName){
        close(); clear();
        _id = UUID.fromString(newStorageName);
        loadData();
        return this;
    }

    public Storage<T> add(String key, T value){
        if( !_data.containsKey(key) ){
            _data.put(key,value);
        }
        return this;
    }

    public Storage<T> set(String key, T value){
        if( !_data.containsKey(key) ){
            _data.put(key,value);
        }else{
            _data.replace(key,value);
        }
        return this;
    }

    public T get(String key)
    { return _data.getOrDefault(key,null); }

    public Storage<T> remove(String key){
        if(_data.containsKey(key))
            _data.remove(key);
        return this;
    }

    private void loadData()  {
        try(
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(_id.toString())
            );
        ){
            _data = (LinkedHashMap<String, T>) in.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public Storage<T> refresh(){
        loadData();
        return this;
    }

    /* CLOSABLE INTERFACE */

    @Override
    public void close() {
        try(
                ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream(_id.toString())
                );
        ){
            out.writeObject(_data);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /* ITERABLE INTERFACE */

    @Override
    public Iterator<String> iterator() {
        return _data.keySet().iterator();
    }

    /* MAP INTERFACE */

    @Override
    public int size() {
        return _data.size();
    }

    @Override
    public boolean isEmpty() {
        return _data.isEmpty();
    }

    public boolean contains(Object o) {
        return containsValue(o) || containsKey(o);
    }

    @Override
    public boolean containsKey(Object o) {
        return _data.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return _data.containsValue(o);
    }

    @Override
    public T get(Object o) {
        return _data.getOrDefault(o,null);
    }

    @Override
    public T put(String s, T t) {
        return add(s,t).get(s);
    }

    @Override
    public T remove(Object o) {
        return _data.remove(o);
    }


    public boolean containsAllKeys(Collection<String> collection) {
        boolean ret = true;
        for(String k: collection){
            ret &= containsKey(k);
            if( !ret ) break;
        }
        return ret;
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> map) {
        for(String k: map.keySet())
            add(k,map.get(k));
    }

    @Override
    public void clear() {
        _data.clear();
    }

    @Override
    public Set<String> keySet() {
        return _data.keySet();
    }

    @Override
    public Collection<T> values() {
        return _data.values();
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        return _data.entrySet();
    }


}
