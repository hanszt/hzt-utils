package hzt.ranges;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Supplier;

final class RangeHelper {

    private RangeHelper() {
    }

    @NotNull
    static LongRange toLongRange(Supplier<Iterator<Long>> iteratorSupplier) {
        return new LongRange() {
            @NotNull
            @Override
            public Iterator<Long> iterator() {
                // needs to be fetched for every iteration. That's why this is a supplier
                return iteratorSupplier.get();
            }

            @Override
            public @NotNull LongRange get() {
                return this;
            }
        };
    }
}
