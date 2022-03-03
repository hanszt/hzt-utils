package org.hzt.utils.test.model;

import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.Transformable;
import org.hzt.test.model.Painting;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class PaintingAuction implements
        Comparable<PaintingAuction>, Sequence<Painting>, Transformable<PaintingAuction> {

    private final String name;
    private final LocalDate dateOfOpening;
    private final List<Painting> paintingList = new ArrayList<>();

    private Painting mostPopularPainting;

    public PaintingAuction(String name, LocalDate dateOfOpening, List<Painting> paintings) {
        Objects.requireNonNull(paintings);
        this.name = name;
        this.dateOfOpening = dateOfOpening;
        this.paintingList.addAll(paintings);
        this.mostPopularPainting = filter(Objects::nonNull).first();
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfOpening() {
        return dateOfOpening;
    }

    public Painting getOldestPainting() {
        return paintingList.stream()
                .min(Comparator.comparing(Painting::getYearOfCreation))
                .orElseThrow();
    }

    public Painting getMostPopularPainting() {
        return mostPopularPainting;
    }

    public void setMostPopularPainting(Painting mostPopularPainting) {
        this.mostPopularPainting = mostPopularPainting;
    }

    public List<Painting> getPaintings() {
        return Collections.unmodifiableList(paintingList);
    }

    public void toDatesOfBirthPainters(Consumer<LocalDate> dateOfBirthConsumer) {
        toPainterDateOfBirthStream()
                .forEach(dateOfBirthConsumer);
    }

    public Stream<LocalDate> toPainterDateOfBirthStream() {
        return stream().map(p -> p.painter().getDateOfBirth()).filter(Objects::nonNull);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof PaintingAuction auction &&
                Objects.equals(name, auction.name) &&
                Objects.equals(dateOfOpening, auction.dateOfOpening) &&
                Objects.equals(mostPopularPainting, auction.mostPopularPainting));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateOfOpening, mostPopularPainting);
    }

    @Override
    public int compareTo(PaintingAuction o) {
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

    @Override
    public @NotNull PaintingAuction get() {
        return this;
    }
}
