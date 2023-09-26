package org.hzt

import java.util.EnumSet

inline fun <reified T : Enum<T>> emptyEnumSet(): EnumSet<T> = EnumSet.noneOf(T::class.java)
inline fun <reified T : Enum<T>> enumSetAllOf(): EnumSet<T> = EnumSet.allOf(T::class.java)
