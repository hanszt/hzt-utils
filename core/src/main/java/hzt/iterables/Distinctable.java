package hzt.iterables;

import hzt.collections.MutableLinkedSet;
import hzt.collections.MutableList;
import hzt.collections.MutableSet;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Distinctable<T> extends Iterable<T> {

    default Distinctable<T> distinct() {
        return distinctBy(It::self);
    }

    default <R> Distinctable<T> distinctBy(@NotNull Function<T, ? extends R> selector) {
        return distinctTo(MutableList::empty, selector);
    }

    default <R, C extends Collection<T>> C distinctTo(@NotNull Supplier<C> collectionFactory,
                                                      @NotNull Function<? super T, ? extends R> selector) {
        C c = collectionFactory.get();
        MutableSet<R> set = MutableLinkedSet.empty();
        for (T t : this) {
            if (t != null) {
                final R r = selector.apply(t);
                if (set.add(r)) {
                    c.add(t);
                }
            }
        }
        return c;
    }
}
