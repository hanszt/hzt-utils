package org.hzt.utils.collections;

import java.util.Map;

final class MapHelper {

    private MapHelper() {
    }

    static <K, V> Map<K, V> mapAddFour(Map<K, V> map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }
}
