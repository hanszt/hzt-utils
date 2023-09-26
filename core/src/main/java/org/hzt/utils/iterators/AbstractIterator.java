package org.hzt.utils.iterators;

import org.hzt.utils.PreConditions;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A base class to simplify implementing iterators so that implementations only have to implement [computeNext]
 * to implement the iterator, calling [done] when the iteration is complete.
 */
public abstract class AbstractIterator<T> implements Iterator<T> {

    private State state = State.NEXT_UNKNOWN;
    private T nextValue = null;

    protected AbstractIterator() {
    }

    @Override
    public boolean hasNext() {
        PreConditions.require(state != State.FAILED);
        return switch (state) {
            case DONE -> false;
            case CONTINUE -> true;
            default -> tryToComputeNext();
        };
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        state = State.NEXT_UNKNOWN;
        return Objects.requireNonNull(nextValue);
    }

    private boolean tryToComputeNext() {
        state = State.FAILED;
        computeNext();
        return state == State.CONTINUE;
    }

    protected abstract void computeNext();

    protected void setNext(final T value) {
        nextValue = value;
        state = State.CONTINUE;
    }

    protected void done() {
        state = State.DONE;
    }

}
