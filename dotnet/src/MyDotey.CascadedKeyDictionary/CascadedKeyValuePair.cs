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
    public class CascadedKeyValuePair<K, V> : ICloneable
    {
        public K[] CascadedKeys { get; private set; }
        public V Value { get; set; }

        public CascadedKeyValuePair(V value, params K[] cascadedKeys)
        {
            CascadedKeys = cascadedKeys;
            Value = value;
        }

        public override String ToString()
        {
            return String.Format("{0} {{ cascadedKeys: [ {1} ], value: {2} }}", GetType().Name, String.Join(", ", CascadedKeys), Value);
        }

        public virtual object Clone()
        {
            return MemberwiseClone();
        }
    }
}
