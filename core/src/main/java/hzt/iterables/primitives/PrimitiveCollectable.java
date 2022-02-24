package hzt.iterables.primitives;

/**
 * @param <C> the primitive collection type
 */
public interface PrimitiveCollectable<C> {

    C toListX();

    C toMutableList();
}
