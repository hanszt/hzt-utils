package org.hzt.utils.spined_buffers;

/**
 * Base class for a data structure for gathering elements into a buffer and then
 * iterating them. Maintains an array of increasingly sized arrays, so there is
 * no copying cost associated with growing the data structure.
 */
abstract class AbstractSpinedBuffer {
    /**
     * Minimum power-of-two for the first chunk.
     */
    public static final int MIN_CHUNK_POWER = 4;

    /**
     * Max power-of-two for chunks.
     */
    public static final int MAX_CHUNK_POWER = 30;

    /**
     * Minimum array size for array-of-chunks.
     */
    public static final int MIN_SPINE_SIZE = 8;


    /**
     * log2 of the size of the first chunk.
     */
    protected final int initialChunkPower;

    /**
     * Index of the *next* element to write; may point into, or just outside the current chunk.
     */
    protected int elementIndex;

    /**
     * Index of the *current* chunk in the spine array, if the spine array is
     * non-null.
     */
    protected int spineIndex;

    /**
     * Count of elements in all prior chunks.
     */
    protected long[] priorElementCount;

    /**
     * Construct with an initial capacity of 16.
     */
    protected AbstractSpinedBuffer() {
        this.initialChunkPower = MIN_CHUNK_POWER;
    }

    /**
     * Construct with a specified initial capacity.
     *
     * @param initialCapacity The minimum expected number of elements
     */
    protected AbstractSpinedBuffer(final int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity);
        }
        this.initialChunkPower = Math.max(MIN_CHUNK_POWER,
                Integer.SIZE - Integer.numberOfLeadingZeros(initialCapacity - 1));
    }

    /**
     * Is the buffer currently empty?
     */
    public boolean isEmpty() {
        return (spineIndex == 0) && (elementIndex == 0);
    }

    /**
     * How many elements are currently in the buffer?
     */
    public long count() {
        return (spineIndex == 0)
                ? elementIndex
                : (priorElementCount[spineIndex] + elementIndex);
    }

    /**
     * How big should the nth chunk be?
     */
    protected int chunkSize(final int n) {
        final int power = ((n == 0) || (n == 1))
                ? initialChunkPower
                : Math.min((initialChunkPower + n) - 1, AbstractSpinedBuffer.MAX_CHUNK_POWER);
        return 1 << power;
    }

    /**
     * Remove all data from the buffer
     */
    public abstract void clear();
}
