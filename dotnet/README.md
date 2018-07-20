# Name Cache

## NuGet package

```sh
dotnet Add package MyDotey.NameCache -v 1.0.0
```

## Usage

```cs
using MyDotey.NameCache;
using NC = MyDotey.NameCache.NameCache;

namespace MyDotey.NameCacheTest
{
    public class NameCacheTest
    {
        [Fact]
        public void TestGetName()
        {
            string separator = ".";
            NC nameCache = new NC(separator);
            CachedName name1 = nameCache.Get("1", "2", "3");
            CachedName name2 = nameCache.Get("1", "2", "3");

            Assert.Equal("1.2.3", name1.Name);
            Assert.True(Object.ReferenceEquals(name1, name2));
            Assert.True(Object.ReferenceEquals(name1.Name, name2.Name));
        }
    }
}
```