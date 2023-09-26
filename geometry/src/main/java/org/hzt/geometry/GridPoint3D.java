package org.hzt.geometry;

public interface GridPoint3D {

    GridPoint3D ZERO = new GridPoint3DImpl(0, 0, 0);

    static GridPoint3D from(final int x, final int y, final int z) {
        return new GridPoint3DImpl(x, y, z);
    }
    int x();

    int y();
    
    int z();

    default GridPoint3D multiply(final GridPoint3D other) {
        return new GridPoint3DImpl(x() * other.x(), y() * other.y(), z() * other.z());
    }

    default GridPoint3D multiply(final int scalar) {
        return new GridPoint3DImpl(x() * scalar, y() * scalar, z() * scalar);
    }

    default GridPoint3D add(final GridPoint3D other) {
        return new GridPoint3DImpl(x() + other.x(), y() + other.y(), z() + other.z());
    }

    default GridPoint3D subtract(final GridPoint3D other) {
        return new GridPoint3DImpl(x() - other.x(), y() - other.y(), z() - other.z());
    }

    default int gridDistance(final int x, final int y, final int z) {
        return Math.abs(x() - x) + Math.abs(y() - y) + Math.abs(z() - z);
    }

    default int gridDistance(final GridPoint3D other) {
        return gridDistance(other.x(), other.y(), other.z());
    }

    default int distanceSquared(final int x, final int y, final int z) {
        return x * x + y * y + z * z;
    }

    default int distanceSquared(final GridPoint3D gridPoint3D) {
        return distanceSquared(gridPoint3D.x(), gridPoint3D.y(), gridPoint3D.z());
    }

    default double distance(final int x, final int y, final int z) {
        return Math.sqrt(distanceSquared(x, y, z));
    }

    default double distance(final GridPoint3D other) {
        return distance(other.x(), other.y(), other.z());
    }

    default int gridMagnitude() {
        return gridDistance(ZERO);
    }

    default double magnitude() {
        return distance(ZERO);
    }

    default GridPoint3D midpoint(final GridPoint3D gridPoint2D) {
        return midpoint(gridPoint2D.x(), gridPoint2D.y(), gridPoint2D.z());
    }

    default GridPoint3D midpoint(final int x, final int y, final int z) {
        return new GridPoint3DImpl(
                x + (x() - x) / 2,
                y + (y() - y) / 2,
                z + (z() - z) / 2);
    }

    default int dotProduct(final int x, final int y, final int z) {
        return x() * x + y() * y + z() * z;
    }

    default int dotProduct(final GridPoint3D point3D) {
        return dotProduct(point3D.x(), point3D.y(), point3D.z());
    }

    default GridPoint3D crossProduct(final int x, final int y, final int z) {
        final var ax = x();
        final var ay = y();
        final var az = z();

        return new GridPoint3DImpl(
                ay * z - az * y,
                az * x - ax * z,
                ax * y - ay * x);
    }

    default GridPoint3D crossProduct(final GridPoint3D point3D) {
       return crossProduct(point3D.x(), point3D.y(), point3D.z());
    }
}
