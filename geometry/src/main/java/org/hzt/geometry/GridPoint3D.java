package org.hzt.geometry;

public interface GridPoint3D {

    GridPoint3D ZERO = new GridPoint3DImpl(0, 0, 0);

    static GridPoint3D from(final int x, final int y, final int z) {
        return new GridPoint3DImpl(x, y, z);
    }
    int getX();

    int getY();
    
    int getZ();

    default GridPoint3D multiply(final GridPoint3D other) {
        return new GridPoint3DImpl(getX() * other.getX(), getY() * other.getY(), getZ() * other.getZ());
    }

    default GridPoint3D multiply(final int scalar) {
        return new GridPoint3DImpl(getX() * scalar, getY() * scalar, getZ() * scalar);
    }

    default GridPoint3D add(final GridPoint3D other) {
        return new GridPoint3DImpl(getX() + other.getX(), getY() + other.getY(), getZ() + other.getZ());
    }

    default GridPoint3D subtract(final GridPoint3D other) {
        return new GridPoint3DImpl(getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ());
    }

    default int gridDistance(final int x, final int y, final int z) {
        return Math.abs(getX() - x) + Math.abs(getY() - y) + Math.abs(getZ() - z);
    }

    default int gridDistance(final GridPoint3D other) {
        return gridDistance(other.getX(), other.getY(), other.getZ());
    }

    default int distanceSquared(final int x, final int y, final int z) {
        return x * x + y * y + z * z;
    }

    default int distanceSquared(final GridPoint3D gridPoint3D) {
        return distanceSquared(gridPoint3D.getX(), gridPoint3D.getY(), gridPoint3D.getZ());
    }

    default double distance(final int x, final int y, final int z) {
        return Math.sqrt(distanceSquared(x, y, z));
    }

    default double distance(final GridPoint3D other) {
        return distance(other.getX(), other.getY(), other.getZ());
    }

    default int gridMagnitude() {
        return gridDistance(ZERO);
    }

    default double magnitude() {
        return distance(ZERO);
    }

    default GridPoint3D midpoint(final GridPoint3D gridPoint2D) {
        return midpoint(gridPoint2D.getX(), gridPoint2D.getY(), gridPoint2D.getZ());
    }

    default GridPoint3D midpoint(final int x, final int y, final int z) {
        return new GridPoint3DImpl(
                x + (getX() - x) / 2,
                y + (getY() - y) / 2,
                z + (getZ() - z) / 2);
    }

    default int dotProduct(final int x, final int y, final int z) {
        return getX() * x + getY() * y + getZ() * z;
    }

    default int dotProduct(final GridPoint3D point3D) {
        return dotProduct(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    default GridPoint3D crossProduct(final int x, final int y, final int z) {
        final var ax = getX();
        final var ay = getY();
        final var az = getZ();

        return new GridPoint3DImpl(
                ay * z - az * y,
                az * x - ax * z,
                ax * y - ay * x);
    }

    default GridPoint3D crossProduct(final GridPoint3D point3D) {
       return crossProduct(point3D.getX(), point3D.getY(), point3D.getZ());
    }
}
