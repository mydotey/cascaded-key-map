# Name Cache

## maven dependency

```xml
<dependency>
    <groupId>org.mydotey.namecache</groupId>
    <artifactId>name-cache</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

```java
    @Test
    public void testGetName() {
        String separator = ".";
        NameCache nameCache = new NameCache(separator);
        CachedName name1 = nameCache.get("1", "2", "3");
        CachedName name2 = nameCache.get("1", "2", "3");

        Assert.assertEquals("1.2.3", name1.getName());
        Assert.assertTrue(name1 == name2);
        Assert.assertTrue(name1.getName() == name2.getName());
    }
```
