package org.hzt.test.model;

import java.time.Clock;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Period;
import java.time.Year;
import java.util.Objects;

public final class Painting
        implements Comparable<Painting> {
    private final String name;
    private final Painter painter;
    private final Year yearOfCreation;
    private final boolean isInMuseum;

    public Painting(final String name, final Painter painter, final Year yearOfCreation, final boolean isInMuseum) {
        this.name = name;
        this.painter = painter;
        this.yearOfCreation = yearOfCreation;
        this.isInMuseum = isInMuseum;
    }

    public static Painting of(final String name) {
        return new Painting(name, null, null, false);
    }

    public Period age(final LocalDate now) {
        return Period.between(yearOfCreation.atMonthDay(MonthDay.of(now.getMonth(), now.getDayOfMonth())), now);
    }

    public int ageInYears(final int currentYear) {
        return currentYear - yearOfCreation.getValue();
    }

    public Year getYearOfCreation() {
        return yearOfCreation;
    }

    public Period getMillenniumOfCreation() {
        if (yearOfCreation.isAfter(Year.of(1000)) && yearOfCreation.isBefore(Year.of(2000))) {
            return Period.between(LocalDate.of(1000, 1, 1), LocalDate.of(1999, 12, 31));
        } else if (yearOfCreation.isBefore(Year.of(1000))) {
            return Period.between(LocalDate.of(0, 1, 1), LocalDate.of(999, 12, 31));
        } else {
            return Period.between(LocalDate.of(2000, 1, 1), LocalDate.now(Clock.systemDefaultZone()));
        }
    }

    @Override
    public int compareTo(final Painting o) {
        return name.compareTo(o.name);
    }

    public String name() {
        return name;
    }

    public Painter painter() {
        return painter;
    }

    public Year yearOfCreation() {
        return yearOfCreation;
    }

    public boolean isInMuseum() {
        return isInMuseum;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        final Painting that = (Painting) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.painter, that.painter) &&
                Objects.equals(this.yearOfCreation, that.yearOfCreation) &&
                this.isInMuseum == that.isInMuseum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, painter, yearOfCreation, isInMuseum);
    }

    @Override
    public String toString() {
        return "Painting[" +
                "name=" + name + ", " +
                "painter=" + painter + ", " +
                "yearOfCreation=" + yearOfCreation + ", " +
                "isInMuseum=" + isInMuseum + ']';
    }

}
