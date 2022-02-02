package hzt.tuples;

import java.util.Objects;

public final class QuadTuple<R1, R2, R3, R4> {

    private final R1 first;
    private final R2 second;
    private final R3 third;
    private final R4 fourth;

    private QuadTuple(R1 first, R2 second, R3 third, R4 fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
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

    public R4 fourth() {
        return fourth;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof QuadTuple &&
                Objects.equals(this.first, ((QuadTuple<?, ?, ?, ?>) obj).first) &&
                Objects.equals(this.second, ((QuadTuple<?, ?, ?, ?>) obj).second) &&
                Objects.equals(this.third, ((QuadTuple<?, ?, ?, ?>) obj).third) &&
                Objects.equals(this.fourth, ((QuadTuple<?, ?, ?, ?>) obj).fourth));
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, fourth);
    }

    @Override
    public String toString() {
        return "QuadTuple[" +
                "first=" + first + ", " +
                "second=" + second + ", " +
                "third=" + third + ", " +
                "fourth=" + fourth + ']';
    }

    public static <A, B, C, D> QuadTuple<A, B, C, D> of(A first, B second, C third, D fourth) {
        return new QuadTuple<>(first, second, third, fourth);
    }

}
