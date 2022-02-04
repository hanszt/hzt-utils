package hzt.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriFunctionTest {

    @Test
    void testAndThen() {
        final var function = TriFunction
                .<String, String, String, Integer>of((s1, s2, s3) -> s1.length() + s2.length() + s3.length())
                .andThen(String::valueOf);

        final var result = function.apply("Hi", "what", "fun");
        assertEquals("9", result);
    }
}
