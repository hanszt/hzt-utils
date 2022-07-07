package org.hzt.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
public final class Grid2DUtils {

    private Grid2DUtils() {
    }

    public static <T> List<List<T>> toListGrid(T[][] grid) {
        List<List<T>> listGrid = new ArrayList<>();
        for (T[] row : grid) {
            List<T> newRow = new ArrayList<>();
            Collections.addAll(newRow, row);
            listGrid.add(newRow);
        }
        return listGrid;
    }

    public static <R> List<List<R>> toListGrid(int[][] grid, IntFunction<R> mapper) {
        List<List<R>> listGrid = new ArrayList<>();
        for (int[] row : grid) {
            List<R> newRow = new ArrayList<>();
            for (int value : row) {
                newRow.add(mapper.apply(value));
            }
            listGrid.add(newRow);
        }
        return listGrid;
    }

    public static List<List<Integer>> toListGrid(int[][] grid) {
        return toListGrid(grid, i -> i);
    }

    public static <R> List<List<R>> toListGrid(boolean[][] grid, Function<Boolean, ? extends R> mapper) {
        final List<List<R>> resultGrid = new ArrayList<>();
        for (boolean[] row : grid) {
            List<R> newRow = new ArrayList<>();
            for (boolean bool : row) {
                newRow.add(mapper.apply(bool));
            }
            resultGrid.add(newRow);
        }
        return resultGrid;
    }

    public static <T> int[][] toIntGrid(T[][] grid, ToIntFunction<? super T> mapper) {
        final int[][] resultGrid = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            T[] row = grid[i];
            int[] newRow = new int[row.length];
            for (int j = 0; j < row.length; j++) {
                newRow[j] = mapper.applyAsInt(row[j]);
            }
            resultGrid[i] = newRow;
        }
        return resultGrid;
    }

    public static <T> int[][] toIntGrid(Iterable<? extends Iterable<T>> grid, ToIntFunction<? super T> mapper) {
        final Function<Iterable<T>, int[]> toIntArray = row -> StreamSupport.stream(row.spliterator(), false)
                .mapToInt(mapper)
                .toArray();

        return StreamSupport.stream(grid.spliterator(), false)
                .map(toIntArray)
                .toArray(int[][]::new);
    }

    public static <T> boolean anyInGrid(Iterable<? extends Iterable<T>> grid, Predicate<T> predicate) {
        for (Iterable<T> row : grid) {
            for (T item : row) {
                if (predicate.test(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> boolean anyInGrid(T[][] grid, Predicate<T> predicate) {
        for (T[] row : grid) {
            for (T item : row) {
                if (predicate.test(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> boolean allInGrid(Iterable<? extends Iterable<T>> grid, Predicate<T> predicate) {
        for (Iterable<T> row : grid) {
            for (T item : row) {
                if (!predicate.test(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T> boolean allInGrid(T[][] grid, Predicate<T> predicate) {
        for (T[] row : grid) {
            for (T item : row) {
                if (!predicate.test(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T> boolean noneInGrid(Iterable<? extends Iterable<T>> grid, Predicate<T> predicate) {
        for (Iterable<T> row : grid) {
            for (T item : row) {
                if (predicate.test(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T> boolean noneInGrid(T[][] grid, Predicate<T> predicate) {
        for (T[] row : grid) {
            for (T item : row) {
                if (predicate.test(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T, R> List<List<R>> mapGrid(Iterable<? extends Iterable<T>> grid, Function<? super T, ? extends R> mapper) {
        final Function<Iterable<T>, List<R>> rowToMapper = row -> StreamSupport.stream(row.spliterator(), false)
                .map(mapper)
                .collect(Collectors.toList());

        return StreamSupport.stream(grid.spliterator(), false)
                .map(rowToMapper)
                .collect(Collectors.toList());
    }

    public static <T> String gridAsString(T[][] grid) {
        final Function<T[], String> rowToString = row -> Arrays.stream(row)
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        return Arrays.stream(grid)
                .map(rowToString)
                .collect(Collectors.joining("\n"));
    }

    public static <T> String gridAsString(Iterable<? extends Iterable<T>> grid) {
        final Function<Iterable<T>, String> rowToString = row -> StreamSupport.stream(row.spliterator(), false)
                .map(Objects::toString)
                .collect(Collectors.joining());

        return StreamSupport.stream(grid.spliterator(), false)
                .map(rowToString)
                .collect(Collectors.joining("\n"));
    }

    public static <T> List<List<T>> swap(List<List<T>> grid, int rowi1, int coli1, int rowi2, int coli2) {
        T item1 = grid.get(rowi1).get(coli1);
        T item2 = grid.get(rowi2).get(coli2);
        grid.get(rowi1).set(coli1, item2);
        grid.get(rowi2).set(coli2, item1);
        return grid;
    }

    public static <T> List<List<T>> swap(List<List<T>> grid, GridPoint2D point1, GridPoint2D point2) {
        return swap(grid, point1.getY(), point2.getX(), point2.getY(), point2.getX());
    }
}

