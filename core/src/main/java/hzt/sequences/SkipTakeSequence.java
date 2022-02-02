package hzt.sequences;

public interface SkipTakeSequence<T> extends Sequence<T> {

    @Override
    Sequence<T> skip(int n);

    @Override
    Sequence<T> take(int n);
}
