package org.hzt.reflection.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectionUtilsTest {

    @Test
    void testEnclosingMethodName() {
        final String enclosingMethodName = ReflectionUtils.getEnclosingMethodName();
        assertEquals("testEnclosingMethodName", enclosingMethodName);
    }

}
