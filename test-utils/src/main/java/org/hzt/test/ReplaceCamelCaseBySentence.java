package org.hzt.test;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

/**
 * @see <a href="https://www.baeldung.com/junit-custom-display-name-generator">JUnit Custom Display Name Generator API</a>
 */
public class ReplaceCamelCaseBySentence extends DisplayNameGenerator.Standard {
    @Override
    public String generateDisplayNameForClass(final Class<?> testClass) {
        return replaceCamelCaseBySentence(super.generateDisplayNameForClass(testClass));
    }

    @Override
    public String generateDisplayNameForNestedClass(final Class<?> nestedClass) {
        return replaceCamelCaseBySentence(super.generateDisplayNameForNestedClass(nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(final Class<?> testClass, final Method testMethod) {
        return replaceCamelCaseBySentence(testMethod.getName()) + parameterTypesAsString(testMethod);
    }

    static String parameterTypesAsString(final Method method) {
        Objects.requireNonNull(method, "Method must not be null");
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 0) {
            return '(' + nullSafeToString(Class::getSimpleName, parameterTypes) + ')';
        }
        return "";
    }

    public static String nullSafeToString(final Function<? super Class<?>, String> mapper, final Class<?>... classes) {
        Objects.requireNonNull(mapper, "Mapping function must not be null");
        if (classes == null || classes.length == 0) {
            return "";
        }
        return stream(classes)
                .map(clazz -> clazz == null ? "null" : mapper.apply(clazz))
                .collect(joining(", "));
    }

    static String replaceCamelCaseBySentence(final String camelCase) {
        final StringBuilder result = new StringBuilder();
        result.append(Character.toUpperCase(camelCase.charAt(0)));
        for (int i = 1; i < camelCase.length(); i++) {
            final char curChar = camelCase.charAt(i);
            if (Character.isUpperCase(curChar)) {
                result.append(' ').append(Character.toLowerCase(curChar));
            } else {
                result.append(curChar);
            }
        }
        return result.toString();
    }
}
