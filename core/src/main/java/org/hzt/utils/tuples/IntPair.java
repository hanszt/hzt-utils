package org.hzt.utils.tuples;

import org.hzt.utils.Transformable;
import org.hzt.utils.function.primitives.IntBiFunction;

import java.util.Objects;
import java.util.function.IntFunction;

public final class IntPair implements Transformable<IntPair> {

    private final int first;
    private final int second;

    private IntPair(final int first, final int second) {
        this.first = first;
        this.second = second;
    }

    public int first() {
        return first;
    }

    public int second() {
        return second;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        final IntPair that = (IntPair) obj;
        return Objects.equals(this.first, that.first) &&
                Objects.equals(this.second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    public static IntPair of(final int first, final int second) {
        return new IntPair(first, second);
    }

    public <R> R to(final IntBiFunction<R> mapper) {
        return mapper.apply(first, second);
    }

    public <A1, B1> Pair<A1, B1> mapBoth(final IntFunction<A1> firstValueMapper, final IntFunction<B1> secondValueMapper) {
        return Pair.of(firstValueMapper.apply(first), secondValueMapper.apply(second));
    }

    @Override
    public String toString() {
        return "Pair[" +
                "first=" + first + ", " +
                "second=" + second + ']';
    }

    @Override
    public IntPair get() {
        return this;
    }
}
