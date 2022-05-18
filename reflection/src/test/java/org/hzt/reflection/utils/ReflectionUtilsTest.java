package org.hzt.reflection.utils;

import org.hzt.reflection.utils.ReflectionUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionUtilsTest {

    @Test
    void testEnclosingMethodName() {
        final var enclosingMethodName = ReflectionUtils.getEnclosingMethodName();
        assertEquals("testEnclosingMethodName", enclosingMethodName);
    }

}
