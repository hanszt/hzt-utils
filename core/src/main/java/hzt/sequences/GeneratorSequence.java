package hzt.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

final class GeneratorSequence<T> implements Sequence<T> {

    private final Supplier<T> initSupplier;
    private final UnaryOperator<T> nextValueSupplier;

    GeneratorSequence(Supplier<T> initSupplier, UnaryOperator<T> nextValueSupplier) {
        this.initSupplier = initSupplier;
        this.nextValueSupplier = nextValueSupplier;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new GeneratorIterator<>(initSupplier, nextValueSupplier);
    }
}
