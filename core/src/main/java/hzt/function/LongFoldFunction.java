package hzt.function;

@FunctionalInterface
public interface LongFoldFunction<R> {

    R apply(R acc, long value);
}
