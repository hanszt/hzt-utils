package org.hzt

import java.time.Month
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EnumUtilsKtTest {

    @Test
    fun `should return an empty enumSet`() = assertTrue(emptyEnumSet<Month>().isEmpty())
    @Test
    fun `should return an enumSet with all months`() = assertEquals(12, enumSetAllOf<Month>().size)
}
