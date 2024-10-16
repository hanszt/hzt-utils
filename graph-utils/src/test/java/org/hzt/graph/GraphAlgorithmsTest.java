package org.hzt.graph;

import org.hzt.utils.collections.MutableMapX;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class GraphAlgorithmsTest {
    // TODO: 8-6-2022 implement

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
        final MutableMapX<String, WeightedNode<String>> graph = MutableMapX.empty();
        final WeightedNode<String> A = WeightedNode.of("A");
        final WeightedNode<String> B = WeightedNode.of("B");
        final WeightedNode<String> C = WeightedNode.of("C");
        final WeightedNode<String> D = WeightedNode.of("D");
        final WeightedNode<String> E = WeightedNode.of("E");
        WeightedEdge.edgesInBothDirectionsBetween(A, B, 3);
        A.addEdgeTo(C, 2)
                .addEdgeTo(D, 6);

        fail("Not implemented");
    }

}
