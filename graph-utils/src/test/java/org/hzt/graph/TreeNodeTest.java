package org.hzt.graph;

import org.hzt.graph.tuples.DepthToTreeNode;
import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeNodeTest {

    @Nested
    class ToTreeStringTests {

        @Test
        void testToTreeString() {
            final Person root = buildPersonTree();
            final String s = root.toTreeString(2);

            final String expected = "root\n" +
                                    "  c1\n" +
                                    "    c4\n" +
                                    "      c10\n" +
                                    "    c5\n" +
                                    "  c2\n" +
                                    "    c6\n" +
                                    "    c7\n" +
                                    "    c8\n" +
                                    "  c3";

            assertEquals(expected, s);
        }

        @Test
        void testToBfsTreeString() {
            final Person root = buildPersonTree();
            final String s = root.toBFSTreeString(2);

            final String expected = "root\n" +
                                    "  c1\n" +
                                    "  c2\n" +
                                    "  c3\n" +
                                    "    c4\n" +
                                    "    c5\n" +
                                    "    c6\n" +
                                    "    c7\n" +
                                    "    c8\n" +
                                    "      c10";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized() {
            final Person root = buildPersonTree();
            final String s = root.toTreeString(1, "-", n -> (n.isLeaf() ? "leaf: " : "") + n);

            final String expected = "root\n" +
                                    "-c1\n" +
                                    "--c4\n" +
                                    "---leaf: c10\n" +
                                    "--leaf: c5\n" +
                                    "-c2\n" +
                                    "--leaf: c6\n" +
                                    "--leaf: c7\n" +
                                    "--leaf: c8\n" +
                                    "-leaf: c3";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized2() {
            final Person root = buildPersonTree();
            final String s = root.toTreeString();

            final String expected = "root[c1[c4[c10], c5], c2[c6, c7, c8], c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized3() {
            final Person root = buildPersonTree();
            final String s = root.toTreeString(n -> (n.isLeaf() ? "leaf: " : "") + n.name);

            final String expected = "root[c1[c4[leaf: c10], leaf: c5], c2[leaf: c6, leaf: c7, leaf: c8], leaf: c3]";

            assertEquals(expected, s);
        }

        @Test
        void testToTreeStringCustomized4() {
            final Person root = buildPersonTree();
            final String s = root.toTreeString(" { ", " ; ", " } ", Objects::toString);

            final String expected = "root { c1 { c4 { c10 }  ; c5 }  ; c2 { c6 ; c7 ; c8 }  ; c3 } ";

            assertEquals(expected, s);
        }
    }

    @Test
    void testToLeafs() {
        final Person root = buildPersonTree();

        final List<String> leafs = root.depthFirstSequence()
                .filter(TreeNode::isLeaf)
                .map(node -> node.name)
                .toList();

        assertEquals(Arrays.asList("c10", "c5", "c6", "c7", "c8", "c3"), leafs);
    }

    @Test
    void testToAllInternalNodes() {
        final Person root = buildPersonTree();

        System.out.println(root.toTreeString());

        final String[] internalNodes = root.depthFirstSequence()
                .filter(TreeNode::isInternal)
                .map(node -> node.name)
                .toTypedArray(String[]::new);

        assertArrayEquals(new String[]{"root", "c1", "c4", "c2"}, internalNodes);
    }

    @Test
    void testMap() {
        final Person root = buildPersonTree();

        final List<String> strings = root.depthFirstSequence().mapTo(ArrayList::new, s -> s.name);

        assertIterableEquals(Arrays.asList("root", "c1", "c4", "c10", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    @Test
    void testDepthFirstSequence() {
        final Person root = buildPersonTree();

        final List<String> strings = root.depthFirstSequence()
                .map(node -> node.name)
                .toList();

        assertIterableEquals(Arrays.asList("root", "c1", "c4", "c10", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    @Test
    void testStream() {
        final Person root = buildPersonTree();

        final List<String> strings = root.depthFirstSequence()
                .stream()
                .map(node -> node.name)
                .filter(n -> n.length() == 2)
                .collect(toList());

        assertIterableEquals(Arrays.asList("c1", "c4", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    @Test
    void testRemoveBranch() {
        final Person root = buildPersonTree();

        println(root.toTreeString(2));

        final Person c1 = root.breadthFirstSequence().first(s -> "c1".equals(s.name));
        final Person node = root.removeSubTree(c1);

        println();
        println(root.toTreeString(2));

        final String[] expected = {"root", "c2", "c6", "c7", "c8", "c3"};
        Assertions.assertArrayEquals(expected, node.depthFirstSequence().toArrayOf(n -> n.name, String[]::new));
    }

    private static TreeNodeTest.Person buildPersonTree() {
        final Person c1 = new Person("c1").addChildren(Arrays.asList(
                new Person("c4").addChild(new Person("c10")),
                new Person("c5")));
        final Person c2 = new Person("c2")
                .addChildren(Arrays.asList(
                        new Person("c6"),
                        new Person("c7")));
        return new Person("root")
                .addChildren(Arrays.asList(c1, c2.addChild(new Person("c8").withParent(c2)), new Person("c3")));
    }

    private static class Person implements TreeNode<Person, Person> {

        private final String name;
        private final List<Person> children;

        private Person parent;

        public Person(final String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        @Override
        public Iterator<Person> childrenIterator() {
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
        public Person withParent(final Person parent) {
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
            final FileX fileX = new FileX(".");

            final FileX file = fileX.breadthFirstSequence()
                    .first(n -> "TreeNodeTest.java".equals(n.getName()));

            final FileX parent = file.optionalParent().orElseThrow(NoSuchElementException::new);
            final FileX itsParent = parent.optionalParent().orElseThrow(NoSuchElementException::new);

            assertAll(
                    () -> assertEquals("graph", parent.getName()),
                    () -> assertEquals("hzt", itsParent.getName())
            );
        }

        @Test
        void testSiblings() {
            final FileX root = new FileX(System.getProperty("user.dir"));
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
            final FileX fileX = new FileX("../TreeNodeTest.java")
                    .parentSequence()
                    .first(f -> "graph-utils".equals(f.getName()));

            final List<DepthToTreeNode<FileX>> nodeToTreeDept1 = fileX.breadthFirstDepthTrackingSequence()
                    .onEach(System.out::println)
                    .toList();

            System.out.println();

            final List<DepthToTreeNode<FileX>> nodeToTreeDepth2 = fileX.depthFirstDepthTrackingSequence()
                    .onEach(System.out::println)
                    .sorted(comparingInt(DepthToTreeNode::treeDepth))
                    .toList();

            assertEquals(nodeToTreeDept1, nodeToTreeDepth2);
        }

        @Test
        void testFileXAsBreadthFirstSequence() {
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

            final Optional<FileX> optionalParent = root.optionalParent();

            assertAll(
                    () -> assertEquals("", root.getName()),
                    () -> assertFalse(optionalParent::isPresent)
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
            final TreeNodeTest.FileX root = new FileX(".");

            final Map<String, Integer> map = root.depthFirstSequence()
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
            final Node simpleTreeNode = buildSimpleTreeNodeTree();

            println("Dfs string:");
            println(simpleTreeNode.toTreeString(1, "-", n -> n.name) + "\n");
            println("Bfs string");
            println(simpleTreeNode.toBFSTreeString(1, "-", n -> n.name) + "\n");

            final List<String> breadthFirst = simpleTreeNode.breadthFirstSequence().toListOf(s -> s.name);

            final List<String> expected = Arrays.asList("root", "internal 1", "leaf 10", "leaf 0", "internal 2", "leaf 6", "internal 4", "leaf 9",
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

        private final class Node implements TreeNode<Node, Node> {
            private final String name;
            private final Node[] children;

            private Node(final String name, final Node... children) {
                this.name = name;
                this.children = children;
            }

            @Override
            public Iterator<Node> childrenIterator() {
                return Sequence.of(children).iterator();
            }

            @Override
            public boolean equals(final Object obj) {
                if (obj == this) {
                    return true;
                }
                if (obj == null || obj.getClass() != this.getClass()) {
                    return false;
                }
                final Node that = (Node) obj;
                return Objects.equals(this.name, that.name) &&
                       Arrays.equals(this.children, that.children);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, Arrays.hashCode(children));
            }

            @Override
            public String toString() {
                return "Node[" + "name=" + name + ", " + ']';
            }

        }
    }

    private static final class FileX extends File implements TreeNode<FileX, FileX> {

        public FileX(final String pathname) {
            super(pathname);
        }

        public FileX(final File file) {
            this(file.getAbsolutePath());
        }

        @Override
        public Iterator<FileX> childrenIterator() {
            final File[] files = listFiles();
            return (files != null ? Arrays.stream(files) : Stream.<File>empty())
                    .flatMap(values -> Stream.of(values).map(FileX::new))
                    .iterator();
        }

        @Override
        public Optional<FileX> optionalParent() {
            return Optional.ofNullable(getParentFile()).map(FileX::new);
        }
    }
}
