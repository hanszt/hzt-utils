package org.hzt.test;

import java.lang.reflect.Method;

/**
 * @see <a href="https://www.baeldung.com/junit-custom-display-name-generator">JUnit Custom Display Name Generator API</a>
 */
public class IndicativeSentences extends ReplaceCamelCaseBySentence {

    @Override
    public String generateDisplayNameForNestedClass(final Class<?> nestedClass) {
        return super.generateDisplayNameForNestedClass(nestedClass) + "...";
    }

    @Override
    public String generateDisplayNameForMethod(final Class<?> testClass, final Method testMethod) {
        return replaceCamelCaseBySentence(testClass.getSimpleName() + " " + testMethod.getName()) + ".";
    }
}
