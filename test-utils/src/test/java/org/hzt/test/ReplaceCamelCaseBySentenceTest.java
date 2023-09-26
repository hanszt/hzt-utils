package org.hzt.test;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class ReplaceCamelCaseBySentenceTest {

    private final ReplaceCamelCaseBySentence replaceCamelCaseBySentence = new ReplaceCamelCaseBySentence();

    @Test
    void testReplaceCamelCaseClassNameBySentence() {
        final var name = new ReplaceCamelCaseBySentence().generateDisplayNameForClass(ReplaceCamelCaseBySentenceTest.class);

        assertEquals("Replace camel case by sentence test", name);
    }

    @Test
    void testReplaceCamelCaseMethodNameBySentence() {
        final var classInMethod = new Object() {
        };
        final var methodName = classInMethod
                .getClass()
                .getEnclosingMethod();

        final var name = replaceCamelCaseBySentence.generateDisplayNameForMethod(ReplaceCamelCaseBySentenceTest.class, methodName);

        assertEquals("Test replace camel case method name by sentence", name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"string 1", "string 2"})
    void testReplaceCamelCaseMethodNameBySentence(@SuppressWarnings("unused") final String s) {
        final var classInMethod = new Object() {
        };
        final var methodName = classInMethod
                .getClass()
                .getEnclosingMethod();

        final var name = replaceCamelCaseBySentence.generateDisplayNameForMethod(ReplaceCamelCaseBySentenceTest.class, methodName);

        assertEquals("Test replace camel case method name by sentence(String)", name);
    }

   @Nested
    class NestedClassTest {

        @Test
        void testReplaceCamelCaseNestedClassBySentence() {
            final var name = replaceCamelCaseBySentence.generateDisplayNameForNestedClass(NestedClassTest.class);

            assertEquals("Nested class test", name);
        }
    }

}
