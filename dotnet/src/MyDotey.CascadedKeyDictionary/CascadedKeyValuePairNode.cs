using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;

namespace MyDotey.Collections.Generic
{
    /**
     * @author koqizhao
     *
     * Jul 20, 2018
     */
    public class CascadedKeyValuePairNode<K, V>
    {
        public K Key { get; private set; }
        public CascadedKeyValuePair<K, V> Pair { get; set; }
        public ConcurrentDictionary<K, CascadedKeyValuePairNode<K, V>> Children { get; private set; }

        public CascadedKeyValuePairNode()
            : this(default(K))
        {

        }

        public CascadedKeyValuePairNode(K key)
        {
            Key = key;
            Children = new ConcurrentDictionary<K, CascadedKeyValuePairNode<K, V>>();
        }

        public override String ToString()
        {
            return string.Format("{0} {{ key: {1}, pair: {2}, children: {3} }}", GetType().Name, Key, Pair, Children);
        }
    }
}