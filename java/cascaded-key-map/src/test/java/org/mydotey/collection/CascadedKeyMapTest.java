package org.mydotey.collection;

import org.junit.Assert;
import org.junit.Test;
import org.mydotey.collection.CascadedKeyMap;
import org.mydotey.collection.CascadedKeyValuePair;

/**
 * @author koqizhao
 *
 * Jul 20, 2018
 */
public class CascadedKeyMapTest {

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

    @Test
    public void testGet() {
        CascadedKeyMap<String, String> map = new CascadedKeyMap<>();

        String value = map.get("1", "2", "3");
        Assert.assertNull(value);
        Assert.assertEquals(0, map.size());

        value = map.getOrAdd(keys -> String.join(".", keys), "1", "2", "3");
        Assert.assertEquals("1.2.3", value);
        Assert.assertEquals(1, map.size());

        String value2 = map.getOrAdd(keys -> String.join(".", keys), "1", "2", "3");
        Assert.assertTrue(value == value2);
        Assert.assertEquals(1, map.size());

        value2 = map.get("1", "2", "3");
        Assert.assertTrue(value == value2);
        Assert.assertEquals(1, map.size());
    }

    @Test
    public void testPut() {
        CascadedKeyMap<String, String> map = new CascadedKeyMap<>();

        String value = map.put("ok", "1", "2", "3");
        Assert.assertNull(value);
        Assert.assertEquals(1, map.size());

        value = map.get("1", "2", "3");
        Assert.assertEquals("ok", value);
        Assert.assertEquals(1, map.size());

        value = map.put("ok2", "1", "2", "3");
        Assert.assertEquals("ok", value);
        Assert.assertEquals(1, map.size());

        value = map.get("1", "2", "3");
        Assert.assertEquals("ok2", value);
        Assert.assertEquals(1, map.size());

        value = map.put(null, "1", "2", "3");
        Assert.assertEquals("ok2", value);
        Assert.assertEquals(0, map.size());

        value = map.get("1", "2", "3");
        Assert.assertNull(value);
        Assert.assertEquals(0, map.size());

        map.put("ok", "1", "2", "3");
        map.put("ok2", "1", "2", "3", "4");
        Assert.assertEquals(2, map.size());

        value = map.get("1", "2", "3");
        Assert.assertEquals("ok", value);
        value = map.get("1", "2", "3", "4");
        Assert.assertEquals("ok2", value);

        value = map.put(null, "1", "2", "3");
        Assert.assertEquals("ok", value);
        Assert.assertEquals(1, map.size());

        value = map.get("1", "2", "3", "4");
        Assert.assertEquals("ok2", value);
    }

    @Test
    public void testRemove() {
        CascadedKeyMap<String, String> map = new CascadedKeyMap<>();

        String value = map.remove("1", "2", "3");
        Assert.assertNull(value);
        Assert.assertEquals(0, map.size());

        map.put("ok", "1", "2", "3");
        value = map.remove("1", "2", "3");
        Assert.assertEquals("ok", value);
        Assert.assertEquals(0, map.size());

        map.put("ok", "1", "2", "3");
        map.put("ok2", "1", "2", "3", "4");
        value = map.remove("1", "2", "3");
        Assert.assertEquals("ok", value);
        Assert.assertEquals(1, map.size());
        value = map.remove("1", "2", "3", "4");
        Assert.assertEquals("ok2", value);
        Assert.assertEquals(0, map.size());
    }

    @Test
    public void testIterable() {
        CascadedKeyMap<String, String> map = new CascadedKeyMap<>();
        map.put("ok", "1", "2", "3");
        map.put("ok2", "1", "2", "3", "4");

        int count = 0;
        for (CascadedKeyValuePair<String, String> pair : map) {
            count++;
            if (count == 1) {
                Assert.assertEquals("ok", pair.getValue());
                Assert.assertArrayEquals(new String[] { "1", "2", "3" }, pair.getCascadedKeys());
            } else if (count == 2) {
                Assert.assertEquals("ok2", pair.getValue());
                Assert.assertArrayEquals(new String[] { "1", "2", "3", "4" }, pair.getCascadedKeys());
            }
        }
        Assert.assertEquals(2, count);

        System.out.println(map);
    }

}
