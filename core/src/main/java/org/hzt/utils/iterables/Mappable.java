package org.hzt.utils.iterables;

import org.hzt.utils.function.IndexedFunction;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public interface Mappable<T> extends IndexedIterable<T> {

    <R> Mappable<R> map(@NotNull Function<? super T, ? extends R> mapper);

    <R> Mappable<R> mapNotNull(@NotNull Function<? super T, ? extends R> mapper);

    <R> Mappable<R> mapIfPresent(Function<? super T, Optional<R>> mapper);

    <R> Mappable<R> mapIndexed(@NotNull IndexedFunction<? super T, ? extends R> mapper);

    <R> Mappable<R> flatMap(@NotNull Function<? super T, ? extends Iterable<? extends R>> mapper);

    PrimitiveIterable.OfInt flatMapToInt(@NotNull Function<? super T, ? extends PrimitiveIterable.OfInt> mapper);

    PrimitiveIterable.OfLong flatMapToLong(@NotNull Function<? super T, ? extends PrimitiveIterable.OfLong> mapper);

    PrimitiveIterable.OfDouble flatMapToDouble(@NotNull Function<? super T, ? extends PrimitiveIterable.OfDouble> mapper);

    <R> Mappable<R> mapMulti(@NotNull BiConsumer<? super T, ? super Consumer<R>> mapper);

    default PrimitiveIterable.OfInt mapMultiToInt(@NotNull final BiConsumer<? super T, IntConsumer> mapper) {
        return flatMapToInt(e -> {
            final var spinedBuffer = new SpinedBuffer.OfInt();
            mapper.accept(e, spinedBuffer);
            return spinedBuffer::iterator;
        });
    }

    default PrimitiveIterable.OfLong mapMultiToLong(@NotNull final BiConsumer<? super T, LongConsumer> mapper) {
        return flatMapToLong(e -> {
            final var spinedBuffer = new SpinedBuffer.OfLong();
            mapper.accept(e, spinedBuffer);
            return spinedBuffer::iterator;
        });
    }

    default PrimitiveIterable.OfDouble mapMultiToDouble(@NotNull final BiConsumer<? super T, DoubleConsumer> mapper) {
        return flatMapToDouble(e -> {
            final var spinedBuffer = new SpinedBuffer.OfDouble();
            mapper.accept(e, spinedBuffer);
            return spinedBuffer::iterator;
        });
    }
}
