package org.hzt.swing_utils.function.document_listeners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@FunctionalInterface
public interface InsertUpdateListener extends DocumentListener {

    @Override
    default void removeUpdate(final DocumentEvent ev) {
    }

    @Override
    default void changedUpdate(final DocumentEvent ev) {
    }
}
