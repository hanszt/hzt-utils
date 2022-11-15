package org.hzt.test;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

/**
 * @see <a href="https://www.baeldung.com/junit-custom-display-name-generator">JUnit Custom Display Name Generator API</a>
 */
public class ReplaceCamelCaseBySentence extends DisplayNameGenerator.Standard {
    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return replaceCamelCaseBySentence(super.generateDisplayNameForClass(testClass));
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return replaceCamelCaseBySentence(super.generateDisplayNameForNestedClass(nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return replaceCamelCaseBySentence(testMethod.getName()) +
                DisplayNameGenerator.parameterTypesAsString(testMethod);
    }

    static String replaceCamelCaseBySentence(String camelCase) {
        final var result = new StringBuilder();
        result.append(Character.toUpperCase(camelCase.charAt(0)));
        for (var i = 1; i < camelCase.length(); i++) {
            final var curChar = camelCase.charAt(i);
            if (Character.isUpperCase(curChar)) {
                result.append(' ').append(Character.toLowerCase(curChar));
            } else {
                result.append(curChar);
            }
        }
        return result.toString();
    }
}
