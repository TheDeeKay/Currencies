package com.thedeekay.rxtestutils

import io.reactivex.subscribers.TestSubscriber


private fun <T> List<T>.hasSameElementsAs(
    other: List<T>
) = containsAll(other) && other.containsAll(this)

fun <T> TestSubscriber<List<T>>.assertValueHasSameElementsAs(other: List<T>): TestSubscriber<List<T>> {
    return assertValue { it.hasSameElementsAs(other) }
}
