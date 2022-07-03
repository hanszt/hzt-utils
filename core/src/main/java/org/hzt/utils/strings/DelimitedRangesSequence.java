package org.hzt.utils.strings;

import org.hzt.utils.iterators.State;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.Pair;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public class DelimitedRangesSequence implements Sequence<IntRange> {

    private final String input;
    private final int startIndex;
    private final int limit;
    private final BiFunction<String, Integer, Pair<Integer, Integer>> getNextMatch;

    public DelimitedRangesSequence(String input,
                                   int startIndex,
                                   int limit,
                                   BiFunction<String, Integer, Pair<Integer, Integer>> getNextMatch) {
        this.input = input;
        this.startIndex = startIndex;
        this.limit = limit;
        this.getNextMatch = getNextMatch;
    }

    @Override
    public Iterator<IntRange> iterator() {
        return new DelimitedRangesIterator();
    }

    @SuppressWarnings("squid:S2972")
    private class DelimitedRangesIterator implements Iterator<IntRange> {

        private State nextState = State.NEXT_UNKNOWN; // -1 for unknown, 0 for done, 1 for continue
        private int currentStartIndex = IntX.coerceIn(startIndex, 0, input.length());
        private int nextSearchIndex = currentStartIndex;
        private IntRange nextItem = null;
        private int counter = 0;

        private void calcNext() {
            if (nextSearchIndex < 0) {
                nextState = State.DONE;
                nextItem = null;
            } else {
                if (((limit > 0) && (++counter >= limit)) || (nextSearchIndex > input.length())) {
                    nextItem = IntRange.of(currentStartIndex, input.length());
                    nextSearchIndex = -1;
                } else {
                    final var match = getNextMatch.apply(input, nextSearchIndex);
                    if (match == null) {
                        nextItem = IntRange.of(currentStartIndex, input.length());
                        nextSearchIndex = -1;
                    } else {
                        final int index = match.first();
                        final int length = match.second();
                        nextItem = IntRange.of(currentStartIndex, index);
                        currentStartIndex = index + length;
                        nextSearchIndex = currentStartIndex + ((length == 0) ? 1 : 0);
                    }
                }
                nextState = State.CONTINUE;
            }
        }

        @Override
        public IntRange next() {
            if (nextState == State.NEXT_UNKNOWN) {
                calcNext();
            }
            if (nextState == State.DONE) {
                throw new NoSuchElementException("Iterator is done");
            }
            final IntRange result = nextItem;
            // Clean next to avoid keeping reference on yielded instance
            nextItem = null;
            nextState = State.NEXT_UNKNOWN;
            return result;
        }

        @Override
        public boolean hasNext() {
            if (nextState == State.NEXT_UNKNOWN) {
                calcNext();
            }
            return nextState == State.CONTINUE;
        }
    }
}
