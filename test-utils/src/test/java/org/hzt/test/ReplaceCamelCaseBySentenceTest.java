package org.hzt.test;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class ReplaceCamelCaseBySentenceTest {

    private final ReplaceCamelCaseBySentence replaceCamelCaseBySentence = new ReplaceCamelCaseBySentence();

    @Test
    void testReplaceCamelCaseClassNameBySentence() {
        final String name = new ReplaceCamelCaseBySentence().generateDisplayNameForClass(ReplaceCamelCaseBySentenceTest.class);

        assertEquals("Replace camel case by sentence test", name);
    }

    @Test
    void testReplaceCamelCaseMethodNameBySentence() {
        final Object classInMethod = new Object() {
        };
        final Method methodName = classInMethod
                .getClass()
                .getEnclosingMethod();

        final String name = replaceCamelCaseBySentence.generateDisplayNameForMethod(ReplaceCamelCaseBySentenceTest.class, methodName);

        assertEquals("Test replace camel case method name by sentence()", name);
    }

   @Nested
    class NestedClassTest {

        @Test
        void testReplaceCamelCaseNestedClassBySentence() {
            final String name = replaceCamelCaseBySentence.generateDisplayNameForNestedClass(NestedClassTest.class);

            assertEquals("Nested class test", name);
        }
    }

}
