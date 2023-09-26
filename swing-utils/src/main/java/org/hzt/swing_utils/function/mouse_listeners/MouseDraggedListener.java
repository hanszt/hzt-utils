package org.hzt.swing_utils.function.mouse_listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public interface MouseDraggedListener extends MouseMotionListener {

    @Override
    default void mouseMoved(final MouseEvent e) {
    }
}
