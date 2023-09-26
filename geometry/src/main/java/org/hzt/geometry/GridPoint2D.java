package org.hzt.geometry;

public interface GridPoint2D {

    GridPoint2D ZERO = new GridPoint2DImpl(0, 0);

    static GridPoint2D from(final int x, final int y) {
        return new GridPoint2DImpl(x, y);
    }
    int getX();

    int getY();

    default GridPoint2D multiply(final GridPoint2D other) {
        return new GridPoint2DImpl(getX() * other.getX(), getY() * other.getY());
    }

    default GridPoint2D multiply(final int scalar) {
        return new GridPoint2DImpl(getX() * scalar, getY() * scalar);
    }

    default GridPoint2D add(final GridPoint2D other) {
        return new GridPoint2DImpl(getX() + other.getX(), getY() + other.getY());
    }

    default GridPoint2D subtract(final GridPoint2D other) {
        return new GridPoint2DImpl(getX() - other.getX(), getY() - other.getY());
    }

    default int distanceSquared(final int x, final int y) {
        return x * x + y * y;
    }

    default int distanceSquared(final GridPoint2D other) {
        return distanceSquared(other.getX(), other.getY());
    }

    default double distance(final int x, final int y) {
        return Math.sqrt(distanceSquared(x, y));
    }

    default double distance(final GridPoint2D other) {
        return distance(other.getX(), other.getY());
    }

    default int gridDistance(final int x, final int y) {
        return Math.abs(getX() - x) + Math.abs(getY() - y);
    }

    default int gridDistance(final GridPoint2D other) {
        return gridDistance(other.getX(), other.getY());
    }

    default int gridMagnitude() {
        return gridDistance(ZERO);
    }

    default double magnitude() {
        return distance(ZERO);
    }

    default GridPoint2D midpoint(final GridPoint2D gridPoint2D) {
        return midpoint(gridPoint2D.getX(), gridPoint2D.getY());
    }

    default GridPoint2D midpoint(final int x, final int y) {
        return new GridPoint2DImpl(
                x + (getX() - x) / 2,
                y + (getY() - y) / 2);
    }

    default double angle(final double x, final double y) {
        final var ax = getX();
        final var ay = getY();

        final var delta = (ax * x + ay * y) / Math.sqrt(
                (ax * ax + ay * ay) * (x * x + y * y));

        if (delta > 1.0) {
            return 0.0;
        }
        if (delta < -1.0) {
            return 180.0;
        }

        return Math.toDegrees(Math.acos(delta));
    }

    default int dotProduct(final int x, final int y) {
        return getX() * x + getY() * y;
    }

    default int dotProduct(final GridPoint2D point2D) {
        return dotProduct(point2D.getX(), point2D.getY());
    }

    default GridPoint3D crossProduct(final int x, final int y) {
        final var ax = getX();
        final var ay = getY();
        return new GridPoint3DImpl(0, 0, ax * y - ay * x);
    }

    default GridPoint3D crossProduct(final GridPoint2D point2D) {
        return crossProduct(point2D.getX(), point2D.getY());
    }
}
