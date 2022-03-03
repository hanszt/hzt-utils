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

    public static Predicate<Instant> isBefore(Instant other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<Instant> isAfter(Instant other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<ZonedDateTime> isBefore(ZonedDateTime other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<ZonedDateTime> isAfter(ZonedDateTime other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<LocalTime> isBefore(LocalTime other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<LocalTime> isAfter(LocalTime other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<LocalDate> isBefore(LocalDate other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<LocalDate> isAfter(LocalDate other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<LocalDateTime> isBefore(LocalDateTime other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<LocalDateTime> isAfter(LocalDateTime other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<Year> isBefore(Year other) {
        return year -> year != null && year.isBefore(other);
    }

    public static Predicate<Year> isAfter(Year other) {
        return year -> year != null && year.isAfter(other);
    }

    public static Predicate<Year> isBefore(int year) {
        return isBefore(Year.of(year));
    }

    public static Predicate<Year> isAfter(int year) {
        return isAfter(Year.of(year));
    }

    public static Predicate<Year> isValidMonthDay(MonthDay other) {
        return year -> year != null && year.isValidMonthDay(other);
    }

    public static Predicate<MonthDay> isValidYear(int year) {
        return monthDay -> monthDay != null && monthDay.isValidYear(year);
    }

    public static Predicate<MonthDay> isBefore(MonthDay other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<MonthDay> isAfter(MonthDay other) {
        return value -> value != null && value.isAfter(other);
    }

    public static Predicate<YearMonth> isValidDay(int day) {
        return monthDay -> monthDay != null && monthDay.isValidDay(day);
    }

    public static Predicate<YearMonth> isBefore(YearMonth other) {
        return value -> value != null && value.isBefore(other);
    }

    public static Predicate<YearMonth> isAfter(YearMonth other) {
        return value -> value != null && value.isAfter(other);
    }

}
