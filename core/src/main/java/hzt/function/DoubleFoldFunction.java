package hzt.function;

@FunctionalInterface
public interface DoubleFoldFunction<R> {

    R apply(R acc, double value);
}
