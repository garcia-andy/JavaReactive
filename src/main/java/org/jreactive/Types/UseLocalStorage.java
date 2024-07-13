package org.jreactive.Types;

import java.util.Map;

public class UseLocalStorage {

    /**
    * @param name The name of the LocalStorage
    * @return The Storage Object
    */
    public static <T>Storage<T> useLocalStorage(String name){
        return new Storage<>(name);
    }

    /**
     * @param name The name of the LocalStorage
     * @param key the key of the init
     * @param initialData the initial value
     * @return The Storage Object
     */
    public static <T>Storage<T> useLocalStorage(String name,String key,T initialData){
        return (new Storage<T>(name)).set(key,initialData);
    }

    /**
     * @param name The name of the LocalStorage
     * @param initialData the initial data
     * @return The Storage Object
     */
    public static <T>Storage<T> useLocalStorage(String name, Map<? extends String, ? extends T> initialData){
        var storage = (new Storage<T>(name));
        storage.putAll(initialData);
        return storage;
    }

    /**
     * @param name The name of the LocalStorage
     * @param key The name of the main key
     * @param initialData the initial data under key
     * @return The Storage Object
     */
    public static <T>Storage< Storage<T> > useLocalStorage(
            String name,
            String key,
            Map<? extends String, ? extends T> initialData
    ){
        var storage = (new Storage<T>(name + initialData.hashCode()));
        storage.putAll(initialData);
        return (new Storage< Storage<T> >(name).add(key,storage));
    }

}
