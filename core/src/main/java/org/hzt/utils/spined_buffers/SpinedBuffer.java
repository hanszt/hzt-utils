package org.hzt.utils.spined_buffers;

import org.hzt.utils.PreConditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 * An ordered collection of elements.  Elements can be added, but not removed.
 * Goes through a building phase, during which elements can be added, and a
 * traversal phase, during which elements can be traversed in order but no
 * further modifications are possible.
 *
 * <p> One or more arrays are used to store elements. The use of a multiple
 * arrays has better performance characteristics than a single array used by
 * {@link ArrayList}, as when the capacity of the list needs to be increased
 * no copying of elements is required.  This is usually beneficial in the case
 * where the results will be traversed a small number of times.
 *
 * @param <E> the type of elements in this list
 */
public class SpinedBuffer<E>
        extends AbstractSpinedBuffer
        implements Consumer<E>, Iterable<E> {

    private static final long MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8L;

    // IllegalArgumentException messages
    private static final String BAD_SIZE = "Size exceeds max array size";

    private static final int SPLITERATOR_CHARACTERISTICS = Spliterator.SIZED | Spliterator.ORDERED | Spliterator.SUBSIZED;
    private static final String CHUNK_TO_STRING_FORMAT = "%s[length=%d, chunks=%d]%s";

    /*
     * We optimistically hope that all the data will fit into the first chunk,
     * so we try to avoid inflating the spine[] and priorElementCount[] arrays
     * prematurely.  So methods must be prepared to deal with these arrays being
     * null.  If spine is non-null, then spineIndex points to the current chunk
     * within the spine, otherwise it is zero.  The spine and priorElementCount
     * arrays are always the same size, and for any i <= spineIndex,
     * priorElementCount[i] is the sum of the sizes of all the prior chunks.
     *
     * The curChunk pointer is always valid.  The elementIndex is the index of
     * the next element to be written in curChunk; this may be past the end of
     * curChunk, so we have to check before writing. When we inflate the spine
     * array, curChunk becomes the first element in it.  When we clear the
     * buffer, we discard all chunks except the first one, which we clear,
     * restoring it to the initial single-chunk state.
     */

    /**
     * Chunk that we're currently writing into; may or may not be aliased with
     * the first element of the spine.
     */
    protected E[] curChunk;

    /**
     * All chunks, or null if there is only one chunk.
     */
    protected E[][] spine;

    /**
     * Constructs an empty list with an initial capacity of sixteen.
     */
    @SuppressWarnings("unchecked")
    public SpinedBuffer() {
        super();
        curChunk = (E[]) new Object[1 << initialChunkPower];
    }

    /**
     * Returns the current capacity of the buffer
     */
    protected long capacity() {
        return (spineIndex == 0)
                ? curChunk.length
                : (priorElementCount[spineIndex] + spine[spineIndex].length);
    }

    @SuppressWarnings("unchecked")
    private void inflateSpine() {
        if (spine == null) {
            spine = (E[][]) new Object[MIN_SPINE_SIZE][];
            priorElementCount = new long[MIN_SPINE_SIZE];
            spine[0] = curChunk;
        }
    }

    /**
     * Ensure that the buffer has at least capacity to hold the target size
     */
    @SuppressWarnings("unchecked")
    protected final void ensureCapacity(final long targetSize) {
        var capacity = capacity();
        if (targetSize > capacity) {
            inflateSpine();
            for (var i = spineIndex + 1; targetSize > capacity; i++) {
                if (i >= spine.length) {
                    final var newSpineSize = spine.length * 2;
                    spine = Arrays.copyOf(spine, newSpineSize);
                    priorElementCount = Arrays.copyOf(priorElementCount, newSpineSize);
                }
                final var nextChunkSize = chunkSize(i);
                spine[i] = (E[]) new Object[nextChunkSize];
                priorElementCount[i] = priorElementCount[i - 1] + spine[i - 1].length;
                capacity += nextChunkSize;
            }
        }
    }

    /**
     * Force the buffer to increase its capacity.
     */
    protected void increaseCapacity() {
        ensureCapacity(capacity() + 1);
    }

    /**
     * Retrieve the element at the specified index.
     */
    public E get(final long index) {
        // @@@ can further optimize by caching last seen spineIndex,
        // which is going to be right most of the time

        // Casts to int are safe since the spine array index is the index minus
        // the prior element count from the current spine
        if (spineIndex == 0) {
            if (index < elementIndex) {
                return curChunk[((int) index)];
            } else {
                throw new IndexOutOfBoundsException(Long.toString(index));
            }
        }

        if (index >= count()) {
            throw new IndexOutOfBoundsException(Long.toString(index));
        }

        for (var j = 0; j <= spineIndex; j++) {
            if (index < (priorElementCount[j] + spine[j].length)) {
                return spine[j][((int) (index - priorElementCount[j]))];
            }
        }

        throw new IndexOutOfBoundsException(Long.toString(index));
    }

    @Override
    public void clear() {
        if (spine != null) {
            curChunk = spine[0];
            Arrays.fill(curChunk, null);
            spine = null;
            priorElementCount = null;
        } else {
            for (var i = 0; i < elementIndex; i++) {
                curChunk[i] = null;
            }
        }
        elementIndex = 0;
        spineIndex = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    public void forEach(final Consumer<? super E> consumer) {
        // completed chunks, if any
        for (var j = 0; j < spineIndex; j++) {
            for (final var t : spine[j]) {
                consumer.accept(t);
            }
        }

        // current chunk
        for (var i = 0; i < elementIndex; i++) {
            consumer.accept(curChunk[i]);
        }
    }

    @Override
    public void accept(final E e) {
        if (elementIndex == curChunk.length) {
            inflateSpine();
            if (((spineIndex + 1) >= spine.length) || (spine[spineIndex + 1] == null)) {
                increaseCapacity();
            }
            elementIndex = 0;
            ++spineIndex;
            curChunk = spine[spineIndex];
        }
        curChunk[elementIndex++] = e;
    }

    @Override
    public String toString() {
        final List<E> list = new ArrayList<>();
        forEach(list::add);
        return "SpinedBuffer:" + list;
    }

    /**
     * Return a {@link Spliterator} describing the contents of the buffer.
     */
    @Override
    @SuppressWarnings({"squid:S3776", "squid:S138"})
    public Spliterator<E> spliterator() {
        class Splitr implements Spliterator<E> {
            // The current spine index
            private int splSpineIndex;

            // Last spine index
            private final int lastSpineIndex;

            // The current element index into the current spine
            private int splElementIndex;

            // Last spine's last element index + 1
            private final int lastSpineElementFence;

            // When splSpineIndex >= lastSpineIndex and
            // splElementIndex >= lastSpineElementFence then
            // this spliterator is fully traversed
            // tryAdvance can set splSpineIndex > spineIndex if the last spine is full

            // The current spine array
            private E[] splChunk;

            Splitr(final int firstSpineIndex, final int lastSpineIndex,
                   final int firstSpineElementIndex, final int lastSpineElementFence) {
                //noinspection DuplicatedCode
                this.splSpineIndex = firstSpineIndex;
                this.lastSpineIndex = lastSpineIndex;
                this.splElementIndex = firstSpineElementIndex;
                this.lastSpineElementFence = lastSpineElementFence;
                PreConditions.require (spine != null || ((firstSpineIndex == 0) && (lastSpineIndex == 0)));
                splChunk = (spine == null) ? curChunk : spine[firstSpineIndex];
            }

            @Override
            public long estimateSize() {
                return (splSpineIndex == lastSpineIndex)
                        ? ((long) lastSpineElementFence - splElementIndex)
                        // # of elements prior to end -
                        : ((priorElementCount[lastSpineIndex] + lastSpineElementFence) -
                                // # of elements prior to current
                                priorElementCount[splSpineIndex] - splElementIndex);
            }

            @Override
            public int characteristics() {
                return SPLITERATOR_CHARACTERISTICS;
            }

            @Override
            public boolean tryAdvance(final Consumer<? super E> consumer) {
                Objects.requireNonNull(consumer);

                if ((splSpineIndex < lastSpineIndex)
                        || ((splSpineIndex == lastSpineIndex) && (splElementIndex < lastSpineElementFence))) {
                    consumer.accept(splChunk[splElementIndex++]);

                    if (splElementIndex == splChunk.length) {
                        splElementIndex = 0;
                        ++splSpineIndex;
                        if ((spine != null) && (splSpineIndex <= lastSpineIndex)) {
                            splChunk = spine[splSpineIndex];
                        }
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void forEachRemaining(final Consumer<? super E> consumer) {
                Objects.requireNonNull(consumer);

                if ((splSpineIndex < lastSpineIndex)
                        || ((splSpineIndex == lastSpineIndex) && (splElementIndex < lastSpineElementFence))) {
                    var i = splElementIndex;
                    // completed chunks, if any
                    for (var sp = splSpineIndex; sp < lastSpineIndex; sp++) {
                        final var chunk = spine[sp];
                        for (; i < chunk.length; i++) {
                            consumer.accept(chunk[i]);
                        }
                        i = 0;
                    }
                    // last (or current uncompleted) chunk
                    final var chunk = (splSpineIndex == lastSpineIndex) ? splChunk : spine[lastSpineIndex];
                    for (; i < lastSpineElementFence; i++) {
                        consumer.accept(chunk[i]);
                    }
                    // mark consumed
                    splSpineIndex = lastSpineIndex;
                    splElementIndex = lastSpineElementFence;
                }
            }

            @Override
            public Spliterator<E> trySplit() {
                if (splSpineIndex < lastSpineIndex) {
                    // split just before last chunk (if it is full this means 50:50 split)
                    final Spliterator<E> ret = new Splitr(splSpineIndex, lastSpineIndex - 1,
                            splElementIndex, spine[lastSpineIndex - 1].length);
                    // position to start of last chunk
                    splSpineIndex = lastSpineIndex;
                    splElementIndex = 0;
                    splChunk = spine[splSpineIndex];
                    return ret;
                } else if (splSpineIndex == lastSpineIndex) {
                    final var t = (lastSpineElementFence - splElementIndex) / 2;
                    if (t == 0) {
                        return null;
                    } else {
                        final var ret = Arrays.spliterator(splChunk, splElementIndex, splElementIndex + t);
                        splElementIndex += t;
                        return ret;
                    }
                } else {
                    return null;
                }
            }
        }
        return new Splitr(0, spineIndex, 0, elementIndex);
    }

    /**
     * An ordered collection of primitive values.  Elements can be added, but
     * not removed. Goes through a building phase, during which elements can be
     * added, and a traversal phase, during which elements can be traversed in
     * order but no further modifications are possible.
     *
     * <p> One or more arrays are used to store elements. The use of a multiple
     * arrays has better performance characteristics than a single array used by
     * {@link ArrayList}, as when the capacity of the list needs to be increased
     * no copying of elements is required.  This is usually beneficial in the case
     * where the results will be traversed a small number of times.
     *
     * @param <E>      the wrapper type for this primitive type
     * @param <T_ARR>  the array type for this primitive type
     * @param <T_CONS> the Consumer type for this primitive type
     */
    @SuppressWarnings({"squid:S1699", "squid:S119", "squid:S2972"})
    abstract static class OfPrimitive<E, T_ARR, T_CONS>
            extends AbstractSpinedBuffer implements Iterable<E> {

        /*
         * We optimistically hope that all the data will fit into the first chunk,
         * so we try to avoid inflating the spine[] and priorElementCount[] arrays
         * prematurely.  So methods must be prepared to deal with these arrays being
         * null.  If spine is non-null, then spineIndex points to the current chunk
         * within the spine, otherwise it is zero.  The spine and priorElementCount
         * arrays are always the same size, and for any i <= spineIndex,
         * priorElementCount[i] is the sum of the sizes of all the prior chunks.
         *
         * The curChunk pointer is always valid.  The elementIndex is the index of
         * the next element to be written in curChunk; this may be past the end of
         * curChunk, so we have to check before writing. When we inflate the spine
         * array, curChunk becomes the first element in it.  When we clear the
         * buffer, we discard all chunks except the first one, which we clear,
         * restoring it to the initial single-chunk state.
         */

        // The chunk we're currently writing into
       protected T_ARR curChunk;

        // All chunks, or null if there is only one chunk
        protected T_ARR[] spine;

        /**
         * Constructs an empty list with the specified initial capacity.
         *
         * @param initialCapacity the initial capacity of the list
         * @throws IllegalArgumentException if the specified initial capacity
         *                                  is negative
         */
        OfPrimitive(final int initialCapacity) {
            super(initialCapacity);
            curChunk = newArray(1 << initialChunkPower);
        }

        /**
         * Constructs an empty list with an initial capacity of sixteen.
         */
        OfPrimitive() {
            super();
            curChunk = newArray(1 << initialChunkPower);
        }

        /**
         * Create a new array-of-array of the proper type and size
         */
        protected abstract T_ARR[] newArrayArray();

        /**
         * Create a new array of the proper type and size
         */
        public abstract T_ARR newArray(int size);

        /**
         * Get the length of an array
         */
        protected abstract int arrayLength(T_ARR array);

        /**
         * Iterate an array with the provided consumer
         */
        protected abstract void arrayForEach(T_ARR array, int from, int to,
                                             T_CONS consumer);

        protected long capacity() {
            return (spineIndex == 0)
                    ? arrayLength(curChunk)
                    : (priorElementCount[spineIndex] + arrayLength(spine[spineIndex]));
        }

        private void inflateSpine() {
            if (spine == null) {
                spine = newArrayArray();
                priorElementCount = new long[MIN_SPINE_SIZE];
                spine[0] = curChunk;
            }
        }

        protected final void ensureCapacity(final long targetSize) {
            var capacity = capacity();
            if (targetSize > capacity) {
                inflateSpine();
                for (var i = spineIndex + 1; targetSize > capacity; i++) {
                    if (i >= spine.length) {
                        final var newSpineSize = spine.length * 2;
                        spine = Arrays.copyOf(spine, newSpineSize);
                        priorElementCount = Arrays.copyOf(priorElementCount, newSpineSize);
                    }
                    final var nextChunkSize = chunkSize(i);
                    spine[i] = newArray(nextChunkSize);
                    priorElementCount[i] = priorElementCount[i - 1] + arrayLength(spine[i - 1]);
                    capacity += nextChunkSize;
                }
            }
        }

        protected void increaseCapacity() {
            ensureCapacity(capacity() + 1);
        }

        protected int chunkFor(final long index) {
            if (spineIndex == 0) {
                if (index < elementIndex) {
                    return 0;
                } else {
                    throw new IndexOutOfBoundsException(Long.toString(index));
                }
            }

            if (index >= count()) {
                throw new IndexOutOfBoundsException(Long.toString(index));
            }

            for (var j = 0; j <= spineIndex; j++) {
                if (index < (priorElementCount[j] + arrayLength(spine[j]))) {
                    return j;
                }
            }

            throw new IndexOutOfBoundsException(Long.toString(index));
        }

        @SuppressWarnings("SuspiciousSystemArraycopy")
        public void copyInto(final T_ARR array, int offset) {
            final var finalOffset = offset + count();
            if ((finalOffset > arrayLength(array)) || (finalOffset < offset)) {
                throw new IndexOutOfBoundsException("does not fit");
            }

            if (spineIndex == 0) {
                System.arraycopy(curChunk, 0, array, offset, elementIndex);
            } else {
                // full chunks
                for (var i = 0; i < spineIndex; i++) {
                    System.arraycopy(spine[i], 0, array, offset, arrayLength(spine[i]));
                    offset += arrayLength(spine[i]);
                }
                if (elementIndex > 0) {
                    System.arraycopy(curChunk, 0, array, offset, elementIndex);
                }
            }
        }

        public T_ARR asPrimitiveArray() {
            final var size = count();
            if (size >= MAX_ARRAY_SIZE) {
                throw new IllegalArgumentException(BAD_SIZE);
            }
            final var result = newArray((int) size);
            copyInto(result, 0);
            return result;
        }

        protected void preAccept() {
            if (elementIndex == arrayLength(curChunk)) {
                inflateSpine();
                if (((spineIndex + 1) >= spine.length) || (spine[spineIndex + 1] == null)) {
                    increaseCapacity();
                }
                elementIndex = 0;
                ++spineIndex;
                curChunk = spine[spineIndex];
            }
        }

        public void clear() {
            if (spine != null) {
                curChunk = spine[0];
                spine = null;
                priorElementCount = null;
            }
            elementIndex = 0;
            spineIndex = 0;
        }

        public void forEach(final T_CONS consumer) {
            // completed chunks, if any
            for (var j = 0; j < spineIndex; j++) {
                arrayForEach(spine[j], 0, arrayLength(spine[j]), consumer);
            }

            // current chunk
            arrayForEach(curChunk, 0, elementIndex, consumer);
        }

        abstract class BaseSpliterator<T_SPLITR extends Spliterator.OfPrimitive<E, T_CONS, T_SPLITR>>
                implements Spliterator.OfPrimitive<E, T_CONS, T_SPLITR> {
            // The current spine index
            private int splSpineIndex;

            // Last spine index
            private final int lastSpineIndex;

            // The current element index into the current spine
            private int splElementIndex;

            // Last spine's last element index + 1
            private final int lastSpineElementFence;

            // When splSpineIndex >= lastSpineIndex and
            // splElementIndex >= lastSpineElementFence then
            // this spliterator is fully traversed
            // tryAdvance can set splSpineIndex > spineIndex if the last spine is full

            // The current spine array
            private T_ARR splChunk;

            BaseSpliterator(final int firstSpineIndex, final int lastSpineIndex,
                            final int firstSpineElementIndex, final int lastSpineElementFence) {
                //noinspection DuplicatedCode
                this.splSpineIndex = firstSpineIndex;
                this.lastSpineIndex = lastSpineIndex;
                this.splElementIndex = firstSpineElementIndex;
                this.lastSpineElementFence = lastSpineElementFence;
                PreConditions.require(spine != null || ((firstSpineIndex == 0) && (lastSpineIndex == 0)));
                splChunk = (spine == null) ? curChunk : spine[firstSpineIndex];
            }

            abstract T_SPLITR newSpliterator(int firstSpineIndex, int lastSpineIndex,
                                             int firstSpineElementIndex, int lastSpineElementFence);

            abstract void arrayForOne(T_ARR array, int index, T_CONS consumer);

            abstract T_SPLITR arraySpliterator(T_ARR array, int offset, int len);

            @Override
            public long estimateSize() {
                return (splSpineIndex == lastSpineIndex)
                        ? ((long) lastSpineElementFence - splElementIndex)
                        // # of elements prior to end -
                        : ((priorElementCount[lastSpineIndex] + lastSpineElementFence) -
                                // # of elements prior to current
                                priorElementCount[splSpineIndex] - splElementIndex);
            }

            @Override
            public int characteristics() {
                return SPLITERATOR_CHARACTERISTICS;
            }

            @Override
            public boolean tryAdvance(final T_CONS consumer) {
                Objects.requireNonNull(consumer);

                if ((splSpineIndex < lastSpineIndex)
                        || ((splSpineIndex == lastSpineIndex) && (splElementIndex < lastSpineElementFence))) {
                    arrayForOne(splChunk, splElementIndex++, consumer);

                    if (splElementIndex == arrayLength(splChunk)) {
                        splElementIndex = 0;
                        ++splSpineIndex;
                        if ((spine != null) && (splSpineIndex <= lastSpineIndex)) {
                            splChunk = spine[splSpineIndex];
                        }
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void forEachRemaining(final T_CONS consumer) {
                Objects.requireNonNull(consumer);

                if ((splSpineIndex < lastSpineIndex)
                        || ((splSpineIndex == lastSpineIndex) && (splElementIndex < lastSpineElementFence))) {
                    var i = splElementIndex;
                    // completed chunks, if any
                    for (var sp = splSpineIndex; sp < lastSpineIndex; sp++) {
                        final var chunk = spine[sp];
                        arrayForEach(chunk, i, arrayLength(chunk), consumer);
                        i = 0;
                    }
                    // last (or current uncompleted) chunk
                    final var chunk = (splSpineIndex == lastSpineIndex) ? splChunk : spine[lastSpineIndex];
                    arrayForEach(chunk, i, lastSpineElementFence, consumer);
                    // mark consumed
                    splSpineIndex = lastSpineIndex;
                    splElementIndex = lastSpineElementFence;
                }
            }

            @Override
            public T_SPLITR trySplit() {
                if (splSpineIndex < lastSpineIndex) {
                    // split just before last chunk (if it is full this means 50:50 split)
                    final var ret = newSpliterator(splSpineIndex, lastSpineIndex - 1,
                            splElementIndex, arrayLength(spine[lastSpineIndex - 1]));
                    // position us to start of last chunk
                    splSpineIndex = lastSpineIndex;
                    splElementIndex = 0;
                    splChunk = spine[splSpineIndex];
                    return ret;
                } else if (splSpineIndex == lastSpineIndex) {
                    final var t = (lastSpineElementFence - splElementIndex) / 2;
                    if (t == 0) {
                        return null;
                    } else {
                        final var ret = arraySpliterator(splChunk, splElementIndex, t);
                        splElementIndex += t;
                        return ret;
                    }
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * An ordered collection of {@code int} values.
     */
    @SuppressWarnings("squid:S2972")
    public static class OfInt extends SpinedBuffer.OfPrimitive<Integer, int[], IntConsumer>
            implements IntConsumer {
        public OfInt() {
        }

        OfInt(final int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        public void forEach(final Consumer<? super Integer> consumer) {
            if (consumer instanceof final IntConsumer intConsumer) {
                super.forEach(intConsumer);
            } else {
                spliterator().forEachRemaining(consumer);
            }
        }

        @Override
        protected int[][] newArrayArray() {
            return new int[AbstractSpinedBuffer.MIN_SPINE_SIZE][];
        }

        @Override
        public int[] newArray(final int size) {
            return new int[size];
        }

        @Override
        protected int arrayLength(final int[] array) {
            return array.length;
        }

        @Override
        protected void arrayForEach(final int[] array,
                                    final int from, final int to,
                                    final IntConsumer consumer) {
            for (var i = from; i < to; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public void accept(final int i) {
            preAccept();
            curChunk[elementIndex++] = i;
        }

        public int get(final long index) {
            // Casts to int are safe since the spine array index is the index minus
            // the prior element count from the current spine
            final var ch = chunkFor(index);
            return ((spineIndex == 0) && (ch == 0)) ? curChunk[(int) index] : spine[ch][(int) (index - priorElementCount[ch])];
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        public Spliterator.OfInt spliterator() {
            class Splitr extends BaseSpliterator<Spliterator.OfInt>
                    implements Spliterator.OfInt {
                Splitr(final int firstSpineIndex, final int lastSpineIndex,
                       final int firstSpineElementIndex, final int lastSpineElementFence) {
                    super(firstSpineIndex, lastSpineIndex,
                            firstSpineElementIndex, lastSpineElementFence);
                }

                @Override
                Splitr newSpliterator(final int firstSpineIndex, final int lastSpineIndex,
                                      final int firstSpineElementIndex, final int lastSpineElementFence) {
                    return new Splitr(firstSpineIndex, lastSpineIndex,
                            firstSpineElementIndex, lastSpineElementFence);
                }

                @Override
                void arrayForOne(final int[] array, final int index, final IntConsumer consumer) {
                    consumer.accept(array[index]);
                }

                @Override
                Spliterator.OfInt arraySpliterator(final int[] array, final int offset, final int len) {
                    return Arrays.spliterator(array, offset, offset + len);
                }
            }
            return new Splitr(0, spineIndex, 0, elementIndex);
        }

        @Override
        public String toString() {
            final var array = asPrimitiveArray();
            if (array.length < 200) {
                return String.format(CHUNK_TO_STRING_FORMAT,
                        getClass().getSimpleName(), array.length,
                        spineIndex, Arrays.toString(array));
            } else {
                final var array2 = Arrays.copyOf(array, 200);
                return String.format(CHUNK_TO_STRING_FORMAT + "...",
                        getClass().getSimpleName(), array.length,
                        spineIndex, Arrays.toString(array2));
            }
        }
    }

    /**
     * An ordered collection of {@code long} values.
     */
    @SuppressWarnings("squid:S2972")
    public static class OfLong extends SpinedBuffer.OfPrimitive<Long, long[], LongConsumer>
            implements LongConsumer {
        public OfLong() {
        }

        OfLong(final int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        public void forEach(final Consumer<? super Long> consumer) {
            if (consumer instanceof final LongConsumer longConsumer) {
                super.forEach(longConsumer);
            } else {
                spliterator().forEachRemaining(consumer);
            }
        }

        @Override
        protected long[][] newArrayArray() {
            return new long[AbstractSpinedBuffer.MIN_SPINE_SIZE][];
        }

        @Override
        public long[] newArray(final int size) {
            return new long[size];
        }

        @Override
        protected int arrayLength(final long[] array) {
            return array.length;
        }

        @Override
        protected void arrayForEach(final long[] array,
                                    final int from, final int to,
                                    final LongConsumer consumer) {
            for (var i = from; i < to; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public void accept(final long i) {
            preAccept();
            curChunk[elementIndex++] = i;
        }

        public long get(final long index) {
            // Casts to int are safe since the spine array index is the index minus
            // the prior element count from the current spine
            final var ch = chunkFor(index);
            return ((spineIndex == 0) && (ch == 0)) ? curChunk[(int) index] :
                    spine[ch][(int) (index - priorElementCount[ch])];
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return Spliterators.iterator(spliterator());
        }


        @Override
        public Spliterator.OfLong spliterator() {
            class Splitr extends BaseSpliterator<Spliterator.OfLong>
                    implements Spliterator.OfLong {
                Splitr(final int firstSpineIndex, final int lastSpineIndex,
                       final int firstSpineElementIndex, final int lastSpineElementFence) {
                    super(firstSpineIndex, lastSpineIndex,
                            firstSpineElementIndex, lastSpineElementFence);
                }

                @Override
                Splitr newSpliterator(final int firstSpineIndex, final int lastSpineIndex,
                                      final int firstSpineElementIndex, final int lastSpineElementFence) {
                    return new Splitr(firstSpineIndex, lastSpineIndex,
                            firstSpineElementIndex, lastSpineElementFence);
                }

                @Override
                void arrayForOne(final long[] array, final int index, final LongConsumer consumer) {
                    consumer.accept(array[index]);
                }

                @Override
                Spliterator.OfLong arraySpliterator(final long[] array, final int offset, final int len) {
                    return Arrays.spliterator(array, offset, offset + len);
                }
            }
            return new Splitr(0, spineIndex, 0, elementIndex);
        }

        @Override
        public String toString() {
            final var array = asPrimitiveArray();
            if (array.length < 200) {
                return String.format(CHUNK_TO_STRING_FORMAT,
                        getClass().getSimpleName(), array.length,
                        spineIndex, Arrays.toString(array));
            } else {
                final var array2 = Arrays.copyOf(array, 200);
                return String.format(CHUNK_TO_STRING_FORMAT + "...",
                        getClass().getSimpleName(), array.length,
                        spineIndex, Arrays.toString(array2));
            }
        }
    }

    /**
     * An ordered collection of {@code double} values.
     */
    @SuppressWarnings("squid:S2972")
    public static class OfDouble
            extends SpinedBuffer.OfPrimitive<Double, double[], DoubleConsumer>
            implements DoubleConsumer {
        public OfDouble() {
        }

        OfDouble(final int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        public void forEach(final Consumer<? super Double> consumer) {
            if (consumer instanceof final DoubleConsumer doubleConsumer) {
                super.forEach(doubleConsumer);
            } else {
                spliterator().forEachRemaining(consumer);
            }
        }

        @Override
        protected double[][] newArrayArray() {
            return new double[AbstractSpinedBuffer.MIN_SPINE_SIZE][];
        }

        @Override
        public double[] newArray(final int size) {
            return new double[size];
        }

        @Override
        protected int arrayLength(final double[] array) {
            return array.length;
        }

        @Override
        protected void arrayForEach(final double[] array,
                                    final int from, final int to,
                                    final DoubleConsumer consumer) {
            for (var i = from; i < to; i++) {
                consumer.accept(array[i]);
            }
        }

        @Override
        public void accept(final double i) {
            preAccept();
            curChunk[elementIndex++] = i;
        }

        public double get(final long index) {
            // Casts to int are safe since the spine array index is the index minus
            // the prior element count from the current spine
            final var ch = chunkFor(index);
            if ((spineIndex == 0) && (ch == 0)) {
                return curChunk[(int) index];
            } else {
                return spine[ch][(int) (index - priorElementCount[ch])];
            }
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        public Spliterator.OfDouble spliterator() {
            class Splitr extends BaseSpliterator<Spliterator.OfDouble>
                    implements Spliterator.OfDouble {
                Splitr(final int firstSpineIndex, final int lastSpineIndex,
                       final int firstSpineElementIndex, final int lastSpineElementFence) {
                    super(firstSpineIndex, lastSpineIndex,
                            firstSpineElementIndex, lastSpineElementFence);
                }

                @Override
                Splitr newSpliterator(final int firstSpineIndex, final int lastSpineIndex,
                                      final int firstSpineElementIndex, final int lastSpineElementFence) {
                    return new Splitr(firstSpineIndex, lastSpineIndex,
                            firstSpineElementIndex, lastSpineElementFence);
                }

                @Override
                void arrayForOne(final double[] array, final int index, final DoubleConsumer consumer) {
                    consumer.accept(array[index]);
                }

                @Override
                Spliterator.OfDouble arraySpliterator(final double[] array, final int offset, final int len) {
                    return Arrays.spliterator(array, offset, offset + len);
                }
            }
            return new Splitr(0, spineIndex, 0, elementIndex);
        }

        @Override
        public String toString() {
            final var array = asPrimitiveArray();
            if (array.length < 200) {
                return String.format(CHUNK_TO_STRING_FORMAT,
                        getClass().getSimpleName(), array.length,
                        spineIndex, Arrays.toString(array));
            } else {
                final var array2 = Arrays.copyOf(array, 200);
                return String.format(CHUNK_TO_STRING_FORMAT + "...",
                        getClass().getSimpleName(), array.length,
                        spineIndex, Arrays.toString(array2));
            }
        }
    }
}
