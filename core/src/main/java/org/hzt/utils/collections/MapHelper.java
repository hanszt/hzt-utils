package org.hzt.utils.collections;

import java.util.Map;

final class MapHelper {

    private MapHelper() {
    }

    static <K, V> Map<K, V> mapAddFour(final Map<K, V> map, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }
}
