package org.hzt.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public final class InternalTreeNode<T> implements SimpleTreeNode<T> {

    private final List<SimpleTreeNode<T>> children = new ArrayList<>();

    static <T> InternalTreeNode<T> parse(String s, Delimiters delimiters, Function<String, T> function) {
        return parse(new Parser(s, delimiters.opening(), delimiters.separator(), delimiters.closing()), function);
    }

    static <T> InternalTreeNode<T> parse(Parser parser, Function<String, T> function) {
        var node = new InternalTreeNode<T>();
        parser.index++;
        var next = parser.nextToken();
        while (!parser.closing.equals(next)) {
            if (parser.separator.equals(next)) {
                parser.index += parser.separator.length();
            } else if (parser.opening.equals(next)) {
                node.children.add(InternalTreeNode.parse(parser, function));
            } else {
                node.children.add(new LeafTreeNode<>(function.apply(parser.nextPart())));
            }
            next = parser.nextToken();
        }
        parser.index += next.length();
        return node;
    }

    @Override
    public Iterator<SimpleTreeNode<T>> childrenIterator() {
        return children.iterator();
    }

    @Override
    public String toString() {
        return "InternalTreeNode{" +
                "children=" + children +
                '}';
    }

    private static final class Parser {
        final String s;
        private final String opening;
        private final String separator;
        private final String closing;
        int index = 0;

        Parser(String s, final String opening, final String separator, final String closing) {
            this.s = s;
            this.opening = opening;
            this.separator = separator;
            this.closing = closing;
        }

        String nextToken() {
            return String.valueOf(s.charAt(index));
        }

        String nextPart() {
            final var separatorIndex = s.indexOf(separator, index);
            final var closingIndex = s.indexOf(closing, index);
            final var part = s.substring(index, Math.min(
                    separatorIndex < 0 ? s.length() : separatorIndex,
                    closingIndex < 0 ? s.length() : closingIndex)
            );
            index += part.length();
            return part;
        }
    }
}
