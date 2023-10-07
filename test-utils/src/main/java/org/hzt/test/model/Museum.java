package org.hzt.test.model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class Museum implements Comparable<Museum>, Iterable<Painting> {

    private final String name;
    private final LocalDate dateOfOpening;
    private final List<Painting> paintingList = new ArrayList<>();

    private Painting mostPopularPainting;

    public Museum(final String name, final LocalDate dateOfOpening, final List<Painting> paintingList) {
        Objects.requireNonNull(paintingList);
        this.name = name;
        this.dateOfOpening = dateOfOpening;
        this.paintingList.addAll(paintingList);
        this.mostPopularPainting = paintingList.stream()
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow();
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfOpening() {
        return dateOfOpening;
    }

    public Optional<LocalDate> dateOfOpening() {
        return Optional.ofNullable(dateOfOpening);
    }

    public Painting getOldestPainting() {
        return paintingList.stream()
                .min(Comparator.comparing(Painting::getYearOfCreation))
                .orElseThrow();
    }

    public Painting getMostPopularPainting() {
        return mostPopularPainting;
    }

    public void setMostPopularPainting(final Painting mostPopularPainting) {
        this.mostPopularPainting = mostPopularPainting;
    }

    public List<Painting> getPaintings() {
        return Collections.unmodifiableList(paintingList);
    }

    public void toDatesOfBirthPainters(final Consumer<LocalDate> dateOfBirthConsumer) {
        toPainterDateOfBirthStream()
                .forEach(dateOfBirthConsumer);
    }

    public Stream<LocalDate> toPainterDateOfBirthStream() {
        return paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull);
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof final Museum museum &&
                Objects.equals(name, museum.name) &&
                Objects.equals(dateOfOpening, museum.dateOfOpening) &&
                Objects.equals(mostPopularPainting, museum.mostPopularPainting));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateOfOpening, mostPopularPainting);
    }

    @Override
    public int compareTo(@NotNull final Museum o) {
        if (name == null) {
            return -1;
        }
        if (o.getName() == null) {
            return 1;
        }
        return name.compareTo(o.getName());
    }

    @Override
    public @NotNull Iterator<Painting> iterator() {
        return paintingList.iterator();
    }

    @Override
    public String toString() {
        return "Museum{" +
                "name='" + name + '\'' +
                ", dateOfOpening=" + dateOfOpening +
                ", mostPopularPainting=" + mostPopularPainting +
                '}';
    }
}
