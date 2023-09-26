package org.hzt.utils.function.predicates;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.function.Predicate;

public final class DateTimePredicates {

    private DateTimePredicates() {
    }

    public static Predicate<Instant> isBefore(final Instant other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<Instant> isAfter(final Instant other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<ZonedDateTime> isBefore(final ZonedDateTime other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<ZonedDateTime> isAfter(final ZonedDateTime other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<LocalTime> isBefore(final LocalTime other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<LocalTime> isAfter(final LocalTime other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<LocalDate> isBefore(final LocalDate other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<LocalDate> isAfter(final LocalDate other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<LocalDateTime> isBefore(final LocalDateTime other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<LocalDateTime> isAfter(final LocalDateTime other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<Year> isBefore(final Year other) {
        return year -> year != null && year.isBefore(other);
    }

    public static Predicate<Year> isAfter(final Year other) {
        return year -> year != null && year.isAfter(other);
    }

    public static Predicate<Year> isBefore(final int year) {
        return isBefore(Year.of(year));
    }

    public static Predicate<Year> isAfter(final int year) {
        return isAfter(Year.of(year));
    }

    public static Predicate<Year> isValidMonthDay(final MonthDay other) {
        return year -> year != null && year.isValidMonthDay(other);
    }

    public static Predicate<MonthDay> isValidYear(final int year) {
        return monthDay -> monthDay != null && monthDay.isValidYear(year);
    }

    public static Predicate<MonthDay> isBefore(final MonthDay other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<MonthDay> isAfter(final MonthDay other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<YearMonth> isValidDay(final int day) {
        return monthDay -> monthDay != null && monthDay.isValidDay(day);
    }

    public static Predicate<YearMonth> isBefore(final YearMonth other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<YearMonth> isAfter(final YearMonth other) {
        return value -> value != null && value.isAfter(other);
    }

}
