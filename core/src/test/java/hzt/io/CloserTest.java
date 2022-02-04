package hzt.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloserTest {

    @Test
    void testCloserForResourceNotImplementingAutoClosable() {
        var closer = Closer.forResource(new Resource("Resource 1"), Resource::close);
        try (closer) {
            assertFalse(closer.getResource().closed);
            closer.execute(Resource::load);
            final var result = closer.apply(Resource::read);
            assertEquals("Read result", result);
        }
        assertTrue(closer.getResource().closed);
    }

    @Test
    void testCloserCLosingFunctionThrowingException() {
        var closer = Closer.forResource(new Resource("Resource 1"), Resource::closeThrowingException);
        assertThrows(IllegalStateException.class, () -> closeThrowingException(closer));

    }

    private void closeThrowingException(Closer<Resource> closer) {
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

        public Resource(String name) {
            this.name = name;
        }


        public void load() throws IOException {
            System.out.println(name + " loading...");
            System.out.println(name + " loaded");
        }

        public String read() throws IOException {
            System.out.println(name + " reading...");
            System.out.println(name + " read");
            return "Read result";
        }

        public void close() {
            System.out.println(name + " is now closed");
            closed = true;
        }

        public void closeThrowingException() throws IOException {
            throw new IOException("Could not close " + name);
        }
    }

}
