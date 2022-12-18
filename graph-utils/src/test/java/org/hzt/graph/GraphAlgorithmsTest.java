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
        MutableMapX<String, WeightedNode<String>> graph = MutableMapX.empty();
        WeightedNode<String> A = WeightedNode.of("A");
        WeightedNode<String> B = WeightedNode.of("B");
        WeightedNode<String> C = WeightedNode.of("C");
        WeightedNode<String> D = WeightedNode.of("D");
        WeightedNode<String> E = WeightedNode.of("E");
        WeightedEdge.edgesInBothDirectionsBetween(A, B, 3);
        A.addEdgeTo(C, 2)
                .addEdgeTo(D, 6);

        fail("Not implemented");
    }

}
