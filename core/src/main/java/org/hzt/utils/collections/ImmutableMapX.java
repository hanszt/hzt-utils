package org.hzt.utils.collections;

import org.hzt.utils.tuples.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

final class ImmutableMapX<K, V> implements MapX<K, V> {

    private final Map<K, V> map;

    ImmutableMapX(final Map<? extends K, ? extends V> map) {
        this.map = Map.copyOf(map);
    }

    ImmutableMapX() {
        this(Map.of());
    }

    ImmutableMapX(final Iterable<Entry<K, V>> iterable) {
        final var hashMap = new HashMap<K, V>();
        for (final var entry : iterable) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        this.map = Map.copyOf(hashMap);
    }

    @SafeVarargs
    ImmutableMapX(final Pair<K, V>... pairs) {
        final Map<K, V> hashMap = new HashMap<>();
        for (final var pair : pairs) {
            hashMap.put(pair.first(), pair.second());
        }
        this.map = Map.copyOf(hashMap);
    }

    @SafeVarargs
    ImmutableMapX(final Entry<? extends K, ? extends V>... entries) {
        final var hashMap = new HashMap<K, V>();
        for (final var entry : entries) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        this.map = Map.copyOf(hashMap);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public <K1, V1> MapX<K1, V1> map(final Function<? super K, ? extends K1> keyMapper,
                                     final Function<? super V, ? extends V1> valueMapper) {
        final Map<K1, V1> resultMap = new HashMap<>();
        for (final var entry : this) {
            final var key = entry.getKey();
            if (key != null) {
                resultMap.put(keyMapper.apply(key), valueMapper.apply(entry.getValue()));
            }
        }
        return MapX.of(resultMap);
    }

    @Override
    public boolean containsKey(final Object key) {
        //noinspection SuspiciousMethodCalls
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        //noinspection SuspiciousMethodCalls
        return map.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        //noinspection SuspiciousMethodCalls
        return map.get(key);
    }

    @Override
    public MutableSetX<K> keySet() {
        return MutableLinkedSetX.of(map.keySet());
    }

    @Override
    public MutableListX<V> values() {
        return MutableListX.of(map.values());
    }

    @Override
    public MutableSetX<Entry<K, V>> entrySet() {
        return MutableSetX.of(map.entrySet());
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }

    /**
     * Compares the specified object with this map for equality.  Returns
     * {@code true} if the given object is also a map and the two maps
     * represent the same mappings.  More formally, two maps {@code m1} and
     * {@code m2} represent the same mappings if
     * {@code m1.entrySet().equals(m2.entrySet())}.  This ensures that the
     * {@code equals} method works properly across different implementations
     * of the {@code Map} interface.
     *
     * @param o object to be compared for equality with this map
     * @return {@code true} if the specified object is equal to this map
     * @implSpec This implementation first checks if the specified object is this map;
     * if so it returns {@code true}.  Then, it checks if the specified
     * object is a map whose size is identical to the size of this map; if
     * not, it returns {@code false}.  If so, it iterates over this map's
     * {@code entrySet} collection, and checks that the specified map
     * contains each mapping that this map contains.  If the specified map
     * fails to contain such a mapping, {@code false} is returned.  If the
     * iteration completes, {@code true} is returned.
     */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MapX)) {
            return false;
        }
        MapX<?, ?> m = (MapX<?, ?>) o;
        if (m.size() != size()) {
            return false;
        }

        try {
            for (Entry<K, V> e : entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                if (value == null) {
                    if (!(m.get(key) == null && m.containsKey(key))) {
                        return false;
                    }
                } else {
                    if (!value.equals(m.get(key))) {
                        return false;
                    }
                }
            }
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }

        return true;
    }

    /**
     * Returns the hash code value for this map.  The hash code of a map is
     * defined to be the sum of the hash codes of each entry in the map's
     * {@code entrySet()} view.  This ensures that {@code m1.equals(m2)}
     * implies that {@code m1.hashCode()==m2.hashCode()} for any two maps
     * {@code m1} and {@code m2}, as required by the general contract of
     * {@link Object#hashCode}.
     *
     * @return the hash code value for this map
     * @implSpec This implementation iterates over {@code entrySet()}, calling
     * {@link Map.Entry#hashCode hashCode()} on each element (entry) in the
     * set, and adding up the results.
     * @see Map.Entry#hashCode()
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    public int hashCode() {
        int h = 0;
        for (Entry<K, V> entry : entrySet()) {
            h += entry.hashCode();
        }
        return h;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
