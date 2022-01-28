package org.hzt.test.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Consumer;

public class Painter implements Comparable<Painter>, Iterable<Painting> {

    private final String firstName;
    private final String lastname;
    private final LocalDate dateOfBirth;
    private final NavigableSet<Painting> paintingList = new TreeSet<>();
    private LocalDate dateOfDeath;

    public Painter(String firstName, String lastname, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
    }

    public Period age() {
        return Period.between(dateOfBirth, dateOfDeath != null ? dateOfDeath : LocalDate.now());
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
        paintingList.addAll(Arrays.asList(paintings));
    }

    @Override
    public int compareTo(Painter o) {
        return lastname.compareTo(o.lastname);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Painter other &&
                Objects.equals(this.firstName, other.firstName) &&
                Objects.equals(this.lastname, other.lastname) &&
                Objects.equals(this.dateOfBirth, other.dateOfBirth));
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastname, dateOfBirth);
    }

    @Override
    public Iterator<Painting> iterator() {
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
