using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;

namespace MyDotey.NameCache
{
    public class NameCacheNode<K, V>
    {
        public K Key { get; set; }
        public V Value { get; set; }
        public ConcurrentDictionary<K, NameCacheNode<K, V>> Children { get; set; }

        public NameCacheNode()
            : this(default(K))
        {
        }

        public NameCacheNode(K key)
        {
            Key = key;
            Children = new ConcurrentDictionary<K, NameCacheNode<K, V>>();
        }
    }
}
