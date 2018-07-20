using System;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;

namespace MyDotey.NameCache
{
    public class NameCache
    {
        private NameCacheNode<object, CachedName> _namePartTree = new NameCacheNode<object, CachedName>();
        private string _separator;

        public NameCache()
            : this(null)
        {

        }

        public NameCache(string separator)
        {
            _separator = separator == null ? String.Empty : separator.Trim();
        }

        public CachedName Get(params object[] nameParts)
        {
            if (nameParts == null || nameParts.Length == 0)
                throw new ArgumentException("nameParts is null or empty");

            NameCacheNode<Object, CachedName> node = _namePartTree;
            for (int i = 0; i < nameParts.Length; i++)
            {
                node = node.Children.GetOrAdd(nameParts[i], n => new NameCacheNode<Object, CachedName>(n));
            }

            if (node.Value == null)
            {
                lock (node)
                {
                    if (node.Value == null)
                        node.Value = new CachedName(_separator, nameParts);
                }
            }

            return node.Value;
        }
    }

}
