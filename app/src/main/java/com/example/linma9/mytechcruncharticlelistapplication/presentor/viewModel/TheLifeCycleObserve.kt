package com.example.linma9.mytechcruncharticlelistapplication.presentor.viewModel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleRegistry


/**
 * Created by linma9 on 1/23/18.
 */

class TheLifeCycleObserve(private var lifecycle: LifecycleRegistry?, private var lifeCycleChangeListener: OnLifeCycleChange) : LifecycleObserver {


    interface OnLifeCycleChange {
        fun onCreate()
        fun onStar()
        fun onStop()
        fun onDestroy()
    }

    init {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        lifeCycleChangeListener.onCreate()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        lifeCycleChangeListener.onStar()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        lifeCycleChangeListener.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        lifeCycleChangeListener.onDestroy()
    }
}