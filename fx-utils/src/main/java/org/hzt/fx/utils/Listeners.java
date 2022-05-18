package org.hzt.fx.utils;

import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;

public final class Listeners {

    private Listeners() {
    }

    public static <N> ChangeListener<N> forNewValue(Consumer<N> consumer) {
        return (o, c, n) -> consumer.accept(n);
    }
}
