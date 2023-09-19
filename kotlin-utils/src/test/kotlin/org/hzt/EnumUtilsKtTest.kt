package org.hzt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Month

class EnumUtilsKtTest {

    @Test
    fun `should return an empty enumSet`() = assertTrue(emptyEnumSet<Month>().isEmpty())
    @Test
    fun `should return an enumSet with all months`() = assertEquals(12, enumSetAllOf<Month>().size)
}
