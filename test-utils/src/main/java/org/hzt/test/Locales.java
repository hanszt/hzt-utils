package org.hzt.test;

import java.util.Locale;
import java.util.function.Consumer;

public final class Locales {

    private Locales() {
    }

    /**
     * Sets the locale to a specific instance during the fixedLocaleScope execution. After executing the fixedLocaleScope,
     * The locale is set back to what the default setting was
     * <p>
     * This method is synchronized and therefore thread-safe
     *
     * @param locale           the locale to be set as default during the fixedLocaleScope
     * @param fixedLocaleScope the function during which the locale is fixed to the given locale
     */
    public static synchronized void testWithFixedLocale(final Locale locale, final Consumer<Locale> fixedLocaleScope) {
        final var defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(locale);
            fixedLocaleScope.accept(locale);
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }
}
