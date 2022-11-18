package org.hzt.fx.utils.iterators;

public enum State {
    INIT_UNKNOWN, NEXT_UNKNOWN, CONTINUE, DONE, FAILED;

    public boolean isUnknown() {
        return this == State.INIT_UNKNOWN || this == State.NEXT_UNKNOWN;
    }
}
