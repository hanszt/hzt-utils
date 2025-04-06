package org.hzt.utils.sequences;

import org.hzt.utils.iterators.AbstractIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class SequenceExtensions {

    private SequenceExtensions() {
    }

    public static <T> SequenceExtension<T, List<T>> chunked(final int size) {
        return windowed(size, size, true);
    }

    public static <T> SequenceExtension<T, List<T>> windowed(final int size, int step) {
        return windowed(size, step, false);
    }

    public static <T> SequenceExtension<T, List<T>> windowed(final int size) {
        return windowed(size, 1);
    }

    public static <T> SequenceExtension<T, List<T>> windowed(final int size, final int step, final boolean partialWindows) {
        return new SequenceExtension<T, List<T>>() {
            public Sequence<List<T>> extend(final Sequence<T> sequence) {
                return new Sequence<List<T>>() {

                    public Iterator<List<T>> iterator() {
                        return windowedIterator();
                    }

                    private Iterator<List<T>> windowedIterator() {
                        final Iterator<T> iterator = sequence.iterator();
                        return new AbstractIterator<List<T>>() {
                            List<T> next = new ArrayList<T>();
                            int skip = 0;
                            boolean hasNext = false;

                            public boolean hasNext() {
                                if (hasNext) {
                                    return true;
                                }
                                int windowInitCapacity = Math.min(size, 1024);
                                final int gap = step - size;
                                if (gap >= 0) {
                                    computeNextNoOverlap(windowInitCapacity, gap);
                                } else {
                                    computeNextOverlapping(windowInitCapacity);
                                }
                                hasNext = !next.isEmpty();
                                return hasNext;
                            }

                            public List<T> next() {
                                if (hasNext()) {
                                    hasNext = false;
                                    return Collections.unmodifiableList(new ArrayList<T>(next));
                                }
                                throw new NoSuchElementException();
                            }

                            private void computeNextOverlapping(int windowInitCapacity) {
                                next = next.isEmpty() ? new ArrayList<T>(windowInitCapacity) : new ArrayList<T>(next);
                                if (next.isEmpty()) {
                                    while (iterator.hasNext() && next.size() < size) {
                                        next.add(iterator.next());
                                    }
                                } else {
                                    int stepCount = step;
                                    while (stepCount > 0) {
                                        if (!next.isEmpty()) {
                                            next.remove(0);
                                        }
                                        if (iterator.hasNext()) {
                                            next.add(iterator.next());
                                        }
                                        stepCount--;
                                    }
                                }
                                if (!partialWindows && next.size() < size) {
                                    next.clear();
                                }
                            }

                            private void computeNextNoOverlap(int bufferInitCapacity, int gap) {
                                next = new ArrayList<T>(bufferInitCapacity);
                                while (iterator.hasNext()) {
                                    T item = iterator.next();
                                    if (skip > 0) {
                                        skip--;
                                        continue;
                                    }
                                    next.add(item);
                                    if (next.size() == size) {
                                        skip = gap;
                                        return;
                                    }
                                }
                                if (!next.isEmpty() && !partialWindows) {
                                    next.clear();
                                }
                            }
                        };
                    }
                };
            }
        };
    }
}
