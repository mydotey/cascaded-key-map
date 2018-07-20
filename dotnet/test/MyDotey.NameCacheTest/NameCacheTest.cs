using System;
using Xunit;

using MyDotey.NameCache;
using NC = MyDotey.NameCache.NameCache;

namespace MyDotey.NameCacheTest
{
    public class NameCacheTest
    {
        [Fact]
        public void TestGetName()
        {
            NC nameCache = new NC(".");
            CachedName name1 = nameCache.Get("1", "2", "3");
            CachedName name2 = nameCache.Get("1", "2", "3");

            Assert.True(Object.ReferenceEquals(name1, name2));
            Assert.True(Object.ReferenceEquals(name1.Name, name2.Name));
        }
    }
}
