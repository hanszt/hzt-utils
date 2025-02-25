package org.hzt.utils;


import java.util.Objects;

/**
 * A generic, lazily loaded (or created) object wrapper. The <code>Lazy</code>
 * is created with a key and a {@link Loader}. When {@link #get()} is called,
 * the <code>Loader</code> is used to instantiate the object identified by the
 * key. The object is cached for subsequent requests.
 * <p>
 * <code>KeyedLazy</code> is thread-safe.
 *
 * @param <K> the type of the key object.
 * @param <V> the type of the value object.
 */
public final class KeyedLazy<K, V> implements Transformable<V> {

    private final K key;
    private final Loader<K, V> loader;

    private volatile V value;

    private KeyedLazy(K key, final Loader<K, V> loader) {
        Objects.requireNonNull(key, "Key must not be null");
        Objects.requireNonNull(loader, "Loader must not be null");
        this.key = key;
        this.loader = loader;
    }

    private KeyedLazy(K key, V value) {
        Objects.requireNonNull(key, "Key must not be null");
        this.key = key;
        this.value = value;
        this.loader = unused -> value;
    }

    public static <K, V> KeyedLazy<K, V> of(K key, final Loader<K, V> loader) {
        return new KeyedLazy<>(key, loader);
    }

    public static <K, V> KeyedLazy<K, V> of(K key, V value) {
        return new KeyedLazy<>(key, value);
    }

    /**
     * Returns the lazily loaded object, identified by the key.
     *
     * @throws NotLoadedException if no object could be loaded for the specified key.
     */
    public V get() {
        V v = this.value;
        if (v != null) {
            return v;
        }
        synchronized (this.key) {
            v = this.value;
            if (v == null) {
                v = this.value = loader.load(this.key);
            }
            return Objects.requireNonNull(v);
        }
    }

    public boolean isLoaded() {
        return this.value != null;
    }

    @FunctionalInterface
    public interface Loader<K, V> {

        /**
         * Loads, or otherwise instantiates the object identified by <code>key</code>.
         *
         * @param key unique identifier for the loaded object.
         * @return the loaded object.
         * @throws NotLoadedException if the object could not be loaded.
         */
        V load(K key) throws NotLoadedException;
    }

    public static class NotLoadedException extends RuntimeException {
        public NotLoadedException(Object key) {
            super("No value could be loaded for key \"" + key + "\"");
        }

        public NotLoadedException(Object key, String message) {
            super("No value could be loaded for key \"" + key + "\": " + message);
        }
    }
}
