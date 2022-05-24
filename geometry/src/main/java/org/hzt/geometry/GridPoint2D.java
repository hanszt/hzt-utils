package org.hzt.geometry;

public interface GridPoint2D {

    GridPoint2D ZERO = new GridPoint2DImpl(0, 0);

    static GridPoint2D from(int x, int y) {
        return new GridPoint2DImpl(x, y);
    }
    int getX();

    int getY();

    default GridPoint2D multiply(GridPoint2D other) {
        return new GridPoint2DImpl(getX() * other.getX(), getY() * other.getY());
    }

    default GridPoint2D multiply(int scalar) {
        return new GridPoint2DImpl(getX() * scalar, getY() * scalar);
    }

    default GridPoint2D add(GridPoint2D other) {
        return new GridPoint2DImpl(getX() + other.getX(), getY() + other.getY());
    }

    default GridPoint2D subtract(GridPoint2D other) {
        return new GridPoint2DImpl(getX() - other.getX(), getY() - other.getY());
    }

    default int gridDistance(int x, int y) {
        return Math.abs(getX() - x) + Math.abs(getY() - y);
    }

    default int gridDistance(GridPoint2D other) {
        return gridDistance(other.getX(), other.getY());
    }

    default int gridMagnitude() {
        return gridDistance(0, 0);
    }

    default GridPoint2D midpoint(GridPoint2D gridPoint2D) {
        return midpoint(gridPoint2D.getX(), gridPoint2D.getY());
    }

    default GridPoint2D midpoint(int x, int y) {
        return new GridPoint2DImpl(
                x + (getX() - x) / 2,
                y + (getY() - y) / 2);
    }

    default double angle(double x, double y) {
        final int ax = getX();
        final int ay = getY();

        final double delta = (ax * x + ay * y) / Math.sqrt(
                (ax * ax + ay * ay) * (x * x + y * y));

        if (delta > 1.0) {
            return 0.0;
        }
        if (delta < -1.0) {
            return 180.0;
        }

        return Math.toDegrees(Math.acos(delta));
    }

    default int dotProduct(int x, int y) {
        return getX() * x + getY() * y;
    }

    default GridPoint3D crossProduct(int x, int y) {
        final int ax = getX();
        final int ay = getY();
        return new GridPoint3DImpl(0, 0, ax * y - ay * x);
    }
}
