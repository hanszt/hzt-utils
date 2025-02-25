package org.hzt.test.assertions;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hzt.test.assertions.Assertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssertionsTest {

    @Test
    @Disabled("Not working properly yet")
    void testAssertSoftly() throws Throwable {
//        assertAll(
//                () -> assertEquals(3, 4),
//                () -> assertEquals(3, 5)
//        );
        assertSoftly(() -> {
            assertEquals(3, 4);
            assertEquals(3, 5);
            assertEquals(3, 6);
        });
    }

}