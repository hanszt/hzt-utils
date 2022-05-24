package org.hzt.geometry;

public interface GridPoint3D {

    GridPoint3D ZERO = new GridPoint3DImpl(0, 0, 0);

    static GridPoint3D from(int x, int y, int z) {
        return new GridPoint3DImpl(x, y, z);
    }
    int getX();

    int getY();
    
    int getZ();

    default GridPoint3D multiply(GridPoint3D other) {
        return new GridPoint3DImpl(getX() * other.getX(), getY() * other.getY(), getZ() * other.getZ());
    }

    default GridPoint3D multiply(int scalar) {
        return new GridPoint3DImpl(getX() * scalar, getY() * scalar, getZ() * scalar);
    }

    default GridPoint3D add(GridPoint3D other) {
        return new GridPoint3DImpl(getX() + other.getX(), getY() + other.getY(), getZ() + other.getZ());
    }

    default GridPoint3D subtract(GridPoint3D other) {
        return new GridPoint3DImpl(getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ());
    }

    default int gridDistance(int x, int y, int z) {
        return Math.abs(getX() - x) + Math.abs(getY() - y) + Math.abs(getZ() - z);
    }

    default int gridDistance(GridPoint3D other) {
        return gridDistance(other.getX(), other.getY(), other.getZ());
    }

    default int gridMagnitude() {
        return gridDistance(0, 0, 0);
    }

    default GridPoint3D midpoint(GridPoint3D gridPoint2D) {
        return midpoint(gridPoint2D.getX(), gridPoint2D.getY(), gridPoint2D.getZ());
    }

    default GridPoint3D midpoint(int x, int y, int z) {
        return new GridPoint3DImpl(
                x + (getX() - x) / 2,
                y + (getY() - y) / 2,
                z + (getZ() - z) / 2);
    }

    default int dotProduct(int x, int y, int z) {
        return getX() * x + getY() * y + getZ() * z;
    }

    default GridPoint3D crossProduct(int x, int y, int z) {
        final int ax = getX();
        final int ay = getY();
        final int az = getZ();

        return new GridPoint3DImpl(
                ay * z - az * y,
                az * x - ax * z,
                ax * y - ay * x);
    }
}
