package com.thedeekay.rxtestutils

import com.thedeekay.commons.hasSameElementsAs
import io.reactivex.subscribers.TestSubscriber

fun <T> TestSubscriber<List<T>>.assertValueHasSameElementsAs(other: List<T>): TestSubscriber<List<T>> {
    return assertValue { it.hasSameElementsAs(other) }
}

fun <T> TestSubscriber<List<T>>.assertValuesHaveSameElementsAs(vararg other: List<T>): TestSubscriber<List<T>> {
    return other.foldRightIndexed(initial = assertValueCount(other.size)) { index, list, acc ->
        acc.assertValueAt(index) { it.hasSameElementsAs(list) }
    }
}

fun <T> TestSubscriber<T>.assertLatestValue(value: T): TestSubscriber<T> {
    return assertValueAt(valueCount() - 1, value)
}
