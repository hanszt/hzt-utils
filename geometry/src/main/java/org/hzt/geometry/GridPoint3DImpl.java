package org.hzt.geometry;

import java.util.Objects;

final class GridPoint3DImpl implements GridPoint3D {

    private final int x;
    private final int y;
    private final int z;

    GridPoint3DImpl(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GridPoint3DImpl that = (GridPoint3DImpl) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "GridPoint3DImpl{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
