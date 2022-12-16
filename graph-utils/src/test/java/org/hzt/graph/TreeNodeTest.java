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
import java.util.stream.Collectors;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.*;

class TreeNodeTest {

    @Nested
    class ToTreeStringTests {

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
        void testToTreeStringCustomized() {
            final Node root = buildTree();
            final String s = root.toTreeString(1, "-", n -> (n.isLeaf() ? "leaf: " : "") + n);

            final String expected = "root\n" +
                    "-c1\n" +
                    "--leaf: c4\n" +
                    "--leaf: c5\n" +
                    "-c2\n" +
                    "--leaf: c6\n" +
                    "--leaf: c7\n" +
                    "-leaf: c3\n";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized2() {
            final Node root = buildTree();
            final String s = root.toTreeString();

            final String expected = "root[c1[c4, c5], c2[c6, c7], c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized3() {
            final Node root = buildTree();
            final String s = root.toTreeString(n -> (n.isLeaf() ? "leaf: " : "") + n.name);

            final String expected = "root[c1[leaf: c4, leaf: c5], c2[leaf: c6, leaf: c7], leaf: c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized4() {
            final Node root = buildTree();
            final String s = root.toTreeString(" { ", " ; ", " } ", Objects::toString);

            final String expected = "root { c1 { c4 ; c5 }  ; c2 { c6 ; c7 }  ; c3 } ";

            assertEquals(expected, s);
        }
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

    @Test
    void testStream() {
        final Node root = buildTree();

        final List<String> strings = root.stream()
                .map(node -> node.name)
                .filter(n -> n.length() < 3)
                .collect(Collectors.toList());

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
    void testFileNode() {
        final FileX fileX = new FileX(".");

        final List<String> files = fileX.asSequence()
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
                    .map(list -> Collections.unmodifiableList(Arrays.stream(list)
                            .map(FileX::new)
                            .collect(Collectors.toList())))
                    .orElse(Collections.emptyList());
        }
    }
}
