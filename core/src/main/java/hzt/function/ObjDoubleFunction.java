package hzt.function;

@FunctionalInterface
public interface ObjDoubleFunction<R> {

    R apply(R acc, double value);
}
