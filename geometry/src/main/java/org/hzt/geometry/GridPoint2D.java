package org.hzt.geometry;

public interface GridPoint2D {

    GridPoint2D ZERO = new StandardGridPoint2D(0, 0);

    static GridPoint2D from(final int x, final int y) {
        return new StandardGridPoint2D(x, y);
    }
    int x();

    int y();

    default GridPoint2D multiply(final GridPoint2D other) {
        return new StandardGridPoint2D(x() * other.x(), y() * other.y());
    }

    default GridPoint2D multiply(final int scalar) {
        return new StandardGridPoint2D(x() * scalar, y() * scalar);
    }

    default GridPoint2D add(final GridPoint2D other) {
        return new StandardGridPoint2D(x() + other.x(), y() + other.y());
    }

    default GridPoint2D subtract(final GridPoint2D other) {
        return new StandardGridPoint2D(x() - other.x(), y() - other.y());
    }

    default int distanceSquared(final int x, final int y) {
        return x * x + y * y;
    }

    default int distanceSquared(final GridPoint2D other) {
        return distanceSquared(other.x(), other.y());
    }

    default double distance(final int x, final int y) {
        return Math.sqrt(distanceSquared(x, y));
    }

    default double distance(final GridPoint2D other) {
        return distance(other.x(), other.y());
    }

    default int gridDistance(final int x, final int y) {
        return Math.abs(x() - x) + Math.abs(y() - y);
    }

    default int gridDistance(final GridPoint2D other) {
        return gridDistance(other.x(), other.y());
    }

    default int gridMagnitude() {
        return gridDistance(ZERO);
    }

    default double magnitude() {
        return distance(ZERO);
    }

    default GridPoint2D midpoint(final GridPoint2D gridPoint2D) {
        return midpoint(gridPoint2D.x(), gridPoint2D.y());
    }

    default GridPoint2D midpoint(final int x, final int y) {
        return new StandardGridPoint2D(
                x + (x() - x) / 2,
                y + (y() - y) / 2);
    }

    default double angle(final double x, final double y) {
        final var ax = x();
        final var ay = y();

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
        return x() * x + y() * y;
    }

    default int dotProduct(final GridPoint2D point2D) {
        return dotProduct(point2D.x(), point2D.y());
    }

    default GridPoint3D crossProduct(final int x, final int y) {
        final var ax = x();
        final var ay = y();
        return new StandardPoint3D(0, 0, ax * y - ay * x);
    }

    default GridPoint3D crossProduct(final GridPoint2D point2D) {
        return crossProduct(point2D.x(), point2D.y());
    }
}
