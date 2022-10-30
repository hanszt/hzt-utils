package org.hzt.graph;

import org.hzt.utils.collections.MutableMapX;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class GraphAlgorithmsTest {
    // TODO: 8-6-2022 implement

    @Test
    @DisplayName("Test find shortest path using Dijkstra")
    void testFindShortestPathUsingDijkstra() {
        final var start = WeightedNode.ofCost(3);
        final var graph = WeightedNode.ofCost(2);
        assertThrows(IllegalStateException.class, () -> GraphAlgorithms.dijkstra(start, graph));
    }

    /**
     *    Find the shortest path from A to E
     *    A---3---B---4---E
     *    | \     |     /
     *    2   6   5   7
     *    |     \ | /
     *    C---1---D
     */
    @Disabled("Needs to be fixed")
    @Test
    void testFindShortestPath() {
        MutableMapX<String, WeightedNode<String>> graph = MutableMapX.empty();
        var AB = WeightedNode.of("AB", 3);
        var BA = WeightedNode.of("BA", 3);
        var AC = WeightedNode.of("AC", 2);
        var CA = WeightedNode.of("CA", 2);
        var AD = WeightedNode.of("AD", 6);
        var DA = WeightedNode.of("DA", 6);
        var BD = WeightedNode.of("BD", 5);
        var DB = WeightedNode.of("DB", 5);
        var CD = WeightedNode.of("CD", 1);
        var DC = WeightedNode.of("DC", 1);
        var DE = WeightedNode.of("DE", 7);
        var BE = WeightedNode.of("BE", 4);
        fail("Not implemented");
    }

    @Test
    @Disabled("Needs to be fixed")
    void testBreadthFirstSearch() {
        GraphAlgorithms.breadthFirstSearch(null, null);
        fail("Not implemented");
    }

    @Test
    @Disabled("Needs to be fixed")
    void testDepthFirstSearch() {
        GraphAlgorithms.depthFirstSearch(null, null);
        fail("Not implemented");
    }

}
