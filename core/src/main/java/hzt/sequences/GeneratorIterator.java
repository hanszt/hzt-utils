package hzt.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

final class GeneratorIterator<T> implements Iterator<T> {

    private final Supplier<T> initSupplier;
    private final UnaryOperator<T> nextValueSupplier;

    private T nextItem = null;
    // -2 for initial unknown, -1 for next unknown, 0 for done, 1 for continue
    private int nextState = -2;

    GeneratorIterator(Supplier<T> initSupplier, UnaryOperator<T> nextValueSupplier) {
        this.initSupplier = initSupplier;
        this.nextValueSupplier = nextValueSupplier;
    }

    @Override
    public boolean hasNext() {
        if (nextState < 0) {
            calculateNext();
        }
        return nextState == 1;
    }

    @Override
    public T next() {
        if (nextState < 0) {
            calculateNext();
        }
        if (nextState == 0) {
            throw new NoSuchElementException();
        }
        final T result = nextItem;
        // Do not clean nextItem (to avoid keeping reference on yielded instance) -- need to keep state for getNextValue
        nextState = -1;
        return result;
    }

    private void calculateNext() {
        nextItem = nextState == -2 ? initSupplier.get() : nextValueSupplier.apply(nextItem);
        nextState = nextItem == null ? 0 : 1;
    }
}
