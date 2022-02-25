package hzt.sequences.primitives;

public interface LongSkipTakeSequence extends LongSequence {

    @Override
    LongSequence skip(long n);

    @Override
    LongSequence take(long n);
}
