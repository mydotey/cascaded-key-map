package org.mydotey.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author koqizhao
 *
 * Jul 20, 2018
 */
public class CascadedKeyValuePair<K, V> implements Cloneable {

    private K[] _cascadedKeys;
    private V _value;

    @SafeVarargs
    public CascadedKeyValuePair(V value, K... cascadedKeys) {
        _cascadedKeys = cascadedKeys;
        _value = value;
    }

    public K[] getCascadedKeys() {
        return _cascadedKeys;
    }

    public V getValue() {
        return _value;
    }

    public void setValue(V value) {
        _value = value;
    }

    @Override
    public String toString() {
        List<String> keyStrings = new ArrayList<>();
        for (K k : _cascadedKeys)
            keyStrings.add(String.valueOf(k));
        return String.format("%s { cascadedKeys: %s, value: %s }", getClass().getSimpleName(), keyStrings, _value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CascadedKeyValuePair<K, V> clone() {
        try {
            return (CascadedKeyValuePair<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return new CascadedKeyValuePair<>(_value, _cascadedKeys);
        }
    }

}
