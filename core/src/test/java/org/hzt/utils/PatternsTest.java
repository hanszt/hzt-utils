package org.hzt.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.regex.Pattern;

import static org.hzt.utils.Patterns.*;
import static org.junit.jupiter.api.Assertions.*;

class PatternsTest {

    public static final Pattern arrowPattern = Pattern.compile(" -> ");

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
        final boolean isValidMailAddress = emailPattern.matcher(mail).matches();
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
        final boolean isValidMailAddress = emailPattern.matcher(mail).matches();
        assertFalse(isValidMailAddress);
    }

}
