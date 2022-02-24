package hzt.io;

import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloserTest {

    @Test
    void testCloserForResourceNotImplementingAutoClosable() {
        try (Closer<Resource> closer = Closer.forResource(new Resource("Resource 1"), Resource::close)) {
            assertFalse(closer.getResource().closed);
            closer.execute(Resource::load);
            final String result = closer.apply(Resource::read);
            assertEquals("Read result", result);
        }
    }

    @Test
    void testCloserForResourceApplyAndClose() {
        final Resource resource = new Resource("Resource 1");

        final String result = Closer.forResource(resource, Resource::close).applyAndClose(Resource::read);

        assertAll(
                () -> assertEquals("Read result", result),
                () -> assertTrue(resource.closed)
        );
    }

    @Test
    void testExecuteAndClose() {
        List<String> list = new ArrayList<>();

        final Resource resource = new Resource("Resource 1");

        Closer.forResource(resource, Resource::close).executeAndClose(l -> list.add(l.read()));

        assertAll(
                () -> assertEquals("Read result", list.get(0)),
                () -> assertTrue(resource.closed)
        );
    }

    @Test
    void testCloserCLosingFunctionThrowingException() {
        assertThrows(IllegalStateException.class, this::closeThrowingException);

    }

    private void closeThrowingException() {
        try (Closer<Resource> closer = Closer.forResource(new Resource("Resource 1"), Resource::closeThrowingException)) {
            assertFalse(closer.getResource().closed);
            closer.execute(Resource::load);
            final String result = closer.apply(Resource::read);
            assertEquals("Read result", result);
        }
    }

    private static class Resource {

        private final String name;
        private boolean closed;

        public Resource(String name) {
            this.name = name;
        }


        public void load() {
            It.println(name + " loading...");
            It.println(name + " loaded");
        }

        public String read() {
            It.println(name + " reading...");
            It.println(name + " read");
            return "Read result";
        }

        public void close() {
            It.println(name + " is now closed");
            closed = true;
        }

        public void closeThrowingException() throws IOException {
            throw new IOException("Could not close " + name);
        }
    }

}
