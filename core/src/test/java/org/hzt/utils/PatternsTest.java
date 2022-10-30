package org.hzt.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hzt.utils.Patterns.emailPattern;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatternsTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "ingridtest@hotmail.com",
            "johndwo@planet.live",
            "username@domain.com",
            "username@domain.com.nl",
            "user.name@domain.com",
            "user_name@domain.com"})
    @DisplayName("Test valid email addresses")
    void testValidEmailAddresses(String mail) {
        final var isValidMailAddress = emailPattern.matcher(mail).matches();
        assertTrue(isValidMailAddress);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ingridtest@hotmail#.com",
            "joh#ndwo@planet.live",
            "username_domain.com",
            "username_@domai@test.com",
            "username|test@domain.com"})
    @DisplayName("Test invalid email addresses")
    void testInValidEmailAddresses(String mail) {
        final var isValidMailAddress = emailPattern.matcher(mail).matches();
        assertFalse(isValidMailAddress);
    }

}
