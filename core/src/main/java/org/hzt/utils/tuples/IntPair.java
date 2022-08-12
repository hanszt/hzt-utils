package org.hzt.utils.tuples;

import org.hzt.utils.Transformable;
import org.hzt.utils.function.primitives.IntBiFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.IntFunction;

public final class IntPair implements Transformable<IntPair> {

    private final int first;
    private final int second;

    private IntPair(int first, int second) {
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
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        IntPair that = (IntPair) obj;
        return Objects.equals(this.first, that.first) &&
                Objects.equals(this.second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    public static IntPair of(int first, int second) {
        return new IntPair(first, second);
    }

    public <R> R to(IntBiFunction<R> mapper) {
        return mapper.apply(first, second);
    }

    public <A1, B1> Pair<A1, B1> mapBoth(IntFunction<A1> firstValueMapper, IntFunction<B1> secondValueMapper) {
        return Pair.of(firstValueMapper.apply(first), secondValueMapper.apply(second));
    }

    @Override
    public String toString() {
        return "Pair[" +
                "first=" + first + ", " +
                "second=" + second + ']';
    }

    @Override
    public @NotNull IntPair get() {
        return this;
    }
}