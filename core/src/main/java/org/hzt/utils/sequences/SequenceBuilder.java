package org.hzt.utils.sequences;


import org.hzt.utils.spined_buffers.SpinedBuffer;

final class SequenceBuilder<T> implements Sequence.Builder<T> {

    private final SpinedBuffer<T> buffer = new SpinedBuffer<>();

    @Override
    public void accept(final T t) {
        buffer.accept(t);
    }

    @Override
    public Sequence<T> build() {
        return Sequence.of(buffer);
    }
}
