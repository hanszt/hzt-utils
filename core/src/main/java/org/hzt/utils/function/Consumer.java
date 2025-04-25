package org.hzt.utils.function;

import org.hzt.utils.Objects;

public abstract class Consumer<T> {

    public abstract void accept(T t);

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    public Consumer<T> andThen(final Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return new Consumer<T>() {
            @Override
            public void accept(final T t) {
                Consumer.this.accept(t);
                after.accept(t);
            }
        };
    }
}
