package cam72cam.immersiverailroading.util;

import com.ibm.icu.impl.IllegalIcuArgumentException;

import java.util.HashMap;
import java.util.Map;

public class BiMap<K, V> {
    Map<K, V> keyToValue = new HashMap<>();
    Map<V, K> valueToKey = new HashMap<>();

    public void put(K key, V value) throws IllegalIcuArgumentException {
        if(keyToValue.containsKey(key) || valueToKey.containsKey(value)) {
            throw new IllegalArgumentException("Duplicate key value detected: K:" + key + ", V:" + value);
        }

        keyToValue.put(key, value);
        valueToKey.put(value, key);
    }

    public void forcePut(K key, V value) {
        if(keyToValue.containsKey(key)) {
            V oldValue = keyToValue.get(key);

            keyToValue.remove(key);
            valueToKey.remove(oldValue);
        }

        if(valueToKey.containsKey(value)) {
            K oldKey = valueToKey.get(value);

            valueToKey.remove(value);
            keyToValue.remove(oldKey);
        }

        keyToValue.put(key, value);
        valueToKey.put(value, key);
    }

    public K getKey(V value) {
        return valueToKey.get(value);
    }

    public V getValue(K key) {
        return keyToValue.get(key);
    }

    public void removeKey(K key) {
        V oldValue = keyToValue.get(key);

        keyToValue.remove(key);
        valueToKey.remove(oldValue);
    }

    public void removeValue(V value) {
        K oldKey = valueToKey.get(value);

        keyToValue.remove(oldKey);
        valueToKey.remove(value);
    }

    public boolean containsKey(K key) {
        return keyToValue.containsKey(key);
    }

    public boolean containsValue(V value) {
        return valueToKey.containsKey(value);
    }

}