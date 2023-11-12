package org.hzt


/**
 * A small extension function to make a when expression used as statement compile time exhaustive
 *
 * See [TIL: when is “when” exhaustive?](https://proandroiddev.com/til-when-is-when-exhaustive-31d69f630a8b)
 */
val <T> T.exhaustive: T get() = this
