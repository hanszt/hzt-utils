package org.hzt.utils.sequences;

public abstract class SequenceExtension<T, R> {

    public abstract Sequence<R> extend(Sequence<T> sequence);

    public <V> SequenceExtension<T, V> andThen(final SequenceExtension<R, V> next) {
        return new SequenceExtension<T, V>() {
            @Override
            public Sequence<V> extend(Sequence<T> sequence) {
                return next.extend(SequenceExtension.this.extend(sequence));
            }
        };
    }
}
