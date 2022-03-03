package org.hzt.swing_utils.function.window_listeners;

import org.junit.jupiter.api.Test;

import java.awt.Frame;
import java.awt.Window;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WindowActivatedListenerTest {

    @Test
    void testWindowActivatedListener() {
        final var thread = new Thread(this::testWindowActivated);
        thread.start();
        await().until(() -> !thread.isAlive());
    }

    private void testWindowActivated() {
        AtomicBoolean isCalled = new AtomicBoolean(false);
        Window frame = new Frame();
        frame.addWindowListener((WindowActivatedListener) e -> isCalled.set(true));
        frame.setVisible(true);

        await().until(isCalled::get);

        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());

        frame.dispose();

        await().until(() -> !frame.isDisplayable());

        assertTrue(isCalled.get());
    }
}
