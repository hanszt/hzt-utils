package org.hzt.utils.sequences.primitives;

import java.util.function.IntConsumer;

final class PrimitiveSequenceHelper {

    private PrimitiveSequenceHelper() {
    }

    static class HoldingConsumer implements IntConsumer {
        private int value = 0;
        @Override
        public void accept(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
