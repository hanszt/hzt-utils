package hzt.sequences.primitives;

public interface DoubleSkipTakeSequence extends DoubleSequence {

    @Override
    DoubleSequence skip(long n);

    @Override
    DoubleSequence take(long n);
}
