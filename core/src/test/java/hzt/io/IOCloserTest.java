package hzt.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IOCloserTest {

    @Test
    void testAutoCloser() throws IOException {
        final ResourceNotImplementingClosable resource1 = new ResourceNotImplementingClosable("Resource 1");
        final ResourceNotImplementingClosable resource2 = new ResourceNotImplementingClosable("Resource 2");
        final ResourceNotImplementingClosable resource3 = new ResourceNotImplementingClosable("Resource 3");
        //noinspection unused
        try (IOCloser ioCloser = new IOCloser(resource1::close, resource2::close, resource3::close)) {
            assertAll(
                    () -> assertFalse(resource1.closed),
                    () -> assertFalse(resource2.closed),
                    () -> assertFalse(resource3.closed)
            );
        }
        assertAll(
                () -> assertTrue(resource1.closed),
                () -> assertTrue(resource2.closed),
                () -> assertTrue(resource3.closed)
        );
    }

    @Test
    void testCloser() {
        //noinspection Convert2MethodRef

        try (Closer<ResourceNotImplementingClosable> closer = Closer
                .forResource(new ResourceNotImplementingClosable("Resource 1"), resource -> resource.close())) {
            assertFalse(closer.getResource().closed);
        }
    }

    @Test
    void testOneOfCloseMethodsThrowingException() {
        final ResourceNotImplementingClosable resource1 = new ResourceNotImplementingClosable("Resource 1");
        final ResourceNotImplementingClosable resource2 = new ResourceNotImplementingClosable("Resource 2");
        final ResourceNotImplementingClosable resource3 = new ResourceNotImplementingClosable("Resource 3");
        try (IOCloser ioCloser = new IOCloser()) {
            resource1.load();
            ioCloser.addCloseFunctions(resource1::close, resource2::closeThrowingException, resource3::close);

        } catch (IOException e) {
            assertEquals("Could not close all", e.getMessage());
            e.printStackTrace();
        }
        assertAll(
                () -> assertTrue(resource1.closed),
                () -> assertFalse(resource2.closed),
                () -> assertTrue(resource3.closed)
        );
    }

    private static class ResourceNotImplementingClosable {

        private final String name;
        private boolean closed;

        public ResourceNotImplementingClosable(String name) {
            this.name = name;
        }


        public void load() throws IOException {
            System.out.println(name + " loading...");
            System.out.println(name + " loaded");
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
