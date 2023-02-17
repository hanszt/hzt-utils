package org.hzt.test.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Consumer;

public final class Painter extends Person implements Comparable<Painter>, Iterable<Painting> {

    private final String firstName;
    private final String lastname;
    private final LocalDate dateOfBirth;
    private final NavigableSet<Painting> paintingList = new TreeSet<>();
    private LocalDate dateOfDeath;

    public Painter(String firstName, String lastname, LocalDate dateOfBirth) {
        super(firstName + " " + lastname);
        this.firstName = firstName;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }

    public Period age(LocalDate now) {
        return Period.between(dateOfBirth, dateOfDeath != null ? dateOfDeath : now);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Optional<LocalDate> getDateOfDeath() {
        return Optional.ofNullable(dateOfDeath);
    }

    public void getDateOfDeathIfPresent(Consumer<LocalDate> consumer) {
        getDateOfDeath().ifPresent(consumer);
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public void addPaintings(Collection<Painting> collection) {
        paintingList.addAll(collection);
    }

    public void addPaintings(Painting... paintings) {
        paintingList.addAll(List.of(paintings));
    }

    @Override
    public int compareTo(Painter o) {
        return lastname.compareTo(o.lastname);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Painter &&
                Objects.equals(this.firstName, ((Painter) o).firstName) &&
                Objects.equals(this.lastname, ((Painter) o).lastname) &&
                Objects.equals(this.dateOfBirth, ((Painter) o).dateOfBirth));
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastname, dateOfBirth);
    }

    @Override
    public @NotNull Iterator<Painting> iterator() {
        return paintingList.iterator();
    }

    @Override
    public String toString() {
        return "Painter{" +
                "firstName='" + firstName + '\'' +
                ", lastname='" + lastname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
