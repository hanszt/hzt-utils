package org.hzt.utils.io;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CloserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloserTest.class);

    @Test
    void testCloserForResourceNotImplementingAutoClosable() {
        final var closer = Closer.forResource(new Resource("Resource 1"), Resource::close);
        try (closer) {
            assertFalse(closer.getResource().closed);
            closer.execute(Resource::load);
            final var result = closer.apply(Resource::read);
            assertEquals("Read result", result);
        }
        assertTrue(closer.getResource().closed);
    }

    @Test
    void testCloserForResourceApplyAndClose() {
        final var resource = new Resource("Resource 1");

        //noinspection resource
        final var result = Closer.forResource(resource, Resource::close).applyAndClose(Resource::read);

        assertAll(
                () -> assertEquals("Read result", result),
                () -> assertTrue(resource.closed)
        );
    }

    @Test
    void testExecuteAndClose() {
        final List<String> list = new ArrayList<>();

        final var resource = new Resource("Resource 1");

        //noinspection resource
        Closer.forResource(resource, Resource::close).executeAndClose(l -> list.add(l.read()));

        assertAll(
                () -> assertEquals("Read result", list.getFirst()),
                () -> assertTrue(resource.closed)
        );
    }

    @Test
    void testCloserCLosingFunctionThrowingException() {
        //noinspection resource
        final var closer = Closer.forResource(new Resource("Resource 1"), Resource::closeThrowingException);
        assertThrows(IllegalStateException.class, () -> closeThrowingException(closer));

    }

    private void closeThrowingException(final Closer<Resource> closer) {
        try (closer) {
            assertFalse(closer.getResource().closed);
            closer.execute(Resource::load);
            final var result = closer.apply(Resource::read);
            assertEquals("Read result", result);
        }
    }

    private static class Resource {

        private final String name;
        private boolean closed;

        public Resource(final String name) {
            this.name = name;
        }


        public void load() {
            LOGGER.atDebug().setMessage(() -> name + " loading...").log();
            LOGGER.atDebug().setMessage(() -> name + " loaded").log();
        }

        public String read() {
            LOGGER.atDebug().setMessage(() -> name + " reading...").log();
            LOGGER.atDebug().setMessage(() -> name + " read").log();
            return "Read result";
        }

        public void close() {
            LOGGER.atDebug().setMessage(() -> name + " is now closed").log();
            closed = true;
        }

        public void closeThrowingException() throws IOException {
            throw new IOException("Could not close " + name);
        }
    }

}
