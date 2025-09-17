package org.hzt.fx.utils

import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.IntegerBinding
import javafx.beans.property.ObjectProperty

fun <T> ObjectProperty<T>.booleanBinding(predicate: (T) -> Boolean): BooleanBinding = Bindings
    .createBooleanBinding({ predicate(get()) }, this)

fun <T> ObjectProperty<T>.intBinding(toInt: (T) -> Int): IntegerBinding = Bindings
    .createIntegerBinding({ toInt(get()) }, this)