package org.hzt.graph;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NodeTest {

    @Test
    void testBreadthFirstSequence() {
        final var graph = buildTrainNet();

        final var stationNames = graph.get("Leiden").breadthFirstSequence()
                .map(railWayStation -> railWayStation.name)
                .toList();

        assertEquals(List.of("Leiden", "Schiphol", "Voorschoten", "Alphen aan de Rijn", "Amsterdam", "Den Haag", "Bodegraven", "Utrecht", "Bergen op zoom", "Rotterdam", "Tilburg", "Middelburg", "Eindhoven", "Vlissingen"), stationNames);
    }

    @Test
    void testFindRouteWithLeastStops() {
        final var graph = buildTrainNet();

        final var leiden = graph.get("Leiden");
        final var vlissingen = graph.get("Vlissingen");

        final var leastStopsPath = leiden.breadthFirstSequence(Node.Mode.SET_PREDECESSORS)
                .first(vlissingen::equals)
                .predecessorSequence()
                .onEach(n -> System.out.println(n.predecessorSequence().count()))
                .map(station -> station.name)
                .toList();

        assertEquals(List.of("Vlissingen", "Middelburg", "Bergen op zoom", "Den Haag", "Voorschoten", "Leiden"), leastStopsPath);
    }

    @Test
    void testFindTouristicRoute() {
        final var trainNet = buildTrainNet();

        final var leiden = trainNet.get("Leiden");

        final List<String> visitedStation = new ArrayList<>();

        final var vlissingen = leiden.depthFirstSequence(Node.Mode.SET_PREDECESSORS)
                .onEach(It::println)
                .onEach(s -> visitedStation.add(0, s.name))
                .first(trainNet.get("Vlissingen")::equals);

        final var leastStopsPath = vlissingen.predecessorSequence().toListOf(station -> station.name);
        final var expected = List.of("Vlissingen", "Middelburg", "Bergen op zoom", "Rotterdam", "Utrecht", "Amsterdam", "Schiphol", "Leiden");

        assertAll(
                () -> assertEquals(expected, leastStopsPath),
                () -> assertEquals(expected, visitedStation)
        );
    }

    @Test
    void testFindRouteWithLeastStopsNoRoute() {
        final var graph = buildTrainNet();

        final var source = graph.get("Leiden");

        final var railWayStations = source.breadthFirstSequence();
        final var timbuktu = graph.get("Timbuktu");

        assertThrows(NoSuchElementException.class, () -> railWayStations.first(timbuktu::equals));
    }

    @Test
    void testDepthFirstSequence() {
        final var graph = buildTrainNet();

        final var stationNames = graph.get("Leiden").depthFirstSequence()
                .map(railWayStation -> railWayStation.name)
                .toList();

        assertEquals(List.of("Leiden", "Schiphol", "Amsterdam", "Utrecht", "Rotterdam", "Bergen op zoom", "Middelburg", "Vlissingen", "Eindhoven", "Den Haag", "Voorschoten", "Bodegraven", "Alphen aan de Rijn", "Tilburg"), stationNames);
    }

    private MapX<String, RailWayStation> buildTrainNet() {
        final var map = ListX.of(
                        "Alphen aan de Rijn",
                        "Amsterdam",
                        "Bergen op zoom",
                        "Bodegraven",
                        "Den Haag",
                        "Eindhoven",
                        "Leiden",
                        "Middelburg",
                        "Rotterdam",
                        "Schiphol",
                        "Tilburg",
                        "Timbuktu",
                        "Utrecht",
                        "Vlissingen",
                        "Voorschoten")
                .associateWith(RailWayStation::new);

        map.get("Amsterdam").bidiAddNeighbor(map.get("Schiphol"));
        map.get("Bergen op zoom").bidiAddNeighbors(List.of(
                map.get("Middelburg").bidiAddNeighbor(map.get("Vlissingen")),
                map.get("Eindhoven"),
                map.get("Rotterdam"),
                map.get("Den Haag")));
        map.get("Leiden").bidiAddNeighbors(Sequence.of(
                "Schiphol",
                "Voorschoten",
                "Alphen aan de Rijn").toListOf(map::get));
        map.get("Rotterdam").bidiAddNeighbors(ListX.of("Den Haag", "Bergen op zoom", "Utrecht").map(map::get));
        map.get("Utrecht").bidiAddNeighbors(ListX.of(
                "Bodegraven",
                "Tilburg",
                "Bergen op zoom",
                "Den Haag",
                "Amsterdam",
                "Rotterdam").map(map::get));
        map.get("Voorschoten").bidiAddNeighbor(map.get("Den Haag"));
        map.get("Bodegraven").bidiAddNeighbor(map.get("Alphen aan de Rijn"));
        return map;
    }


    static class RailWayStation implements Node<RailWayStation, RailWayStation> {


        private final String name;

        private final Set<RailWayStation> neighbors = new LinkedHashSet<>();
        private RailWayStation predecessor;

        public RailWayStation(final String name) {
            this.name = name;
        }

        @Override
        public RailWayStation withPredecessor(final RailWayStation predecessor) {
            this.predecessor = predecessor;
            return this;
        }

        @Override
        public Iterator<RailWayStation> neighborIterator() {
            return neighbors.iterator();
        }

        @Override
        public Set<RailWayStation> getMutableNeighbors() {
            return neighbors;
        }


        @Override
        public Optional<RailWayStation> optionalPredecessor() {
            return Optional.ofNullable(predecessor);
        }

        @Override
        public String toString() {
            return "RailWayStation{" +
                   "name='" + name + '\'' +
                   '}';
        }
    }
}
