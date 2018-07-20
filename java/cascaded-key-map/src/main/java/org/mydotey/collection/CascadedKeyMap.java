package org.mydotey.collection;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author koqizhao
 *
 * Jul 20, 2018
 */
@SuppressWarnings("unchecked")
public class CascadedKeyMap<K, V> implements Iterable<CascadedKeyValuePair<K, V>> {

    private CascadedKeyValuePairNode<K, V> _rootNode = new CascadedKeyValuePairNode<>();

    private Function<K, CascadedKeyValuePairNode<K, V>> _nodeCreator = k -> new CascadedKeyValuePairNode<>(k);

    private Object _lock = new Object();
    private AtomicInteger _size = new AtomicInteger();

    public CascadedKeyMap() {

    }

    public int size() {
        return _size.get();
    }

    public boolean contains(K... cascadedKeys) {
        return getNode(cascadedKeys) != null;
    }

    public V get(K... cascadedKeys) {
        if (cascadedKeys == null || cascadedKeys.length == 0)
            Objects.requireNonNull("cascadedKeys is null or empty");

        CascadedKeyValuePairNode<K, V> node = getNode(cascadedKeys);
        if (node == null)
            return null;

        CascadedKeyValuePair<K, V> pair = node.getPair();
        return pair == null ? null : pair.getValue();
    }

    public V getOrAdd(Function<K[], V> valueFactory, K... cascadedKeys) {
        Objects.requireNonNull(valueFactory, "valueFactory is null");
        if (cascadedKeys == null || cascadedKeys.length == 0)
            Objects.requireNonNull("cascadedKeys is null or empty");

        V value = get(cascadedKeys);
        if (value != null)
            return value;

        synchronized (_lock) {
            CascadedKeyValuePairNode<K, V> node = getOrAddNode(cascadedKeys);
            CascadedKeyValuePair<K, V> pair = node.getPair();
            if (pair != null)
                return pair.getValue();

            value = valueFactory.apply(cascadedKeys);
            if (value == null)
                throw new IllegalArgumentException("valueFactory return null");

            pair = new CascadedKeyValuePair<>(value, cascadedKeys);
            node.setPair(pair);
            _size.incrementAndGet();
            return value;
        }
    }

    public V put(V value, K... cascadedKeys) {
        if (value == null)
            return remove(cascadedKeys);

        if (cascadedKeys == null || cascadedKeys.length == 0)
            Objects.requireNonNull("cascadedKeys is null or empty");

        synchronized (_lock) {
            CascadedKeyValuePairNode<K, V> node = getOrAddNode(cascadedKeys);
            CascadedKeyValuePair<K, V> pair = node.getPair();
            V oldValue = pair == null ? null : pair.getValue();
            if (pair == null) {
                pair = new CascadedKeyValuePair<>(value, cascadedKeys);
                node.setPair(pair);
                _size.incrementAndGet();
            } else
                pair.setValue(value);

            return oldValue;
        }
    }

    public V remove(K... cascadedKeys) {
        if (cascadedKeys == null || cascadedKeys.length == 0)
            Objects.requireNonNull("cascadedKeys is null or empty");

        synchronized (_lock) {
            Stack<CascadedKeyValuePairNode<K, V>> nodes = new Stack<>();
            nodes.push(_rootNode);
            CascadedKeyValuePairNode<K, V> node = _rootNode;
            int i = 0;
            for (K key : cascadedKeys) {
                node = node.getChildren().get(key);
                if (node == null)
                    break;

                nodes.push(node);
                i++;
            }

            V value = null;
            if (i == cascadedKeys.length) {
                node = nodes.pop();
                CascadedKeyValuePair<K, V> pair = node.getPair();
                if (pair != null) {
                    value = pair.getValue();
                    _size.decrementAndGet();
                }

                if (!node.getChildren().isEmpty()) {
                    node.setPair(null);
                    return value;
                }

                nodes.peek().getChildren().remove(node.getKey());
            }

            for (node = nodes.pop(); node != _rootNode; node = nodes.pop()) {
                if (node.getPair() == null && node.getChildren().isEmpty())
                    nodes.peek().getChildren().remove(node.getKey());
            }

            return value;
        }
    }

    public void clear() {
        synchronized (_lock) {
            _rootNode = new CascadedKeyValuePairNode<>();
            _size.set(0);
        }
    }

    protected CascadedKeyValuePairNode<K, V> getNode(K... cascadedKeys) {
        CascadedKeyValuePairNode<K, V> node = _rootNode;
        for (K key : cascadedKeys) {
            node = node.getChildren().get(key);
            if (node == null)
                return null;
        }

        return node;
    }

    protected CascadedKeyValuePairNode<K, V> getOrAddNode(K... cascadedKeys) {
        CascadedKeyValuePairNode<K, V> node = _rootNode;
        for (K key : cascadedKeys) {
            node = node.getChildren().computeIfAbsent(key, _nodeCreator);
        }

        return node;
    }

    @Override
    public Iterator<CascadedKeyValuePair<K, V>> iterator() {
        return toList().iterator();
    }

    private List<CascadedKeyValuePair<K, V>> toList() {
        synchronized (_lock) {
            List<CascadedKeyValuePair<K, V>> pairs = new ArrayList<>();
            fillList(pairs, _rootNode);
            return pairs;
        }
    }

    private void fillList(List<CascadedKeyValuePair<K, V>> pairs, CascadedKeyValuePairNode<K, V> node) {
        if (node.getPair() != null)
            pairs.add(node.getPair().clone());

        for (CascadedKeyValuePairNode<K, V> subNode : node.getChildren().values())
            fillList(pairs, subNode);
    }

    @Override
    public String toString() {
        List<CascadedKeyValuePair<K, V>> list = toList();
        return String.format("%s %s", getClass().getSimpleName(), list);
    }

}
