package org.hzt.kotlin

import java.util.*

inline fun <reified T : Enum<T>> emptyEnumSet(): EnumSet<T> = EnumSet.noneOf(T::class.java)
inline fun <reified T : Enum<T>> enumSetAllOf(): EnumSet<T> = EnumSet.allOf(T::class.java)
