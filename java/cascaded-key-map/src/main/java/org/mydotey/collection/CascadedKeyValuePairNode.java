package org.mydotey.collection;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author koqizhao
 *
 * Jul 20, 2018
 */
public class CascadedKeyValuePairNode<K, V> {

    private K _key;
    private CascadedKeyValuePair<K, V> _pair;
    private ConcurrentHashMap<K, CascadedKeyValuePairNode<K, V>> _children;

    public CascadedKeyValuePairNode() {
        this(null);
    }

    public CascadedKeyValuePairNode(K key) {
        _key = key;
        _children = new ConcurrentHashMap<K, CascadedKeyValuePairNode<K, V>>();
    }

    public K getKey() {
        return _key;
    }

    public CascadedKeyValuePair<K, V> getPair() {
        return _pair;
    }

    public void setPair(CascadedKeyValuePair<K, V> pair) {
        _pair = pair;
    }

    public ConcurrentHashMap<K, CascadedKeyValuePairNode<K, V>> getChildren() {
        return _children;
    }

    @Override
    public String toString() {
        return String.format("%s { key: %s, pair: %s, children: %s }", getClass().getSimpleName(), _key, _pair,
                _children);
    }

}
