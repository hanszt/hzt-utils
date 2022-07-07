package org.hzt.utils.tuples;

import java.util.Objects;

public final class IndexedLongValue {
    private final int index;
    private final long value;

    public IndexedLongValue(int index, long value) {
        this.index = index;
        this.value = value;
    }

    public int index() {
        return index;
    }

    public long value() {
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
        IndexedLongValue that = (IndexedLongValue) obj;
        return this.index == that.index &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, value);
    }

    @Override
    public String toString() {
        return "IndexedLongValue[" +
                "index=" + index + ", " +
                "value=" + value + ']';
    }

}
