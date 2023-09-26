package org.hzt.swing_utils.function.window_listeners;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WindowClosedListenerTest {

    @Test
    void testWindowClosedListener() {
        final var thread = new Thread(this::testWindowClosed);
        thread.start();
        await().until(() -> !thread.isAlive());
    }

    private void testWindowClosed() {
        final var isCalled = new AtomicBoolean(false);
        final Window frame = new Frame();
        frame.addWindowListener((WindowClosedListener) e -> isCalled.set(true));
        frame.setVisible(true);
        frame.setVisible(false);

        System.out.println("jFrame.isActive() = " + frame.isActive());

        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());

        frame.dispose();

        await().until(() -> !frame.isDisplayable());

       assertTrue(isCalled.get());
    }

}
