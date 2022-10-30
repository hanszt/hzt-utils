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
        final WeightedNode<Object> start = WeightedNode.ofCost(3);
        final WeightedNode<Object> graph = WeightedNode.ofCost(2);
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
        WeightedNode<String> AB = WeightedNode.of("AB", 3);
        WeightedNode<String> BA = WeightedNode.of("BA", 3);
        WeightedNode<String> AC = WeightedNode.of("AC", 2);
        WeightedNode<String> CA = WeightedNode.of("CA", 2);
        WeightedNode<String> AD = WeightedNode.of("AD", 6);
        WeightedNode<String> DA = WeightedNode.of("DA", 6);
        WeightedNode<String> BD = WeightedNode.of("BD", 5);
        WeightedNode<String> DB = WeightedNode.of("DB", 5);
        WeightedNode<String> CD = WeightedNode.of("CD", 1);
        WeightedNode<String> DC = WeightedNode.of("DC", 1);
        WeightedNode<String> DE = WeightedNode.of("DE", 7);
        WeightedNode<String> BE = WeightedNode.of("BE", 4);
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
