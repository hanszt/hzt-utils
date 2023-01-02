package org.hzt.graph;

import org.hzt.graph.tuples.DepthToTreeNode;
import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
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
            final var root = buildPersonTree();
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
                      c3""";

            assertEquals(expected, s);
        }

        @Test
        void testToBfsTreeString() {
            final var root = buildPersonTree();
            final var s = root.toBFSTreeString(2);

            final var expected = """
                     root
                       c1
                       c2
                       c3
                         c4
                         c5
                         c6
                         c7
                         c8
                           c10""";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized() {
            final var root = buildPersonTree();
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
                    -leaf: c3""";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized2() {
            final var root = buildPersonTree();
            final var s = root.toTreeString();

            final var expected = "root[c1[c4[c10], c5], c2[c6, c7, c8], c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized3() {
            final var root = buildPersonTree();
            final var s = root.toTreeString(n -> (n.isLeaf() ? "leaf: " : "") + n.name);

            final var expected = "root[c1[c4[leaf: c10], leaf: c5], c2[leaf: c6, leaf: c7, leaf: c8], leaf: c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized4() {
            final var root = buildPersonTree();
            final var s = root.toTreeString(" { ", " ; ", " } ", Objects::toString);

            final var expected = "root { c1 { c4 { c10 }  ; c5 }  ; c2 { c6 ; c7 ; c8 }  ; c3 } ";

            assertEquals(expected, s);
        }
    }

    @Test
    void testToLeafs() {
        final var root = buildPersonTree();

        System.out.println(root.toTreeString());

        final var leafs = root.depthFirstSequence()
                .filter(TreeNode::isLeaf)
                .map(node -> node.name)
                .toList();

        assertEquals(List.of("c10", "c5", "c6", "c7", "c8", "c3"), leafs);
    }

    @Test
    void testToAllInternalNodes() {
        final var root = buildPersonTree();

        System.out.println(root.toTreeString());

        final var internalNodes = root.depthFirstSequence()
                .filter(TreeNode::isInternal)
                .map(node -> node.name)
                .toList();

        assertEquals(List.of("root", "c1", "c4", "c2"), internalNodes);
    }

    @Test
    void testMap() {
        final var root = buildPersonTree();

        System.out.println(root.toTreeString());

        final List<String> strings = root.depthFirstSequence().mapTo(ArrayList::new, s -> s.name);

        assertEquals(List.of("root", "c1", "c4", "c10", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }


    @Test
    void testDepthFirstSequence() {
        final var root = buildPersonTree();

        System.out.println(root.toTreeString(1));

        final var strings = root.depthFirstSequence()
                .map(node -> node.name)
                .toList();

        assertEquals(List.of("root", "c1", "c4", "c10", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    @Test
    void testRemoveBranch() {
        final var root = buildPersonTree();

        println(root.toTreeString(2));

        final var c1 = root.breadthFirstSequence().first(s -> "c1".equals(s.name));
        final var node = root.removeSubTree(c1);

        println();
        println(root.toTreeString(2));

        final var expected = new String[]{"root", "c2", "c6", "c7", "c8", "c3"};
        assertArrayEquals(expected, node.depthFirstSequence().toArrayOf(n -> n.name, String[]::new));
    }

    @NotNull
    private static TreeNodeTest.Person buildPersonTree() {
        final var c1 = new Person("c1")
                .addChildrenWithThisAsParent(List.of(
                        new Person("c4").addChildWithThisAsParent(new Person("c10")),
                        new Person("c5")));
        final var c2 = new Person("c2")
                .addChildren(List.of(
                        new Person("c6"),
                        new Person("c7")));
        return new Person("root")
                .addChildren(List.of(c1, c2.addChild(new Person("c8").withParent(c2)), new Person("c3")));
    }

    private static class Person implements TreeNode<Person, Person> {

        private final String name;
        private final List<Person> children;

        private Person parent;

        public Person(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        @Override
        public @NotNull Iterator<Person> childrenIterator() {
            return children.iterator();
        }

        @Override
        public Collection<Person> getMutableChildren() {
            return children;
        }

        @Override
        public Optional<Person> optionalParent() {
            return Optional.ofNullable(parent);
        }

        @Override
        public Person withParent(Person parent) {
            this.parent = parent;
            return this;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Nested
    class FileXTests {
        @Test
        void testTraverseToParent() {
            final var fileX = new FileX(".");

            final var file = fileX.breadthFirstSequence()
                    .first(n -> "TreeNodeTest.java".equals(n.getName()));

            final var parent = file.optionalParent().orElseThrow();
            final var itsParent = parent.optionalParent().orElseThrow();

            assertAll(
                    () -> assertEquals("graph", parent.getName()),
                    () -> assertEquals("hzt", itsParent.getName())
            );
        }

        @Test
        void testSiblings() {
            final var root = new FileX(System.getProperty("user.dir"));
            System.out.println("root.getName() = " + root.getName());

            System.out.println(root.toTreeString());

            final List<String> fileNames = root.siblingSequence().map(File::getName).toList();

            assertAll(
                    () -> assertFalse(fileNames.isEmpty()),
                    () -> assertTrue(new FileX(".").siblingSequence().none())
            );
        }

        @Test
        void testDepthTrackingTraversal() {
            final var fileX = new FileX(".");

            final var nodeToTreeDept1 = fileX.breadthFirstDepthTrackingSequence()
                    .onEach(System.out::println)
                    .toList();

            System.out.println();

            final var nodeToTreeDepth2 = fileX.depthFirstDepthTrackingSequence()
                    .onEach(System.out::println)
                    .sorted(Comparator.comparingInt(DepthToTreeNode::treeDepth))
                    .toList();

            assertEquals(nodeToTreeDept1, nodeToTreeDepth2);
        }

        @Test
        void testFileXAsBreadthFirstSequence() {
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

            final var optionalParent = root.optionalParent();

            assertAll(
                    () -> assertEquals("", root.getName()),
                    () -> assertFalse(optionalParent::isPresent)
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

    @Nested
    class SimpleNodeTests {

        @Test
        void testSimpleNode() {
            final var simpleTreeNode = buildSimpleTreeNodeTree();

            println("Dfs string:");
            println(simpleTreeNode.toTreeString(1, "-", n -> n.name) + "\n");
            println("Bfs string");
            println(simpleTreeNode.toBFSTreeString(1, "-", n -> n.name) + "\n");

            final var breadthFirst = simpleTreeNode.breadthFirstSequence().toListOf(s -> s.name);

            final var expected = List.of("root", "internal 1", "leaf 10", "leaf 0", "internal 2", "leaf 6", "internal 4", "leaf 9",
                    "leaf 1", "leaf 2", "internal 3", "leaf 7", "leaf 8", "leaf 4", "leaf 5");
            assertEquals(expected, breadthFirst);
        }

        private Node buildSimpleTreeNodeTree() {
            return new Node("root",
                    new Node("internal 1",
                            new Node("leaf 0"),
                            new Node("internal 2",
                                    new Node("leaf 1"),
                                    new Node("leaf 2"),
                                    new Node("internal 3",
                                            new Node("leaf 4"),
                                            new Node("leaf 5"))),
                            new Node("leaf 6"),
                            new Node("internal 4",
                                    new Node("leaf 7"),
                                    new Node("leaf 8")),
                            new Node("leaf 9")),
                    new Node("leaf 10"));
        }

        private class Node implements TreeNode<Node, Node> {

            private final String name;
            private final Node[] children;

            public Node(String name, Node... children) {
                this.name = name;
                this.children = children;
            }

            @Override
            public @NotNull Iterator<Node> childrenIterator() {
                return Sequence.of(children).iterator();
            }
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
        public @NotNull Iterator<FileX> childrenIterator() {
            return Stream.ofNullable(listFiles())
                    .flatMap(values -> Stream.of(values).map(FileX::new))
                    .iterator();
        }

        @Override
        public Optional<FileX> optionalParent() {
            return Optional.ofNullable(getParentFile()).map(FileX::new);
        }
    }
}
