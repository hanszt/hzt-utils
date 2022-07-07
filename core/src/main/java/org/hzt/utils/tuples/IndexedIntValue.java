package org.hzt.utils.tuples;

import java.util.Objects;

public final class IndexedIntValue {
    private final int index;
    private final int value;

    public IndexedIntValue(int index, int value) {
        this.index = index;
        this.value = value;
    }

    public int index() {
        return index;
    }

    public int value() {
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
        IndexedIntValue that = (IndexedIntValue) obj;
        return this.index == that.index &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, value);
    }

    @Override
    public String toString() {
        return "IndexedIntValue[" +
                "index=" + index + ", " +
                "value=" + value + ']';
    }

}
