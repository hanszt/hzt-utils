package org.hzt.swing_utils.function.window_listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@FunctionalInterface
public interface WindowClosedListener extends WindowListener {

    @Override
    default void windowOpened(final WindowEvent e) {
    }

    @Override
    default void windowClosing(final WindowEvent e) {
    }

    @Override
    default void windowIconified(final WindowEvent e) {
    }

    @Override
    default void windowDeiconified(final WindowEvent e) {
    }

    @Override
    default void windowActivated(final WindowEvent e) {
    }

    @Override
    default void windowDeactivated(final WindowEvent e) {
    }
}
