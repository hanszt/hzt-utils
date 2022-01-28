package hzt.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AutoCloserTest {

    @Test
    void testAutoCloser() {
        ResourceNotImplementingClosable resource1;
        ResourceNotImplementingClosable resource2;
        ResourceNotImplementingClosable resource3;
        try (AutoCloser autoCloser = new AutoCloser()) {
            resource1 = new ResourceNotImplementingClosable("Resource 1");
            resource2 = new ResourceNotImplementingClosable("Resource 2");
            resource3 = new ResourceNotImplementingClosable("Resource 3");
            autoCloser.addCloseFunctions(resource1::close, resource2::close, resource3::close);
            assertFalse(resource1.closed);
            assertFalse(resource2.closed);
            assertFalse(resource3.closed);
        }
        //noinspection ConstantConditions
        assertTrue(resource1.closed);
        assertTrue(resource2.closed);
        assertTrue(resource3.closed);
    }

    @Test
    void testOneOfCloseMethodsThrowingException() {
        ResourceNotImplementingClosable resource1 = new ResourceNotImplementingClosable("Resource 1");
        ResourceNotImplementingClosable resource2 = new ResourceNotImplementingClosable("Resource 2");
        ResourceNotImplementingClosable resource3 = new ResourceNotImplementingClosable("Resource 3");
        try (AutoCloser autoCloser = new AutoCloser()) {
            autoCloser.addCloseFunctions(resource1::close, resource2::closeThrowingException, resource3::close);

        } catch (IllegalStateException e) {
            assertEquals("Could not close all", e.getMessage());
            e.printStackTrace();
        }
        assertTrue(resource1.closed);
        assertFalse(resource2.closed);
        assertTrue(resource3.closed);
    }

    private static class ResourceNotImplementingClosable {

        private final String name;
        private boolean closed;

        public ResourceNotImplementingClosable(String name) {
            this.name = name;
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
