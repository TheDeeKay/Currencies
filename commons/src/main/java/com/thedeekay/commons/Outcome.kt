package com.thedeekay.commons

/**
 * Type representing a return value of an operation that can fail.
 * Similar to [Result], but this also holds generic type of failure.
 *
 * @param T Type of successful result held by this class.
 * @param E Type of unsuccessful result held by this class.
 */
sealed class Outcome<out T, out E> {

    data class Success<out T>(val result: T) : Outcome<T, Nothing>()

    data class Failure<out E>(val error: E) : Outcome<Nothing, E>()
}
