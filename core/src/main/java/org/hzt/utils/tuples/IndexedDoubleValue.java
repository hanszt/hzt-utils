package org.hzt.utils.tuples;

import java.util.Objects;

public final class IndexedDoubleValue {
    private final int index;
    private final double value;

    public IndexedDoubleValue(int index, double value) {
        this.index = index;
        this.value = value;
    }

    public int index() {
        return index;
    }

    public double value() {
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
        IndexedDoubleValue that = (IndexedDoubleValue) obj;
        return this.index == that.index &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, value);
    }

    @Override
    public String toString() {
        return "IndexedDoubleValue[" +
                "index=" + index + ", " +
                "value=" + value + ']';
    }

}
