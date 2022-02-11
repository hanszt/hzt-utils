package hzt.sequences;

import hzt.iterators.MultiMappingIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MultiMappingSequence<T, R> implements Sequence<R> {

    private final Sequence<T> upstream;
    private final BiConsumer<? super T, ? super Consumer<R>> mapper;

    public MultiMappingSequence(Sequence<T> upstream, BiConsumer<? super T, ? super Consumer<R>> mapper) {
        this.upstream = upstream;
        this.mapper = mapper;
    }

    @NotNull
    @Override
    public Iterator<R> iterator() {
        return new MultiMappingIterator<>(upstream.iterator(), mapper);
    }
}
