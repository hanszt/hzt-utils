package org.hzt.graph;

import org.hzt.utils.It;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeNodeTest {

    @Nested
    class ToTreeStringTests {

        @Test
        void testToTreeString() {
            final Person root = buildTree();
            final String s = root.toTreeString(2);

            final String expected = "" +
                    "root\n" +
                    "  c1\n" +
                    "    c4\n" +
                    "      c10\n" +
                    "    c5\n" +
                    "  c2\n" +
                    "    c6\n" +
                    "    c7\n" +
                    "  c3\n";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized() {
            final Person root = buildTree();
            final String s = root.toTreeString(1, "-", n -> (n.isLeaf() ? "leaf: " : "") + n);

            final String expected = "root\n" +
                    "-c1\n" +
                    "--c4\n" +
                    "---leaf: c10\n" +
                    "--leaf: c5\n" +
                    "-c2\n" +
                    "--leaf: c6\n" +
                    "--leaf: c7\n" +
                    "-leaf: c3\n";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized2() {
            final Person root = buildTree();
            final String s = root.toTreeString();

            final String expected = "root[c1[c4[c10], c5], c2[c6, c7], c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized3() {
            final Person root = buildTree();
            final String s = root.toTreeString(n -> (n.isLeaf() ? "leaf: " : "") + n.name);

            final String expected = "root[c1[c4[leaf: c10], leaf: c5], c2[leaf: c6, leaf: c7], leaf: c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized4() {
            final Person root = buildTree();
            final String s = root.toTreeString(" { ", " ; ", " } ", Objects::toString);

            final String expected = "root { c1 { c4 { c10 }  ; c5 }  ; c2 { c6 ; c7 }  ; c3 } ";

            assertEquals(expected, s);
        }
    }

    @Test
    void testMapLeafs() {
        final Person root = buildTree();
        final List<String> strings = root.mapLeafsTo(ArrayList::new, s -> s.name);

        final List<String> fromSequence = root.depthFirstSequence()
                .filter(org.hzt.graph.TreeNode::isLeaf)
                .map(node -> node.name)
                .toList();

        strings.forEach(It::println);

        assertAll(
                () -> assertEquals(strings, fromSequence),
                () -> assertEquals(Arrays.asList("c10", "c5", "c6", "c7", "c3"), strings)
        );
    }

    @Test
    void testMap() {
        final Person root = buildTree();

        final List<String> strings = root.mapTo(ArrayList::new, s -> s.name);
        strings.forEach(It::println);

        assertEquals(Arrays.asList("root", "c1", "c4", "c10", "c5", "c2", "c6", "c7", "c3"), strings);
    }

    @Test
    void testAsSequence() {
        final Person root = buildTree();

        final List<String> strings = root.depthFirstSequence()
                .map(node -> node.name)
                .filter(n -> n.length() < 3)
                .toList();

        strings.forEach(It::println);
        assertEquals(Arrays.asList("c1", "c4", "c5", "c2", "c6", "c7", "c3"), strings);
    }

    @Test
    void testStream() {
        final Person root = buildTree();

        final List<String> strings = root.depthFirstSequence()
                .stream()
                .map(node -> node.name)
                .filter(n -> n.length() < 3)
                .collect(Collectors.toList());

        strings.forEach(It::println);
        assertEquals(Arrays.asList("c1", "c4", "c5", "c2", "c6", "c7", "c3"), strings);
    }

    @Test
    void testRemoveBranch() {
        final Person root = buildTree();

        println(root.toTreeString(2));

        final Person c1 = root.breadthFirstSequence().first(s -> "c1".equals(s.name));
        final Person node = root.removeSubTree(c1);

        println();
        println(root.toTreeString(2));

        final String[] expected = {"root", "c2", "c6", "c7", "c3"};
        Assertions.assertArrayEquals(expected, node.depthFirstSequence().toArrayOf(n -> n.name, String[]::new));
    }

    private static TreeNodeTest.Person buildTree() {
        final Person c1 = new Person("c1").addChildren(Arrays.asList(
                new Person("c4").addChild(new Person("c10")),
                new Person("c5")));
        final Person c2 = new Person("c2")
                .addChildren(Arrays.asList(
                        new Person("c6"),
                        new Person("c7")));
        return new Person("root")
                .addChildren(Arrays.asList(c1, c2, new Person("c3")));
    }

    @Nested
    class FileXTests {
        @Test
        void testTraverseToParent() {
            final FileX fileX = new FileX(".");

            final FileX file = fileX.breadthFirstSequence()
                    .first(n -> "TreeNodeTest.java".equals(n.getName()));

            final FileX parent = file.parent();
            final FileX itsParent = parent.parent();

            assertAll(
                    () -> assertEquals("graph", parent.getName()),
                    () -> assertEquals("hzt", itsParent.getName())
            );
        }

        @Test
        void testFileXAsSequence() {
            final FileX fileX = new FileX(".");

            final List<String> files = fileX.breadthFirstSequence()
                    .map(File::getName)
                    .toList();

            println(fileX.toTreeString(2, File::getName));

            assertTrue(files.contains("pom.xml"));
        }

        @Test
        void testTraverseToRoot() {
            final FileX fileX = new FileX(".");

            final FileX file = fileX.breadthFirstSequence()
                    .first(n -> "TreeNodeTest.java".equals(n.getName()));

            final FileX root = file.parentSequence()
                    .onEach(s -> System.out.println(s.getAbsolutePath()))
                    .last();

            assertAll(
                    () -> assertEquals("", root.getName()),
                    () -> assertThrows(IllegalStateException.class, root::parent)
            );
        }

        @Test
        void testBreadthFirstSearch() {
            final FileX root = new FileX(".");

            final Map<String, Integer> map = root.breadthFirstSequence()
                    .filter(n -> n.getName().endsWith("java"))
                    .associateWith(TreeNode::treeDepth)
                    .onEach(It::println)
                    .mapByKeys(File::getName)
                    .toMap();

            assertTrue(map.containsKey(TreeNodeTest.class.getSimpleName() + ".java"));
        }

        @Test
        void testDepthFirstSearch() {
            final FileX root = new FileX(".");

            final Map<String, Integer> map = root.depthFirstSequence()
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

        public FileX(String pathname) {
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

        @Override
        public Optional<FileX> optionalParent() {
            return Optional.ofNullable(getParentFile()).map(FileX::new);
        }
    }
}
