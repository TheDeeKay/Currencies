package com.thedeekay.rxtestutils

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.functions.Function
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.ExternalResource
import java.util.concurrent.Callable

/**
 * JUnit test rule for setting all schedulers to 'trampoline' while tests are running.
 */
class RxJavaSchedulersRule : ExternalResource() {
    private val trampolineScheduler = Function<Scheduler, Scheduler> { Schedulers.trampoline() }
    private val callableTrampolineScheduler = Function<Callable<Scheduler>, Scheduler> { Schedulers.trampoline() }

    private var oldIoSchedulerHandler: Function<in Scheduler, out Scheduler>? = null
    private var oldComputationHandler: Function<in Scheduler, out Scheduler>? = null
    private var oldNewThreadHandler: Function<in Scheduler, out Scheduler>? = null
    private var oldSingleThreadHandler: Function<in Scheduler, out Scheduler>? = null
    private var oldInitMainThreadHandler: Function<Callable<Scheduler>, Scheduler>? = null
    private var oldMainThreadHandler: Function<Scheduler, Scheduler>? = null

    override fun before() {
        oldIoSchedulerHandler = RxJavaPlugins.getIoSchedulerHandler()
        oldComputationHandler = RxJavaPlugins.getComputationSchedulerHandler()
        oldNewThreadHandler = RxJavaPlugins.getNewThreadSchedulerHandler()
        oldSingleThreadHandler = RxJavaPlugins.getSingleSchedulerHandler()
        oldMainThreadHandler = RxAndroidPlugins.getOnMainThreadSchedulerHandler()
        oldInitMainThreadHandler = RxAndroidPlugins.getInitMainThreadSchedulerHandler()

        RxJavaPlugins.setIoSchedulerHandler(trampolineScheduler)
        RxJavaPlugins.setComputationSchedulerHandler(trampolineScheduler)
        RxJavaPlugins.setNewThreadSchedulerHandler(trampolineScheduler)
        RxJavaPlugins.setSingleSchedulerHandler(trampolineScheduler)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(callableTrampolineScheduler)
        RxAndroidPlugins.setMainThreadSchedulerHandler(trampolineScheduler)
    }

    override fun after() {
        RxJavaPlugins.setIoSchedulerHandler(oldIoSchedulerHandler)
        RxJavaPlugins.setComputationSchedulerHandler(oldComputationHandler)
        RxJavaPlugins.setNewThreadSchedulerHandler(oldNewThreadHandler)
        RxJavaPlugins.setSingleSchedulerHandler(oldSingleThreadHandler)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(oldInitMainThreadHandler)
        RxAndroidPlugins.setMainThreadSchedulerHandler(oldMainThreadHandler)
    }
}
