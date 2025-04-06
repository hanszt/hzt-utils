package org.hzt.demo;

import org.hzt.utils.iterators.AbstractIterator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.SequenceExtension;

import java.util.Iterator;

public final class IO {

    private IO() {
    }

    public static <T> SequenceExtension<T, T> println() {
        return new SequenceExtension<T, T>() {
            public Sequence<T> extend(final Sequence<T> sequence) {
                return new Sequence<T>() {
                    public Iterator<T> iterator() {
                        return new AbstractIterator<T>() {
                            final Iterator<T> iter = sequence.iterator();

                            public boolean hasNext() {
                                return iter.hasNext();
                            }

                            public T next() {
                                final T next = iter.next();
                                System.out.println(next);
                                return next;
                            }
                        };
                    }
                };
            }
        };
    }
}
