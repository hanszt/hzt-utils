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

    public static <T> List<List<List<T>>> toListGrid(T[][][] grid) {
        List<List<List<T>>> newGrid = new ArrayList<>();
        for (T[][] plane : grid) {
            List<List<T>> newPlane = new ArrayList<>();
            for (T[] row : plane) {
                List<T> newRow = new ArrayList<>();
                Collections.addAll(newRow, row);
                newPlane.add(newRow);
            }
            newGrid.add(newPlane);
        }
        return newGrid;
    }

    public static <T> List<List<List<T>>> toListGrid(int[][][] grid, IntFunction<T> mapper) {
        List<List<List<T>>> newGrid = new ArrayList<>();
        for (int[][] plane : grid) {
            List<List<T>> newPlane = new ArrayList<>();
            for (int[] row : plane) {
                List<T> newRow = new ArrayList<>();
                for (int value : row) {
                    newRow.add(mapper.apply(value));
                }
                newPlane.add(newRow);
            }
            newGrid.add(newPlane);
        }
        return newGrid;
    }

    public static <T> boolean anyInGrid(T[][][] grid, Predicate<T> predicate) {
        for (T[][] plane : grid) {
            for (T[] row : plane) {
                for (T item : row) {
                    if (predicate.test(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static <T> boolean noneInGrid(T[][][] grid, Predicate<T> predicate) {
        for (T[][] plane : grid) {
            for (T[] row : plane) {
                for (T item : row) {
                    if (predicate.test(item)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static <T> boolean allInGrid(T[][][] grid, Predicate<T> predicate) {
        for (T[][] plane : grid) {
            for (T[] row : plane) {
                for (T item : row) {
                    if (!predicate.test(item)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
