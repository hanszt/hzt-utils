package org.hzt.utils.streams;

import java.util.Map;
import java.util.stream.Stream;

final class EntryStreamXImpl<K, V> extends StreamXImpl<Map.Entry<K, V>> implements EntryStreamX<K, V> {

    EntryStreamXImpl(Stream<Map.Entry<K, V>> stream) {
        super(stream);
    }

}
