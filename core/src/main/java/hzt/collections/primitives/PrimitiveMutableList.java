package hzt.collections.primitives;

/**
 * @param <L> the primitive list iterator
 */
public interface PrimitiveMutableList<L> {

    L listIterator();

    L listIterator(int index);
}
