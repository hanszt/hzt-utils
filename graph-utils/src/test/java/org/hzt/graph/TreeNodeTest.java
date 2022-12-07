package org.hzt.graph;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeNodeTest {

    @Test
    void testToTreeString() {
        final Node root = buildTree();
        final String s = root.toTreeString(2);

        final String expected = "" +
                "root\n" +
                "  c1\n" +
                "    c4\n" +
                "    c5\n" +
                "  c2\n" +
                "    c6\n" +
                "    c7\n" +
                "  c3\n";

        assertEquals(expected, s);
    }

    @Test
    void testMapLeafs() {
        final Node root = buildTree();
        final List<String> strings = root.mapLeafsTo(ArrayList::new, s -> s.name);

        final List<String> fromSequence = root.asSequence()
                .filter(n -> n.children.isEmpty())
                .map(node -> node.name)
                .toList();

        strings.forEach(System.out::println);

        assertAll(
                () -> assertEquals(strings, fromSequence),
                () -> assertEquals(Arrays.asList("c4", "c5", "c6", "c7", "c3"), strings)
        );
    }

    @Test
    void testMap() {
        final Node root = buildTree();
        final List<String> strings = root.mapTo(ArrayList::new, s -> s.name);
        strings.forEach(System.out::println);
        assertEquals(Arrays.asList("root", "c1", "c4", "c5", "c2", "c6", "c7", "c3"), strings);
    }

    @Test
    void testAsSequence() {
        final Node root = buildTree();
        final List<String> strings = root.asSequence()
                .map(node -> node.name)
                .filter(n -> n.length() < 3)
                .toList();
        strings.forEach(System.out::println);
        assertEquals(Arrays.asList("c1", "c4", "c5", "c2", "c6", "c7", "c3"), strings);
    }

    @NotNull
    private static TreeNodeTest.Node buildTree() {
        final Node c1 = new Node("c1")
                .addChildren(Arrays.asList(
                        new Node("c4"),
                        new Node("c5")));
        final Node c2 = new Node("c2")
                .addChildren(Arrays.asList(
                        new Node("c6"),
                        new Node("c7")));
        return new Node("root")
                .addChildren(Arrays.asList(c1, c2, new Node("c3")));
    }

    @Test
    void testGetParentThrowsUnsupportedOperationExceptionByDefault() {
        final Node node = new Node("test");
        assertThrows(UnsupportedOperationException.class, () -> tryPrintParent(node));
    }

    private static void tryPrintParent(Node node) {
        final Node parent = node.getParent();
        System.out.println(parent);
    }


    private static class Node implements TreeNode<Node, Node> {

        private final String name;
        private final List<Node> children;

        public Node(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        @Override
        public List<Node> getChildren() {
            return children;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
