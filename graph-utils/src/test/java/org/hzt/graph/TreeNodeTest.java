package org.hzt.graph;

import org.hzt.graph.tuples.DepthToTreeNode;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TreeNodeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeNodeTest.class);

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
            final var s = root.toTreeString(n -> (n.isLeaf() ? "leaf: " : "") + n.name());

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

    @Nested
    class ParseTreeNodeTests {

        @Test
        void testParse() {
            final var input = "[1,[2,[3,[4,[5,6,7]]]],8,9]";
            final var delimiters = new SimpleTreeNode.Delimiters("[", ",", "]");
            final var node = SimpleTreeNode.parse(input, delimiters, Integer::parseInt);

            final var treeString = node.toTreeString("[", ",", "]",
                    s -> s instanceof LeafTreeNode(Integer value) ? value.toString() : "");

            final var values = node.leafValues().toList();

            assertThat(values).isEqualTo(List.of(1, 8, 9, 2, 3, 4, 5, 6, 7));
            assertThat(treeString).isEqualTo(input);
        }
    }

    @Test
    void testToLeafs() {
        final var root = buildPersonTree();

        LOGGER.atDebug().setMessage(() -> root.toTreeString()).log();

        final var leafs = root.depthFirstSequence()
                .filter(TreeNode::isLeaf)
                .map(node -> node.name())
                .toList();

        assertEquals(List.of("c10", "c5", "c6", "c7", "c8", "c3"), leafs);
    }

    @Test
    void testToAllInternalNodes() {
        final var root = buildPersonTree();

        LOGGER.atDebug().setMessage(() -> root.toTreeString(1)).log();

        final var internalNodes = root.depthFirstSequence()
                .filter(TreeNode::isInternal)
                .map(node -> node.name())
                .toList();

        assertEquals(List.of("root", "c1", "c4", "c2"), internalNodes);
    }

    @Test
    void testMap() {
        final var root = buildPersonTree();

        LOGGER.atDebug().setMessage(() -> root.toTreeString()).log();

        final List<String> strings = root.depthFirstSequence().mapTo(ArrayList::new, Person::name);

        assertEquals(List.of("root", "c1", "c4", "c10", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }


    @Test
    void testDepthFirstSequence() {
        final var root = buildPersonTree();

        LOGGER.atDebug().setMessage(() -> root.toTreeString(1)).log();

        final var strings = root.depthFirstSequence()
                .map(Person::name)
                .toList();

        assertEquals(List.of("root", "c1", "c4", "c10", "c5", "c2", "c6", "c7", "c8", "c3"), strings);
    }

    private static Person buildPersonTree() {
        final var c1 = new MutablePerson("c1")
                .addChildrenWithThisAsParent(List.of(
                        new MutablePerson("c4").addChildWithThisAsParent(new MutablePerson("c10")),
                        new MutablePerson("c5")));
        final var c2 = new MutablePerson("c2");
        c2.addChildren(List.of(
                new MutablePerson("c6"),
                new MutablePerson("c7")));
        return new MutablePerson("root")
                .addChildrenWithThisAsParent(List.of(c1, c2.addChild(new MutablePerson("c8").withParent(c2)), new MutablePerson("c3")));
    }

    private static class MutablePerson implements MutableTreeNode<Person>, Person {

        private final String name;
        private final List<Person> children = new ArrayList<>();

        private Person parent;

        private MutablePerson(final String name) {
            this.name = name;
        }

        @Override
        public Iterator<Person> childrenIterator() {
            return children.iterator();
        }

        @Override
        public List<Person> getMutableChildren() {
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

        @Override
        public String name() {
            return name;
        }
    }

    private interface Person extends TreeNode<Person> {
        String name();
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

            LOGGER.atDebug().setMessage(() -> "root.getName() = " + root.getName()).log();
            LOGGER.atDebug().setMessage(root::toTreeString).log();

            final var fileNames = root.siblingSequence().map(File::getName).toList();

            LOGGER.debug("fileNames = {}", fileNames);

            assertAll(
//                    () -> assertThat(root.parentSequence().last().siblingSequence()).hasSize(1),
                    () -> assertThat(fileNames).hasSizeGreaterThan(1),
                    () -> assertThat(new FileX(".").siblingSequence()).hasSize(1)
            );
        }

        @Test
        void testDepthTrackingTraversal() {
            final var fileX = new FileX("../TreeNodeTest.java")
                    .parentSequence()
                    .first(f -> "graph-utils".equals(f.getName()));

            final var nodeToTreeDept1 = fileX.breadthFirstDepthTrackingSequence()
                    .onEach(e -> LOGGER.debug("{}", e))
                    .toList();

            final var nodeToTreeDepth2 = fileX.depthFirstDepthTrackingSequence()
                    .onEach(e -> LOGGER.debug("{}", e))
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

            LOGGER.atDebug().setMessage(() -> fileX.toTreeString(2, File::getName)).log();

            assertTrue(files.contains("pom.xml"));
        }

        @Test
        void testTraverseToRoot() {
            final var fileX = new FileX(".");

            final var file = fileX.breadthFirstSequence()
                    .first(n -> "TreeNodeTest.java".equals(n.getName()));

            final var root = file.parentSequence()
                    .onEach(s -> LOGGER.atDebug().setMessage(() -> s.getAbsolutePath()).log())
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
                    .onEach(e -> LOGGER.debug("{}", e))
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
                    .onEach(e -> LOGGER.debug("{}", e))
                    .mapByKeys(File::getName)
                    .toMap();

            assertThat(map).containsKey(TreeNodeTest.class.getSimpleName() + ".java");
        }
    }

    @Test
    void testParentIterator() {
        var person = buildPersonTree();
        LOGGER.atDebug().setMessage(() -> person.toTreeString()).log();
        var leafPerson = person.depthFirstSequence().first(Person::isLeaf);

        var parentIterator = leafPerson
                .parentSequence()
                .iterator();

        assertThat(leafPerson.name()).isEqualTo("c10");
        assertThat(parentIterator.next().name()).isEqualTo("c10");
        assertThat(parentIterator.next().name()).isEqualTo("c4");
        assertThat(parentIterator.next().name()).isEqualTo("c1");
        assertThat(parentIterator.next().name()).isEqualTo("root");
        assertThat(parentIterator).isExhausted();
    }

    @Nested
    class SimpleNodeTests {

        @Test
        void testSimpleNode() {
            final var simpleTreeNode = buildSimpleTreeNodeTree();

            LOGGER.atDebug().setMessage(() -> "Dfs string:").log();
            LOGGER.atDebug().setMessage(() -> simpleTreeNode.toTreeString(1, "-", n -> n.name) + "\n").log();
            LOGGER.atDebug().setMessage(() -> "Bfs string").log();
            LOGGER.atDebug().setMessage(() -> simpleTreeNode.toBFSTreeString(1, "-", n -> n.name) + "\n").log();

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

        private record Node(String name, SimpleNodeTests.Node... children) implements TreeNode<Node> {

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
                final var that = (Node) obj;
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

    private static final class FileX extends File implements TreeNode<FileX> {

        public FileX(final String pathname) {
            super(pathname);
        }

        public FileX(final File file) {
            this(file.getAbsolutePath());
        }

        @Override
        public Iterator<FileX> childrenIterator() {
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
