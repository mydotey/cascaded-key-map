using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;
using System.Threading;

namespace MyDotey.Collections.Generic
{
    /**
     * @author koqizhao
     *
     * Jul 20, 2018
     */
    public class CascadedKeyDictionary<K, V> : IEnumerable<CascadedKeyValuePair<K, V>>
    {
        private CascadedKeyValuePairNode<K, V> _rootNode = new CascadedKeyValuePairNode<K, V>();

        private object _lock = new object();
        private volatile int _count;

        public CascadedKeyDictionary()
        {

        }

        public int Count { get { return _count; } }

        public bool Contains(params K[] cascadedKeys)
        {
            return GetNode(cascadedKeys) != null;
        }

        public V Get(params K[] cascadedKeys)
        {
            if (cascadedKeys == null || cascadedKeys.Length == 0)
                throw new ArgumentException("cascadedKeys is null or empty");

            CascadedKeyValuePairNode<K, V> node = GetNode(cascadedKeys);
            if (node == null)
                return default(V);

            CascadedKeyValuePair<K, V> pair = node.Pair;
            return pair == null ? default(V) : pair.Value;
        }

        public V GetOrAdd(Func<K[], V> valueFacTory, params K[] cascadedKeys)
        {
            if (valueFacTory == null)
                throw new ArgumentNullException("valueFacTory is null");

            if (cascadedKeys == null || cascadedKeys.Length == 0)
                throw new ArgumentException("cascadedKeys is null or empty");

            V value = Get(cascadedKeys);
            if (value != null)
                return value;

            lock (_lock)
            {
                CascadedKeyValuePairNode<K, V> node = GetOrAddNode(cascadedKeys);
                CascadedKeyValuePair<K, V> pair = node.Pair;
                if (pair != null)
                    return pair.Value;

                value = valueFacTory(cascadedKeys);
                if (value == null)
                    throw new ArgumentException("valueFacTory return null");

                pair = new CascadedKeyValuePair<K, V>(value, cascadedKeys);
                node.Pair = pair;
                Interlocked.Increment(ref _count);
                return value;
            }
        }

        public V Put(V value, params K[] cascadedKeys)
        {
            if (value == null)
                return Remove(cascadedKeys);

            if (cascadedKeys == null || cascadedKeys.Length == 0)
                throw new ArgumentException("cascadedKeys is null or empty");

            lock (_lock)
            {
                CascadedKeyValuePairNode<K, V> node = GetOrAddNode(cascadedKeys);
                CascadedKeyValuePair<K, V> pair = node.Pair;
                V oldValue = pair == null ? default(V) : pair.Value;
                if (pair == null)
                {
                    pair = new CascadedKeyValuePair<K, V>(value, cascadedKeys);
                    node.Pair = pair;
                    Interlocked.Increment(ref _count);
                }
                else
                    pair.Value = value;

                return oldValue;
            }
        }

        public V Remove(params K[] cascadedKeys)
        {
            if (cascadedKeys == null || cascadedKeys.Length == 0)
                throw new ArgumentException("cascadedKeys is null or empty");

            lock (_lock)
            {
                Stack<CascadedKeyValuePairNode<K, V>> nodes = new Stack<CascadedKeyValuePairNode<K, V>>();
                nodes.Push(_rootNode);
                CascadedKeyValuePairNode<K, V> node = _rootNode;
                int i = 0;
                foreach (K key in cascadedKeys)
                {
                    node.Children.TryGetValue(key, out node);
                    if (node == null)
                        break;

                    nodes.Push(node);
                    i++;
                }

                V value = default(V);
                if (i == cascadedKeys.Length)
                {
                    node = nodes.Pop();
                    CascadedKeyValuePair<K, V> pair = node.Pair;
                    if (pair != null)
                    {
                        value = pair.Value;
                        Interlocked.Decrement(ref _count);
                    }

                    if (!node.Children.IsEmpty)
                    {
                        node.Pair = null;
                        return value;
                    }

                    nodes.Peek().Children.TryRemove(node.Key, out node);
                }

                for (node = nodes.Pop(); node != _rootNode; node = nodes.Pop())
                {
                    if (node.Pair == null && node.Children.IsEmpty)
                        nodes.Peek().Children.TryRemove(node.Key, out node);
                }

                return value;
            }
        }

        public void Clear()
        {
            lock (_lock)
            {
                _rootNode = new CascadedKeyValuePairNode<K, V>();
                _count = 0;
            }
        }

        protected CascadedKeyValuePairNode<K, V> GetNode(params K[] cascadedKeys)
        {
            CascadedKeyValuePairNode<K, V> node = _rootNode;
            foreach (K key in cascadedKeys)
            {
                node.Children.TryGetValue(key, out node);
                if (node == null)
                    return null;
            }

            return node;
        }

        protected CascadedKeyValuePairNode<K, V> GetOrAddNode(params K[] cascadedKeys)
        {
            CascadedKeyValuePairNode<K, V> node = _rootNode;
            foreach (K key in cascadedKeys)
            {
                node = node.Children.GetOrAdd(key, k => new CascadedKeyValuePairNode<K, V>(k));
            }

            return node;
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return ToList().GetEnumerator();
        }

        public virtual IEnumerator<CascadedKeyValuePair<K, V>> GetEnumerator()
        {
            return ToList().GetEnumerator();
        }

        private List<CascadedKeyValuePair<K, V>> ToList()
        {
            lock (_lock)
            {
                List<CascadedKeyValuePair<K, V>> pairs = new List<CascadedKeyValuePair<K, V>>();
                FillList(pairs, _rootNode);
                return pairs;
            }
        }

        private void FillList(List<CascadedKeyValuePair<K, V>> pairs, CascadedKeyValuePairNode<K, V> node)
        {
            if (node.Pair != null)
                pairs.Add((CascadedKeyValuePair<K, V>)node.Pair.Clone());

            foreach (CascadedKeyValuePairNode<K, V> subNode in node.Children.Values)
                FillList(pairs, subNode);
        }

        public override String ToString()
        {
            List<CascadedKeyValuePair<K, V>> list = ToList();
            return String.Format("{0} [ {1} ]", GetType().Name, String.Join(", ", list.Select(p => p.ToString()).ToArray()));
        }
    }
}