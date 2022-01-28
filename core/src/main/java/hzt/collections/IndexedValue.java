package hzt.collections;

import java.util.Objects;

public final class IndexedValue<T> {
    private final int index;
    private final T value;

    public IndexedValue(int index, T value) {
        this.index = index;
        this.value = value;
    }

    public int index() {
        return index;
    }

    public T value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        IndexedValue<T> that = (IndexedValue<T>) obj;
        return this.index == that.index &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, value);
    }

    @Override
    public String toString() {
        return "IndexedValue[" +
                "index=" + index + ", " +
                "value=" + value + ']';
    }

}
