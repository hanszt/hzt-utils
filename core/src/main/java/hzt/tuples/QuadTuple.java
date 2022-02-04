package hzt.tuples;

import hzt.utils.Transformable;

import java.util.Objects;

public final class QuadTuple<A, B, C, D> implements Transformable<QuadTuple<A, B, C, D>> {

    private final A first;
    private final B second;
    private final C third;
    private final D fourth;

    private QuadTuple(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }

    public C third() {
        return third;
    }

    public D fourth() {
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

    public <E> QuintTuple<A, B, C, D, E> plus(E fifth) {
        return QuintTuple.of(first, second, third, fourth, fifth);
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

    @Override
    public QuadTuple<A, B, C, D> get() {
        return this;
    }
}
