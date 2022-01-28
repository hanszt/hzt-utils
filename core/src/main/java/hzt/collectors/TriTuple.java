package hzt.collectors;

import java.util.Objects;

public final class TriTuple<R1, R2, R3> {
    private final R1 first;
    private final R2 second;
    private final R3 third;

    TriTuple(R1 first, R2 second, R3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public R1 first() {
        return first;
    }

    public R2 second() {
        return second;
    }

    public R3 third() {
        return third;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof TriTuple &&
                Objects.equals(this.first, ((TriTuple<?, ?, ?>) obj).first) &&
                Objects.equals(this.second, ((TriTuple<?, ?, ?>) obj).second) &&
                Objects.equals(this.third, ((TriTuple<?, ?, ?>) obj).third));
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "TriTuple[" +
                "first=" + first + ", " +
                "second=" + second + ", " +
                "third=" + third + ']';
    }

}
