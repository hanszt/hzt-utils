package org.hzt.geometry;

import java.util.Objects;

final class GridPoint2DImpl implements GridPoint2D {

    private final int x;
    private final int y;

    GridPoint2DImpl(final int x, final int y) {
        this.x = x;
        this.y = y;
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GridPoint2DImpl that = (GridPoint2DImpl) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "GridPoint2DImpl{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
