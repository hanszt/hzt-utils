package org.hzt.test;

import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.platform.commons.util.ClassUtils;
import org.junit.platform.commons.util.Preconditions;

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
        return replaceCamelCaseBySentence(testMethod.getName()) + parameterTypesAsString(testMethod);
    }

    static String parameterTypesAsString(Method method) {
        Preconditions.notNull(method, "Method must not be null");
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 0) {
            return '(' + ClassUtils.nullSafeToString(Class::getSimpleName, parameterTypes) + ')';
        }
        return "";
    }

    static String replaceCamelCaseBySentence(String camelCase) {
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
