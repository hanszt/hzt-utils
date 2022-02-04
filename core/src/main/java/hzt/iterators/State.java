package hzt.iterators;

enum State {
    INIT_UNKNOWN, NEXT_UNKNOWN, CONTINUE, DONE, FAILED;

    boolean isUnknown() {
        return this == State.INIT_UNKNOWN || this == State.NEXT_UNKNOWN;
    }
}
