package org.hzt.graph;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NodeTest {

    @Test
    void testBreadthFirstSequence() {
        final MapX<String, RailWayStation> graph = buildTrainNet();

        final List<String> stationNames = graph.get("Leiden").breadthFirstSequence()
                .onEach(It::println)
                .map(railWayStation -> railWayStation.name)
                .toList();

        assertEquals(Arrays.asList("Leiden", "Amsterdam", "Den Haag", "Utrecht", "Tilburg", "Bergen op zoom", "Rotterdam", "Middelburg", "Vlissingen"), stationNames);
    }

    @Test
    void testFindRouteWithLeastStops() {
        final MapX<String, RailWayStation> graph = buildTrainNet();

        final RailWayStation leiden = graph.get("Leiden");
        final RailWayStation vlissingen = graph.get("Vlissingen");

        final List<String> visitedStation = new ArrayList<>();

        final List<String> leastStopsPath = leiden.breadthFirstSequence(Node.Mode.SET_PREDECESSORS)
                .first(vlissingen::equals)
                .predecessorSequence()
                .onEach(n -> System.out.println(n.predecessorSequence().count()))
                .map(station -> station.name)
                .onEach(visitedStation::add)
                .toList();

        assertAll(
                () -> assertIterableEquals(Arrays.asList("Vlissingen", "Middelburg", "Bergen op zoom", "Utrecht", "Leiden"), leastStopsPath),
                () -> assertIterableEquals(leastStopsPath, visitedStation)
        );
    }

    @Test
    void testFindTouristicRoute() {
        final MapX<String, RailWayStation> trainNet = buildTrainNet();

        final RailWayStation leiden = trainNet.get("Leiden");

        final List<String> visitedStation = new ArrayList<>();

        final RailWayStation vlissingen = leiden.depthFirstSequence(Node.Mode.SET_PREDECESSORS)
                .onEach(It::println)
                .onEach(s -> visitedStation.add(0, s.name))
                .first(trainNet.get("Vlissingen")::equals);

        final List<String> leastStopsPath = vlissingen.predecessorSequence().toListOf(station -> station.name);
        final List<String> expected = Arrays.asList("Vlissingen", "Middelburg", "Bergen op zoom", "Tilburg", "Utrecht", "Amsterdam", "Leiden");

        assertAll(
                () -> assertEquals(expected, leastStopsPath),
                () -> assertEquals(expected, visitedStation)
        );
    }

    @Test
    void testFindRouteWithLeastStopsNoRoute() {
        final MapX<String, RailWayStation> graph = buildTrainNet();

        final RailWayStation source = graph.get("Leiden");

        final Sequence<RailWayStation> railWayStations = source.breadthFirstSequence();
        final RailWayStation timbuktu = graph.get("Timbuktu");

        assertThrows(NoSuchElementException.class, () -> railWayStations.first(timbuktu::equals));
    }

    @Test
    void testDepthFirstSequence() {
        final MapX<String, RailWayStation> graph = buildTrainNet();

        final List<String> stationNames = graph.get("Leiden").depthFirstSequence()
                .map(railWayStation -> railWayStation.name)
                .toList();

        assertEquals(Arrays.asList("Leiden", "Amsterdam", "Utrecht", "Tilburg", "Bergen op zoom", "Middelburg", "Vlissingen", "Rotterdam", "Den Haag"), stationNames);
    }

    private MapX<String, RailWayStation> buildTrainNet() {
        final MapX<String, RailWayStation> map = ListX.of(
                        "Leiden",
                        "Amsterdam",
                        "Den Haag",
                        "Rotterdam",
                        "Utrecht",
                        "Middelburg",
                        "Vlissingen",
                        "Tilburg",
                        "Bergen op zoom",
                        "Timbuktu")
                .associateWith(RailWayStation::new);

        map.get("Leiden").bidiAddNeighbors(Sequence.of(
                "Amsterdam",
                "Den Haag",
                "Utrecht").toListOf(map::get));
        map.get("Utrecht").bidiAddNeighbors(ListX.of(
                "Tilburg",
                "Bergen op zoom",
                "Den Haag",
                "Amsterdam",
                "Rotterdam").map(map::get));
        map.get("Bergen op zoom").bidiAddNeighbors(Arrays.asList(
                map.get("Middelburg").bidiAddNeighbor(map.get("Vlissingen")),
                map.get("Tilburg"),
                map.get("Rotterdam")));
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
