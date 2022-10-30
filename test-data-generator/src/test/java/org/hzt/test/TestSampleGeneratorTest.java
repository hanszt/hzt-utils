package org.hzt.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class TestSampleGeneratorTest {

    @Test
    void testGetEnglishNameList() {
        final var englishNameList = TestSampleGenerator.getEnglishNameList();

        assertFalse(englishNameList.isEmpty());
    }

}
