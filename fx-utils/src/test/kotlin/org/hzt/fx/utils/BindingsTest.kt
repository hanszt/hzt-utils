package org.hzt.fx.utils

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BindingsTest {

    @Test
    fun testBooleanBinding() {
        val isHallo = SimpleBooleanProperty()
        val text = SimpleObjectProperty("Hoi")

        isHallo.bind(text.booleanBinding { it == "Hallo" })

        assertFalse(isHallo.get())
        text.set("Hallo")
        assertTrue(isHallo.get())
    }

    @Test
    fun testIntBinding() {
        val textLength = SimpleIntegerProperty()
        val text = SimpleObjectProperty("Hoi")

        textLength.bind(text.intBinding(String::length))

        assertEquals(3, textLength.get())
        text.set("Hallo")
        assertEquals(5, textLength.get())
    }


}