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

    public static <T> List<List<T>> toListGrid(final T[][] grid) {
        final List<List<T>> listGrid = new ArrayList<>();
        for (final T[] row : grid) {
            final List<T> newRow = new ArrayList<>();
            Collections.addAll(newRow, row);
            listGrid.add(newRow);
        }
        return listGrid;
    }

    public static <R> List<List<R>> toListGrid(final int[][] grid, final IntFunction<R> mapper) {
        final List<List<R>> listGrid = new ArrayList<>();
        for (final int[] row : grid) {
            final List<R> newRow = new ArrayList<>();
            for (final int value : row) {
                newRow.add(mapper.apply(value));
            }
            listGrid.add(newRow);
        }
        return listGrid;
    }

    public static List<List<Integer>> toListGrid(final int[][] grid) {
        return toListGrid(grid, i -> i);
    }

    public static <R> List<List<R>> toListGrid(final boolean[][] grid, final Function<Boolean, ? extends R> mapper) {
        final List<List<R>> resultGrid = new ArrayList<>();
        for (final boolean[] row : grid) {
            final List<R> newRow = new ArrayList<>();
            for (final boolean bool : row) {
                newRow.add(mapper.apply(bool));
            }
            resultGrid.add(newRow);
        }
        return resultGrid;
    }

    public static <T> int[][] toIntGrid(final T[][] grid, final ToIntFunction<? super T> mapper) {
        final int[][] resultGrid = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            final T[] row = grid[i];
            final int[] newRow = new int[row.length];
            for (int j = 0; j < row.length; j++) {
                newRow[j] = mapper.applyAsInt(row[j]);
            }
            resultGrid[i] = newRow;
        }
        return resultGrid;
    }

    public static <T> int[][] toIntGrid(final Iterable<? extends Iterable<T>> grid, final ToIntFunction<? super T> mapper) {
        final Function<Iterable<T>, int[]> toIntArray = row -> StreamSupport.stream(row.spliterator(), false)
                .mapToInt(mapper)
                .toArray();

        return StreamSupport.stream(grid.spliterator(), false)
                .map(toIntArray)
                .toArray(int[][]::new);
    }

    public static <T> boolean anyInGrid(final Iterable<? extends Iterable<T>> grid, final Predicate<T> predicate) {
        for (final Iterable<T> row : grid) {
            for (final T item : row) {
                if (predicate.test(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> boolean anyInGrid(final T[][] grid, final Predicate<T> predicate) {
        for (final T[] row : grid) {
            for (final T item : row) {
                if (predicate.test(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> boolean allInGrid(final Iterable<? extends Iterable<T>> grid, final Predicate<T> predicate) {
        for (final Iterable<T> row : grid) {
            for (final T item : row) {
                if (!predicate.test(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T> boolean allInGrid(final T[][] grid, final Predicate<T> predicate) {
        for (final T[] row : grid) {
            for (final T item : row) {
                if (!predicate.test(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T> boolean noneInGrid(final Iterable<? extends Iterable<T>> grid, final Predicate<T> predicate) {
        for (final Iterable<T> row : grid) {
            for (final T item : row) {
                if (predicate.test(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T> boolean noneInGrid(final T[][] grid, final Predicate<T> predicate) {
        for (final T[] row : grid) {
            for (final T item : row) {
                if (predicate.test(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T, R> List<List<R>> mapGrid(final Iterable<? extends Iterable<T>> grid, final Function<? super T, ? extends R> mapper) {
        final Function<Iterable<T>, List<R>> rowToMapper = row -> StreamSupport.stream(row.spliterator(), false)
                .map(mapper)
                .collect(Collectors.toList());

        return StreamSupport.stream(grid.spliterator(), false)
                .map(rowToMapper)
                .collect(Collectors.toList());
    }

    public static <T> String gridAsString(final T[][] grid) {
        final Function<T[], String> rowToString = row -> Arrays.stream(row)
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        return Arrays.stream(grid)
                .map(rowToString)
                .collect(Collectors.joining("\n"));
    }

    public static <T> String gridAsString(final Iterable<? extends Iterable<T>> grid) {
        final Function<Iterable<T>, String> rowToString = row -> StreamSupport.stream(row.spliterator(), false)
                .map(Objects::toString)
                .collect(Collectors.joining());

        return StreamSupport.stream(grid.spliterator(), false)
                .map(rowToString)
                .collect(Collectors.joining("\n"));
    }

    public static <T> List<List<T>> swap(final List<List<T>> grid, final int rowi1, final int coli1, final int rowi2, final int coli2) {
        final T item1 = grid.get(rowi1).get(coli1);
        final T item2 = grid.get(rowi2).get(coli2);
        grid.get(rowi1).set(coli1, item2);
        grid.get(rowi2).set(coli2, item1);
        return grid;
    }

    public static <T> List<List<T>> swap(final List<List<T>> grid, final GridPoint2D point1, final GridPoint2D point2) {
        return swap(grid, point1.getY(), point2.getX(), point2.getY(), point2.getX());
    }
}

