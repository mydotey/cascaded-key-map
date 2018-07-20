package org.mydotey.namecache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author koqizhao
 *
 * Jul 20, 2018
 */
public class NameCacheNode<K, V> {

    private K key;
    private V value;
    private ConcurrentHashMap<K, NameCacheNode<K, V>> children;

    public NameCacheNode() {
        this(null);
    }

    public NameCacheNode(K key) {
        this.key = key;
        children = new ConcurrentHashMap<K, NameCacheNode<K, V>>();
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public ConcurrentHashMap<K, NameCacheNode<K, V>> getChildren() {
        return children;
    }

    public void setChildren(ConcurrentHashMap<K, NameCacheNode<K, V>> children) {
        this.children = children;
    }
}
