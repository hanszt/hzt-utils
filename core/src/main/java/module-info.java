open module core {

    requires org.jetbrains.annotations;
    requires java.desktop;
    requires primitive;

    exports hzt.collections;
    exports hzt.collections.primitives;
    exports hzt.function;
    exports hzt.function.predicates;
    exports hzt.stream;
    exports hzt.sequences;
    exports hzt.sequences.primitives;
    exports hzt.statistics;
    exports hzt.strings;
    exports hzt.numbers;
    exports hzt.io;
    exports hzt.iterables;
    exports hzt.iterables.primitives;
    exports hzt.tuples;
    exports hzt.utils;
    exports hzt.collectors;
    exports hzt.collectors.primitves;
    exports hzt.ranges;
    exports hzt.progressions;
    exports hzt.iterators.primitives;
}
