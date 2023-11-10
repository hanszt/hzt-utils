package org.hzt.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;

@SuppressWarnings({"unused", "squid:S134"})
public final class Grid3DUtils {

    private Grid3DUtils() {
    }

    public static <T> List<List<List<T>>> toListGrid(final T[][][] grid) {
        final List<List<List<T>>> newGrid = new ArrayList<>();
        for (final T[][] plane : grid) {
            final List<List<T>> newPlane = new ArrayList<>();
            for (final T[] row : plane) {
                final List<T> newRow = new ArrayList<>();
                Collections.addAll(newRow, row);
                newPlane.add(newRow);
            }
            newGrid.add(newPlane);
        }
        return newGrid;
    }

    public static <T> List<List<List<T>>> toListGrid(final int[][][] grid, final IntFunction<T> mapper) {
        final List<List<List<T>>> newGrid = new ArrayList<>();
        for (final int[][] plane : grid) {
            final List<List<T>> newPlane = new ArrayList<>();
            for (final int[] row : plane) {
                final List<T> newRow = new ArrayList<>();
                for (final int value : row) {
                    newRow.add(mapper.apply(value));
                }
                newPlane.add(newRow);
            }
            newGrid.add(newPlane);
        }
        return newGrid;
    }

    public static <T> boolean anyInGrid(final T[][][] grid, final Predicate<T> predicate) {
        for (final T[][] plane : grid) {
            for (final T[] row : plane) {
                for (final T item : row) {
                    if (predicate.test(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static <T> boolean noneInGrid(final T[][][] grid, final Predicate<T> predicate) {
        for (final T[][] plane : grid) {
            for (final T[] row : plane) {
                for (final T item : row) {
                    if (predicate.test(item)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static <T> boolean allInGrid(final T[][][] grid, final Predicate<T> predicate) {
        for (final T[][] plane : grid) {
            for (final T[] row : plane) {
                for (final T item : row) {
                    if (!predicate.test(item)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
