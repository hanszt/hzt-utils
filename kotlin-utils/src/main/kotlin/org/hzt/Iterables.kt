package org.hzt

fun <T, R> Iterable<Array<T>>.flatMapArray(mapper: (T) -> R): List<R> = flatMap { it.map(mapper) }
fun <T, R> Array<Array<T>>.flatMapArray(mapper: (T) -> R): List<R> = flatMap { it.map(mapper) }
