package hzt.iterables;

import hzt.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Takeable<T> extends Iterable<T> {

    default <C extends Collection<T>> C takeTo(Supplier<C> collectionFactory, long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        C collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        final Iterable<T> iterable = this;
        if (iterable instanceof Collection) {
            Collection<T> c = (Collection<T>) iterable;
            if (n >= c.size()) {
                collection.addAll(c);
                return collection;
            }
        }
        int count = 0;
        for (T t : this) {
            collection.add(t);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }

    default <C extends Collection<T>> C takeWhileTo(@NotNull Supplier<C> collectionFactory,
                                                    @NotNull Predicate<T> predicate,
                                                    boolean inclusive) {
        final C collection = collectionFactory.get();
        for (T item : this) {
            if (!predicate.test(item)) {
                if (inclusive) {
                    collection.add(item);
                }
                break;
            }
            collection.add(item);
        }
        return collection;
    }

    Takeable<T> take(long n);

    Takeable<T> takeWhile(@NotNull Predicate<T> predicate);

    Takeable<T> takeWhileInclusive(@NotNull Predicate<T> predicate);
}
