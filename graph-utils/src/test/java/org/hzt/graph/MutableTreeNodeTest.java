package org.hzt.graph;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableTreeNodeTest {

    private static final Logger logger = LoggerFactory.getLogger(MutableTreeNodeTest.class);

    @Nested
    class IsTreeTests {

        @Test
        void testHasSelfReferenceSoNoTree() {
            final var root = buildPersonTree();
            root.getMutableChildren().getFirst().getMutableChildren().add(root);

            final var c2 = root.breadthFirstSequence().first(p -> "c2".equals(p.name));

            assertTrue(c2.isTree());
            assertFalse(root.isTree());
        }

        @Test
        void testIsTreeStructure() {
            final var root = buildPersonTree();
            assertTrue(root.isTree());
        }
    }

    @Test
    void testRemoveBranch() {
        final var root = buildPersonTree();

        logger.atDebug().setMessage(() -> root.toTreeString(2)).log();

        final var c1 = root.breadthFirstSequence().first(s -> "c1".equals(s.name));
        final var node = root.removeSubTree(c1);

        logger.atDebug().setMessage(() -> "After prune: " + root.toTreeString(2)).log();

        final var expected = new String[]{"root", "c2", "c6", "c7", "c8", "c3"};
        final String[] actual = node.depthFirstSequence().toArrayOf(n -> n.name, String[]::new);
        assertThat(actual).isEqualTo(expected);
    }

    private static MutableTreeNodeTest.Person buildPersonTree() {
        final var c1 = new MutableTreeNodeTest.Person("c1")
                .addChildrenWithThisAsParent(List.of(
                        new MutableTreeNodeTest.Person("c4").addChildWithThisAsParent(new MutableTreeNodeTest.Person("c10")),
                        new MutableTreeNodeTest.Person("c5")));
        final var c2 = new MutableTreeNodeTest.Person("c2")
                .addChildren(List.of(
                        new MutableTreeNodeTest.Person("c6"),
                        new MutableTreeNodeTest.Person("c7")));
        return new MutableTreeNodeTest.Person("root")
                .addChildrenWithThisAsParent(List.of(c1, c2.addChild(new MutableTreeNodeTest.Person("c8").withParent(c2)), new MutableTreeNodeTest.Person("c3")));
    }


    private static class Person implements MutableTreeNode<Person> {

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
    }

}