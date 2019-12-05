package com.thedeekay.commons

fun <T> List<T>.hasSameElementsAs(
    other: List<T>
) = containsAll(other) && other.containsAll(this)
