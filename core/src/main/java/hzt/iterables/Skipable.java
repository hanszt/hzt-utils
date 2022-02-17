package hzt.iterables;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Skipable<T> extends Iterable<T> {

    default <C extends Collection<T>> C skipTo(Supplier<C> collectionFactory, int count) {
        C collection = collectionFactory.get();
        int counter = 0;
        for (T value : this) {
            if (counter >= count) {
                collection.add(value);
            }
            counter++;
        }
        return collection;
    }

    default  <C extends Collection<T>> C skipWhileTo(
            @NotNull Supplier<C> collectionFactory,
            @NotNull Predicate<? super T> predicate,
            boolean inclusive) {
        boolean yielding = false;
        C list = collectionFactory.get();
        for (T item : this) {
            if (yielding) {
                list.add(item);
                continue;
            }
            if (!predicate.test(item)) {
                if (!inclusive) {
                    list.add(item);
                }
                yielding = true;
            }
        }
        return list;
    }

    Skipable<T> skip(long count);

    Skipable<T> skipWhile(@NotNull Predicate<T> predicate);

    Skipable<T> skipWhileInclusive(@NotNull Predicate<T> predicate);
}
