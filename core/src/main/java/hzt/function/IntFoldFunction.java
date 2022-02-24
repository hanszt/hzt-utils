package hzt.function;

@FunctionalInterface
public interface IntFoldFunction<R> {

    R apply(R acc, int value);
}
