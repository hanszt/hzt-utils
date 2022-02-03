package hzt.sequences;

import hzt.PreConditions;

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

    @Override
    public boolean hasNext() {
        PreConditions.require(state != State.FAILED);
        switch (state) {
            case DONE:
                return false;
            case CONTINUE:
                return true;
            default:
                return tryToComputeNext();
        }
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

    abstract void computeNext();

    protected void setNext(T value) {
        nextValue = value;
        state = State.CONTINUE;
    }

    protected void done() {
        state = State.DONE;
    }

}
