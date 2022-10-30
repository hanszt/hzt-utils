package org.hzt.test;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class TestSampleGeneratorTest {

    @Test
    void testGetEnglishNameList() {
        final List<String> englishNameList = TestSampleGenerator.getEnglishNameList();

        assertFalse(englishNameList.isEmpty());
    }

}
