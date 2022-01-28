package hzt.collectors;

import java.util.Objects;

public final class QuintTuple<R1, R2, R3, R4, R5> {

    private final R1 first;
    private final R2 second;
    private final R3 third;
    private final R4 fourth;
    private final R5 fifth;

    QuintTuple(R1 first, R2 second, R3 third, R4 fourth, R5 fifth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
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

    public R5 fifth() {
        return fifth;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof QuintTuple &&
                Objects.equals(this.first, ((QuintTuple<?, ?, ?, ?, ?>) obj).first) &&
                Objects.equals(this.second, ((QuintTuple<?, ?, ?, ?, ?>) obj).second) &&
                Objects.equals(this.third, ((QuintTuple<?, ?, ?, ?, ?>) obj).third) &&
                Objects.equals(this.fourth, ((QuintTuple<?, ?, ?, ?, ?>) obj).fourth) &&
                Objects.equals(this.fifth, ((QuintTuple<?, ?, ?, ?, ?>) obj).fifth));
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, fourth, fifth);
    }

    @Override
    public String toString() {
        return "QuintTuple[" +
                "first=" + first + ", " +
                "second=" + second + ", " +
                "third=" + third + ", " +
                "fourth=" + fourth + ", " +
                "fifth=" + fifth + ']';
    }

}
