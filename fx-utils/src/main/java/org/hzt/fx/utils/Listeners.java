package org.hzt.fx.utils;

import javafx.beans.value.ChangeListener;

import java.util.function.Consumer;

public final class Listeners {

    private Listeners() {
    }

    public static <N> ChangeListener<N> forNewValue(final Consumer<N> consumer) {
        return (o, c, n) -> consumer.accept(n);
    }
}
