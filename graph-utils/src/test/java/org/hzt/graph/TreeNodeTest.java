package org.hzt.graph;

import org.hzt.utils.It;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
                          c10
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
                    --c4
                    ---leaf: c10
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

            final var expected = "root[c1[c4[c10], c5], c2[c6, c7, c8], c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized3() {
            final var root = buildTree();
            final var s = root.toTreeString(n -> (n.isLeaf() ? "leaf: " : "") + n.name);

            final var expected = "root[c1[c4[leaf: c10], leaf: c5], c2[leaf: c6, leaf: c7, leaf: c8], leaf: c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized4() {
            final var root = buildTree();
            final var s = root.toTreeString(" { ", " ; ", " } ", Objects::toString);

            final var expected = "root { c1 { c4 { c10 }  ; c5 }  ; c2 { c6 ; c7 ; c8 }  ; c3 } ";

            assertEquals(expected, s);
        }
    }

    @Test
    void testMapLeafs() {
        final var root = buildTree();
        final List<String> strings = root.mapLeafsTo(ArrayList::new, s -> s.name);

        final var fromSequence = root.depthFirstSequence()
                .filter(org.hzt.graph.TreeNode::isLeaf)
                .map(node -> node.name)
                .toList();

        strings.forEach(It::println);

        assertAll(
                () -> assertEquals(strings, fromSequence),
                () -> assertEquals(List.of("c10", "c5", "c6", "c7", "c8", "c3"), strings)
        );
    }

    @Test
    void testMap() {
        final var root = buildTree();

        final List<String> strings = root.mapTo(ArrayList::new, s -> s.name);
        strings.forEach(It::println);

        assertEquals(List.of("root", "c1", "c4", "c10", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    @Test
    void testAsSequence() {
        final var root = buildTree();

        final var strings = root.depthFirstSequence()
                .map(node -> node.name)
                .filter(n -> n.length() < 3)
                .toList();

        strings.forEach(It::println);
        assertEquals(List.of("c1", "c4", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    @Test
    void testStream() {
        final var root = buildTree();

        final var strings = root.depthFirstSequence()
                .stream()
                .map(node -> node.name)
                .filter(n -> n.length() < 3)
                .toList();

        strings.forEach(It::println);
        assertEquals(List.of("c1", "c4", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    @Test
    void testRemoveBranch() {
        final var root = buildTree();

        println(root.toTreeString(2));

        final var c1 = root.breadthFirstSequence().first(s -> "c1".equals(s.name));
        final var node = root.removeSubTree(c1);

        println();
        println(root.toTreeString(2));

        final var expected = new String[]{"root", "c2", "c6", "c7", "c8", "c3"};
        Assertions.assertArrayEquals(expected, node.depthFirstSequence().toArrayOf(n -> n.name, String[]::new));
    }

    @NotNull
    private static TreeNodeTest.Person buildTree() {
        final var c1 = new Person("c1").addChildren(List.of(
                new Person("c4").addChild(new Person("c10")),
                new Person("c5")));
        final var c2 = new Person("c2")
                .addChildren(List.of(
                        new Person("c6"),
                        new Person("c7"),
                        new Person("c8")));
        return new Person("root")
                .addChildren(List.of(c1, c2, new Person("c3")));
    }

    @Nested
    class FileXTests {
        @Test
        void testTraverseToParent() {
            final var fileX = new FileX(".");

            final var file = fileX.breadthFirstSequence()
                    .first(n -> "TreeNodeTest.java".equals(n.getName()));

            final var parent = file.parent();
            final var itsParent = parent.parent();

            assertAll(
                    () -> assertEquals("graph", parent.getName()),
                    () -> assertEquals("hzt", itsParent.getName())
            );
        }

        @Test
        void testFileXAsSequence() {
            final var fileX = new FileX(".");

            final var files = fileX.breadthFirstSequence()
                    .map(File::getName)
                    .toList();

            println(fileX.toTreeString(2, File::getName));

            assertTrue(files.contains("pom.xml"));
        }

        @Test
        void testTraverseToRoot() {
            final var fileX = new FileX(".");

            final var file = fileX.breadthFirstSequence()
                    .first(n -> "TreeNodeTest.java".equals(n.getName()));

            final var root = file.parentSequence()
                    .onEach(s -> System.out.println(s.getAbsolutePath()))
                    .last();

            assertAll(
                    () -> assertEquals("", root.getName()),
                    () -> assertThrows(IllegalStateException.class, root::parent)
            );
        }

        @Test
        void testBreadthFirstSearch() {
            final var root = new FileX(".");

            final var map = root.breadthFirstSequence()
                    .filter(n -> n.getName().endsWith("java"))
                    .associateWith(TreeNode::treeDepth)
                    .onEach(It::println)
                    .mapByKeys(File::getName)
                    .toMap();

            assertTrue(map.containsKey(TreeNodeTest.class.getSimpleName() + ".java"));
        }

        @Test
        void testDepthFirstSearch() {
            final var root = new FileX(".");

            final var map = root.depthFirstSequence()
                    .filter(n -> n.getName().endsWith("java"))
                    .associateWith(TreeNode::treeDepth)
                    .onEach(It::println)
                    .mapByKeys(File::getName)
                    .toMap();

            assertTrue(map.containsKey(TreeNodeTest.class.getSimpleName() + ".java"));
        }
    }


    private static class Person implements TreeNode<Person, Person> {

        private final String name;
        private final List<Person> children;

        public Person(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        @Override
        public List<Person> getChildren() {
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
                    .map(list -> Stream.of(list)
                            .map(FileX::new)
                            .toList())
                    .orElse(Collections.emptyList());
        }

        @Override
        public Optional<FileX> optionalParent() {
            return Optional.ofNullable(getParentFile()).map(FileX::new);
        }
    }
}
