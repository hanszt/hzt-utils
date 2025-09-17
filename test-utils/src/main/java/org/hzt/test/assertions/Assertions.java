package org.hzt.test.assertions;

import org.assertj.core.api.AbstractComparableAssert;
import org.assertj.core.api.AbstractIteratorAssert;
import org.assertj.core.api.AbstractLocalDateAssert;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.AssertionFailedError;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class Assertions {

    private Assertions() {
    }

    public static LocalDateAssert assertThat(LocalDate actual) {
        return new LocalDateAssert(actual);
    }

    public static YearAssert assertThat(Year actual) {
        return new YearAssert(actual);
    }

    public static IntAssert assertThat(int actual) {
        return new IntAssert(actual);
    }

    public static <T> IterableAssert<T> assertThat(Iterable<? extends T> iterable) {
        return new IterableAssert<>(iterable);
    }

    public static <T> ListIteratorAssert<T> assertThat(ListIterator<? extends T> actual) {
        return new ListIteratorAssert<>(actual);
    }

    public static final class LocalDateAssert extends AbstractLocalDateAssert<LocalDateAssert> {
        /**
         * Creates a new <code>{@link AbstractLocalDateAssert}</code>.
         *
         * @param actual the actual value to verify
         */
        private LocalDateAssert(LocalDate actual) {
            super(actual, LocalDateAssert.class);
        }

        public LocalDateAssert isLeapYear() {
            assertTrue(actual.isLeapYear(), () -> "Expected " + actual + " to be a leap year but was not!");
            return this;
        }

        public LocalDateAssert isNotLeapYear() {
            assertFalse(actual.isLeapYear(), () -> "Expected " + actual + " not to be a leap year but was!");
            return this;
        }
    }

    public static final class YearAssert extends AbstractComparableAssert<YearAssert, Year> {
        /**
         * Creates a new <code>{@link AbstractComparableAssert}</code>.
         *
         * @param actual the actual value to verify
         */
        private YearAssert(Year actual) {
            super(actual, YearAssert.class);
        }

        public YearAssert isLeap() {
            assertTrue(actual.isLeap(), () -> "Expected " + actual + " to be a leap year but was not!");
            return this;
        }

        public YearAssert isNotLeap() {
            assertFalse(actual.isLeap(), () -> "Expected " + actual + " not to be a leap year but was!");
            return this;
        }
    }

    public static final class ListIteratorAssert<T> extends AbstractIteratorAssert<ListIteratorAssert<T>, T> {

        private final ListIterator<? extends T> actual;
        /**
         * Creates a new <code>{@link AbstractIteratorAssert}</code>.
         *
         * @param actual   the actual value to verify
         */
        private ListIteratorAssert(ListIterator<? extends T> actual) {
            super(actual, ListIteratorAssert.class);
            this.actual = actual;
        }

        public ListIteratorAssert<T> hasPrevious() {
            assertTrue(actual.hasPrevious(), () -> "Expected " + actual + " to have previous but did not");
            return this;
        }

        public ListIteratorAssert<T> doesNotHavePrevious() {
            assertFalse(actual.hasPrevious(), () -> "Expected " + actual + " to not have previous but did");
            return this;
        }
    }

    public static final class IterableAssert<T> extends ListAssert<T> {

        public IterableAssert(final Iterable<? extends T> actual) {
            this(StreamSupport.stream(Spliterators.spliteratorUnknownSize(actual.iterator(), 0), false));
        }

        public IterableAssert(final Stream<? extends T> actual) {
            super(actual);
        }

        public <S extends Comparable<? super S>> ListAssert<T> isSortedBy(Function<? super T, ? extends S> selector) {
            return isSortedAccordingTo(Comparator.comparing(selector));
        }
    }

    public static final class IntAssert {

        private final int actual;

        private IntAssert(int actual) {
            this.actual = actual;
        }

        public IntAssert is(IntPredicate predicate, Supplier<String> onErrorMessage) {
            assertTrue(predicate.test(actual), onErrorMessage);
            return this;
        }

        public IntAssert is(IntPredicate predicate) {
            assertTrue(predicate.test(actual), () -> "Expected " + actual + " to pass the given condition but did not");
            return this;
        }
    }

    static void assertSoftly(Executable assertions) throws Throwable {
        if (errorCollector().getErrorCollectionMode() == ErrorCollectionMode.SOFT) {
            final var oldError = errorCollector().getErrors();
            errorCollector().clear();
            errorCollector().incrementDepth();

            try {
                assertions.execute();
            } catch (Throwable e) {
                throw new AssertionFailedError("Failed with exception", e);
            } finally {
                final var aggregated = collectiveErrors(errorCollector());
                errorCollector().clear();
                final var errorCollector = errorCollector();
                oldError.forEach(errorCollector::addError);
                aggregated.ifPresent(errorCollector::addError);
                errorCollector().decrementDepth();
            }
        }

        errorCollector().setErrorCollectionMode(ErrorCollectionMode.SOFT);
        try {
            assertions.execute();
        } finally {
            errorCollector().setErrorCollectionMode(ErrorCollectionMode.HARD);
            collectiveErrors(errorCollector()).ifPresent(failedError -> {
                throw failedError;
            });
        }
    }

    private static ErrorCollector errorCollector() {
        return errorCollectorThreadLocal.get();
    }

    private static Optional<AssertionFailedError> collectiveErrors(ErrorCollector errorCollector) {
        final var failures = errorCollector.getErrors();
        errorCollector.clear();
        return failures.size() == 1 && failures.getFirst() instanceof AssertionFailedError e ? Optional.of(e) : toAssertionError(failures, errorCollector.getDepth());
    }

    private static Optional<AssertionFailedError> toAssertionError(List<Throwable> failures, int depth) {
        if (failures.isEmpty()) {
            return Optional.empty();
        } else {
            // TODO clean stacktraces?
            return Optional.of(new MultiAssertionError(failures, depth));
        }
    }

    public static final class MultiAssertionError extends AssertionFailedError {
        private final List<Throwable> errors;
        private final int depth;

        public MultiAssertionError(List<Throwable> errors, int depth) {
            this.errors = errors;
            this.depth = depth;
        }

        public List<Throwable> errors() {
            return errors;
        }

        public int depth() {
            return depth;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (MultiAssertionError) obj;
            return Objects.equals(this.errors, that.errors) &&
                    this.depth == that.depth;
        }

        @Override
        public int hashCode() {
            return Objects.hash(errors, depth);
        }

        @Override
        public String toString() {
            return "MultiAssertionError[" +
                    "errors=" + errors + ", " +
                    "depth=" + depth + ']';
        }


    }

    private static ThreadLocal<ErrorCollector> errorCollectorThreadLocal = ThreadLocal.withInitial(SimpleErrorCollector::new);

    public interface ErrorCollector {
        int getDepth();

        void incrementDepth();

        void decrementDepth();

        ErrorCollectionMode getErrorCollectionMode();

        void setErrorCollectionMode(ErrorCollectionMode errorCollectionMode);

        List<Throwable> getErrors();

        void addError(Throwable t);

        void clear();

    }

    static class SimpleErrorCollector implements ErrorCollector {

        private final List<Throwable> errors = new ArrayList<>();
        private ErrorCollectionMode errorCollectionMode = ErrorCollectionMode.HARD;
        private int depth = 0;

        @Override
        public int getDepth() {
            return depth;
        }

        @Override
        public void incrementDepth() {
            depth++;
        }

        @Override
        public void decrementDepth() {
            depth--;
        }

        @Override
        public ErrorCollectionMode getErrorCollectionMode() {
            return errorCollectionMode;
        }

        @Override
        public void setErrorCollectionMode(ErrorCollectionMode errorCollectionMode) {
            this.errorCollectionMode = errorCollectionMode;
        }

        @Override
        public List<Throwable> getErrors() {
            return List.copyOf(errors);
        }

        @Override
        public void addError(Throwable t) {
            errors.add(t);
        }

        @Override
        public void clear() {
            errors.clear();
        }
    }

    public enum ErrorCollectionMode {
        SOFT, HARD
    }
}
