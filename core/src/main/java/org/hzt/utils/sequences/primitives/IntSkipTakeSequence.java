package org.hzt.utils.sequences.primitives;

public interface IntSkipTakeSequence extends IntSequence {

    @Override
    IntSequence skip(long n);

    @Override
    IntSequence take(long n);
}
