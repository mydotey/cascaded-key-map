package org.mydotey.namecache;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author koqizhao
 *
 * Jul 20, 2018
 */
public class NameCache {

    private NameCacheNode<Object, CachedName> _namePartTree = new NameCacheNode<Object, CachedName>();
    private String _separator;

    private Function<Object, NameCacheNode<Object, CachedName>> _nodeCreator = k -> new NameCacheNode<Object, CachedName>(
            k);

    public NameCache() {
        this(null);
    }

    public NameCache(String separator) {
        _separator = separator == null ? "" : separator.trim();
    }

    public CachedName get(Object... nameParts) {
        if (nameParts == null || nameParts.length == 0)
            Objects.requireNonNull("nameParts is null or empty");

        NameCacheNode<Object, CachedName> node = _namePartTree;
        for (int i = 0; i < nameParts.length; i++) {
            node = node.getChildren().computeIfAbsent(nameParts[i], _nodeCreator);
        }

        if (node.getValue() == null) {
            synchronized (node) {
                if (node.getValue() == null)
                    node.setValue(new CachedName(_separator, nameParts));
            }
        }

        return node.getValue();
    }

}
