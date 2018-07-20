using System;
using Xunit;

using MyDotey.Collections.Generic;

namespace MyDotey.NameCacheTest
{
    /**
     * @author koqizhao
     *
     * Jul 20, 2018
     */
    public class CascadedKeyDictionaryTest
    {
        [Fact]
        public void TestDemo()
        {
            CascadedKeyDictionary<String, String> map = new CascadedKeyDictionary<String, String>();

            map.Put("ok", "key1", "key2", "key3");
            String value = map.Get("key1", "key2", "key3");
            Assert.Equal("ok", value);

            map.Put("ok2", "key1", "key2", "key3", "key4");
            value = map.Get("key1", "key2", "key3", "key4");
            Assert.Equal("ok2", value);

            value = map.GetOrAdd(cascadedKeys => "ok3", "key1", "key2");
            Assert.Equal("ok3", value);

            map.Remove("key1", "key2");

            Assert.Equal(2, map.Count);

            foreach (CascadedKeyValuePair<String, String> pair in map)
            {
                Console.WriteLine("cascadedKeys: [ {0} ], value: {1}\n", String.Join(", ", pair.CascadedKeys), pair.Value);
            }
        }

        [Fact]
        public void TestGet()
        {
            CascadedKeyDictionary<String, String> map = new CascadedKeyDictionary<String, String>();

            String value = map.Get("1", "2", "3");
            Assert.Null(value);
            Assert.Equal(0, map.Count);

            value = map.GetOrAdd(keys => String.Join(".", keys), "1", "2", "3");
            Assert.Equal("1.2.3", value);
            Assert.Equal(1, map.Count);

            String value2 = map.GetOrAdd(keys => String.Join(".", keys), "1", "2", "3");
            Assert.True(value == value2);
            Assert.Equal(1, map.Count);

            value2 = map.Get("1", "2", "3");
            Assert.True(value == value2);
            Assert.Equal(1, map.Count);
        }

        [Fact]
        public void TestPut()
        {
            CascadedKeyDictionary<String, String> map = new CascadedKeyDictionary<String, String>();

            String value = map.Put("ok", "1", "2", "3");
            Assert.Null(value);
            Assert.Equal(1, map.Count);

            value = map.Get("1", "2", "3");
            Assert.Equal("ok", value);
            Assert.Equal(1, map.Count);

            value = map.Put("ok2", "1", "2", "3");
            Assert.Equal("ok", value);
            Assert.Equal(1, map.Count);

            value = map.Get("1", "2", "3");
            Assert.Equal("ok2", value);
            Assert.Equal(1, map.Count);

            value = map.Put(null, "1", "2", "3");
            Assert.Equal("ok2", value);
            Assert.Equal(0, map.Count);

            value = map.Get("1", "2", "3");
            Assert.Null(value);
            Assert.Equal(0, map.Count);

            map.Put("ok", "1", "2", "3");
            map.Put("ok2", "1", "2", "3", "4");
            Assert.Equal(2, map.Count);

            value = map.Get("1", "2", "3");
            Assert.Equal("ok", value);
            value = map.Get("1", "2", "3", "4");
            Assert.Equal("ok2", value);

            value = map.Put(null, "1", "2", "3");
            Assert.Equal("ok", value);
            Assert.Equal(1, map.Count);

            value = map.Get("1", "2", "3", "4");
            Assert.Equal("ok2", value);
        }

        [Fact]
        public void TestRemove()
        {
            CascadedKeyDictionary<String, String> map = new CascadedKeyDictionary<String, String>();

            String value = map.Remove("1", "2", "3");
            Assert.Null(value);
            Assert.Equal(0, map.Count);

            map.Put("ok", "1", "2", "3");
            value = map.Remove("1", "2", "3");
            Assert.Equal("ok", value);
            Assert.Equal(0, map.Count);

            map.Put("ok", "1", "2", "3");
            map.Put("ok2", "1", "2", "3", "4");
            value = map.Remove("1", "2", "3");
            Assert.Equal("ok", value);
            Assert.Equal(1, map.Count);
            value = map.Remove("1", "2", "3", "4");
            Assert.Equal("ok2", value);
            Assert.Equal(0, map.Count);
        }

        [Fact]
        public void TestIterable()
        {
            CascadedKeyDictionary<String, String> map = new CascadedKeyDictionary<String, String>();
            map.Put("ok", "1", "2", "3");
            map.Put("ok2", "1", "2", "3", "4");

            int count = 0;
            foreach (CascadedKeyValuePair<String, String> pair in map)
            {
                count++;
                if (count == 1)
                {
                    Assert.Equal("ok", pair.Value);
                    Assert.Equal(new String[] { "1", "2", "3" }, pair.CascadedKeys);
                }
                else if (count == 2)
                {
                    Assert.Equal("ok2", pair.Value);
                    Assert.Equal(new String[] { "1", "2", "3", "4" }, pair.CascadedKeys);
                }
            }
            Assert.Equal(2, count);

            Console.WriteLine(map);
            Console.WriteLine();
        }
    }
}
