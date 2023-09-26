package org.hzt.swing_utils.function.window_listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@FunctionalInterface
public interface WindowOpenedListener extends WindowListener {

    @Override
    default void windowActivated(final WindowEvent e) {
    }

    @Override
    default void windowClosing(final WindowEvent e) {
    }

    @Override
    default void windowClosed(final WindowEvent e) {
    }

    @Override
    default void windowIconified(final WindowEvent e) {
    }

    @Override
    default void windowDeiconified(final WindowEvent e) {
    }

    @Override
    default void windowDeactivated(final WindowEvent e) {
    }
}
