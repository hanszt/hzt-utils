package org.hzt.graph;

import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NodeTest {

    @Test
    void testBreadthFirstSequence() {
        final Map<String, RailWayStation> graph = buildGraph();

        final List<String> stationNames = graph.get("Leiden").breadthFirstSequence()
                .onEach(It::println)
                .map(railWayStation -> railWayStation.name)
                .toList();

        assertEquals(Arrays.asList("Leiden", "Amsterdam", "Den Haag", "Utrecht", "Tilburg", "Bergen op zoom", "Rotterdam", "Middelburg", "Vlissingen"), stationNames);
    }

    @Test
    void testFindRouteWithLeastStops() {
        final Map<String, RailWayStation> graph = buildGraph();

        final RailWayStation source = graph.get("Leiden");
        final RailWayStation target = graph.get("Vlissingen");

        final List<String> leastStopsPath = source.breadthFirstSequence()
                .first(target::equals)
                .predecessorSequence()
                .onEach(n -> System.out.println(n.predecessorSequence().count()))
                .toListOf(station -> station.name);

        assertEquals(Arrays.asList("Vlissingen", "Middelburg", "Bergen op zoom", "Utrecht", "Leiden"), leastStopsPath);
    }

    @Test
    void testFindTouristicRoute() {
        final Map<String, RailWayStation> graph = buildGraph();

        final RailWayStation source = graph.get("Leiden");

        final RailWayStation vlissingen = source.depthFirstSequence().onEach(System.out::println).first(graph.get("Vlissingen")::equals);

        final List<String> leastStopsPath = vlissingen.predecessorSequence().take(100).toListOf(station -> station.name);

        assertEquals(Collections.singletonList("Vlissingen"), leastStopsPath);
    }

    @Test
    void testFindRouteWithLeastStopsNoRoute() {
        final Map<String, RailWayStation> graph = buildGraph();

        final RailWayStation source = graph.get("Leiden");

        final Sequence<RailWayStation> railWayStations = source.breadthFirstSequence();
        final RailWayStation timbuktu = graph.get("Timbuktu");

        assertThrows(NoSuchElementException.class, () -> railWayStations.first(timbuktu::equals));
    }

    @Test
    void testDepthFirstSequence() {
        final Map<String, RailWayStation> graph = buildGraph();

        final List<String> stationNames = graph.get("Leiden").depthFirstSequence()
                .map(railWayStation -> railWayStation.name)
                .toList();

        assertEquals(Arrays.asList("Leiden", "Amsterdam", "Utrecht", "Tilburg", "Bergen op zoom", "Middelburg", "Vlissingen", "Rotterdam", "Den Haag"), stationNames);
    }

    private Map<String, RailWayStation> buildGraph() {
        final Map<String, RailWayStation> map = Sequence.of(
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
                .associateWith(RailWayStation::new)
                .toMap();

        map.get("Leiden").bidiAddNeighbors(Sequence.of(
                "Amsterdam",
                "Den Haag",
                "Utrecht").toListOf(map::get));
        map.get("Utrecht").bidiAddNeighbors(Sequence.of(
                "Tilburg",
                "Bergen op zoom",
                "Den Haag",
                "Amsterdam",
                "Rotterdam").toListOf(map::get));
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
        public Set<RailWayStation> getNeighbors() {
            return neighbors;
        }

        @Override
        public RailWayStation getPredecessor() {
            return predecessor;
        }

        @Override
        public String toString() {
            return "RailWayStation{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
