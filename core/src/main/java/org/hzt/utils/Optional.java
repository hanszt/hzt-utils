package org.hzt.utils;

import java.util.NoSuchElementException;

public final class Optional<T> {

    public static <T> Optional<T> empty() {
        return new Optional<T>(null);
    }

    public static <T> Optional<T> of(T value) {
        Objects.requireNonNull(value);
        return new Optional<T>(value);
    }

    public static <T> Optional<T> ofNullable(T value) {
        return new Optional<T>(value);
    }

    private final T value;

    private Optional(final T value) {
        this.value = value;
    }

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public boolean isPresent() {
        return value != null;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public T orElseThrow() {
        if (value != null) {
            return value;
        }
        throw new NoSuchElementException();
    }

    public T orElse(T defaultValue) {
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    /**
     * Indicates whether some other object is "equal to" this Optional. The
     * other object is considered equal if:
     * <ul>
     * <li>it is also an {@code Optional} and;
     * <li>both instances have no value present or;
     * <li>the present values are "equal to" each other via {@code equals()}.
     * </ul>
     *
     * @param obj an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object
     * otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Optional)) {
            return false;
        }

        Optional<?> other = (Optional<?>) obj;
        if (value == null) {
            return other.value == null;
        }
        return value.equals(other.value);
    }

    /**
     * Returns the hash code value of the present value, if any, or 0 (zero) if
     * no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    /**
     * Returns a non-empty string representation of this Optional suitable for
     * debugging. The exact presentation format is unspecified and may vary
     * between implementations and versions.
     * <p>
     * If a value is present the result must include its string
     * representation in the result. Empty and present Optionals must be
     * unambiguously differentiable.
     *
     * @return the string representation of this instance
     */
    @Override
    public String toString() {
        return value != null
                ? String.format("Optional[%s]", value)
                : "Optional.empty";
    }
}
