package org.hzt.graph;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.*;

class TreeNodeTest {

    @Nested
    class ToTreeStringTests {

        @Test
        void testToTreeString() {
            final var root = buildTree();
            final var s = root.toTreeString(2);

            final var expected = """
                    root
                      c1
                        c4
                        c5
                      c2
                        c6
                        c7
                        c8
                      c3
                    """;

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized() {
            final var root = buildTree();
            final var s = root.toTreeString(1, "-", n -> (n.isLeaf() ? "leaf: " : "") + n);

            final var expected = """
                    root
                    -c1
                    --leaf: c4
                    --leaf: c5
                    -c2
                    --leaf: c6
                    --leaf: c7
                    --leaf: c8
                    -leaf: c3
                    """;

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized2() {
            final var root = buildTree();
            final var s = root.toTreeString();

            final var expected = "root[c1[c4, c5], c2[c6, c7, c8], c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized3() {
            final var root = buildTree();
            final var s = root.toTreeString(n -> (n.isLeaf() ? "leaf: " : "") + n.name);

            final var expected = "root[c1[leaf: c4, leaf: c5], c2[leaf: c6, leaf: c7, leaf: c8], leaf: c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized4() {
            final var root = buildTree();
            final var s = root.toTreeString(" { ", " ; ", " } ", Objects::toString);

            final var expected = "root { c1 { c4 ; c5 }  ; c2 { c6 ; c7 ; c8 }  ; c3 } ";

            assertEquals(expected, s);
        }
    }

    @Test
    void testMapLeafs() {
        final var root = buildTree();
        final List<String> strings = root.mapLeafsTo(ArrayList::new, s -> s.name);

        final var fromSequence = root.asSequence()
                .filter(TreeNode::isLeaf)
                .map(node -> node.name)
                .toList();

        strings.forEach(System.out::println);

        assertAll(
                () -> assertEquals(strings, fromSequence),
                () -> assertEquals(List.of("c4", "c5", "c6", "c7", "c8", "c3"), strings)
        );
    }

    @Test
    void testMap() {
        final var root = buildTree();
        final List<String> strings = root.mapTo(ArrayList::new, s -> s.name);
        strings.forEach(System.out::println);
        assertEquals(List.of("root", "c1", "c4", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    @Test
    void testAsSequence() {
        final var root = buildTree();

        final var strings = root.asSequence()
                .map(node -> node.name)
                .filter(n -> n.length() < 3)
                .toList();

        strings.forEach(System.out::println);
        assertEquals(List.of("c1", "c4", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    @Test
    void testStream() {
        final var root = buildTree();

        final var strings = root.stream()
                .map(node -> node.name)
                .filter(n -> n.length() < 3)
                .toList();

        strings.forEach(System.out::println);
        assertEquals(List.of("c1", "c4", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    @NotNull
    private static Node buildTree() {
        final var c1 = new Node("c1")
                .addChildren(List.of(
                        new Node("c4"),
                        new Node("c5")));
        final var c2 = new Node("c2")
                .addChildren(List.of(
                        new Node("c6"),
                        new Node("c7"),
                        new Node("c8")));
        return new Node("root")
                .addChildren(List.of(c1, c2, new Node("c3")));
    }

    @Test
    void testFileNode() {
        final var fileX = new FileX(".");

        final var files = fileX.asSequence()
                .map(File::getName)
                .toList();

        println(fileX.toTreeString(2, File::getName));

        assertTrue(files.contains("pom.xml"));
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

    private static final class FileX extends File implements TreeNode<FileX, FileX> {

        public FileX(@NotNull String pathname) {
            super(pathname);
        }

        public FileX(File file) {
            this(file.getAbsolutePath());
        }

        @Override
        public List<FileX> getChildren() {
            return Optional.ofNullable(listFiles())
                    .map(list -> Arrays.stream(list)
                            .map(FileX::new)
                            .toList())
                    .orElse(Collections.emptyList());
        }
    }
}
