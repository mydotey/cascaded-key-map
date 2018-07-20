# Cascaded Key Map

## maven dependency

```xml
<dependency>
    <groupId>org.mydotey.cascadedkeymap</groupId>
    <artifactId>cascaded-key-map</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

```java
    @Test
    public void testDemo() {
        CascadedKeyMap<String, String> map = new CascadedKeyMap<>();

        map.put("ok", "key1", "key2", "key3");
        String value = map.get("key1", "key2", "key3");
        Assert.assertEquals("ok", value);

        map.put("ok2", "key1", "key2", "key3", "key4");
        value = map.get("key1", "key2", "key3", "key4");
        Assert.assertEquals("ok2", value);

        value = map.getOrAdd(cascadedKeys -> "ok3", "key1", "key2");
        Assert.assertEquals("ok3", value);

        map.remove("key1", "key2");

        Assert.assertEquals(2, map.size());

        for (CascadedKeyValuePair<String, String> pair : map) {
            System.out.printf("cascadedKeys: %s, value: %s\n", pair.getCascadedKeys(), pair.getValue());
        }
    }
```
