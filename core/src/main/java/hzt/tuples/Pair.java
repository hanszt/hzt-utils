package hzt.tuples;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("ClassCanBeRecord")
public final class Pair<A, B> {

    private final A first;
    private final B second;

    private Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A first() {
        return first;
    }

    public B second() {
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
        Pair<A, B> that = (Pair<A ,B>) obj;
        return Objects.equals(this.first, that.first) &&
                Objects.equals(this.second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    public static <A, B> Pair<A, B> ofEntry(Map.Entry<A, B> entry) {
        return new Pair<>(entry.getKey(), entry.getValue());
    }

    public <R> R to(BiFunction<A, B, R> mapper) {
        return mapper.apply(first, second);
    }

    public <A1, B1> Pair<A1, B1> mapBoth(Function<A, A1> firstValueMapper, Function<B, B1> secondValueMapper) {
        return Pair.of(firstValueMapper.apply(first), secondValueMapper.apply(second));
    }

    public <A1> Pair<A1, B> mapFirst(Function<A, A1> firstValueMapper) {
        return Pair.of(firstValueMapper.apply(first), second);
    }

    public <B1> Pair<A, B1> mapSecond(Function<B, B1> secondValueMapper) {
        return Pair.of(first, secondValueMapper.apply(second));
    }

    @Override
    public String toString() {
        return "Pair[" +
                "first=" + first + ", " +
                "second=" + second + ']';
    }

}
