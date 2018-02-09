package com.example.linma9.mytechcruncharticlelistapplication.DI.scope

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

/**
 * Created by linma9 on 2/9/18.
 */


/**
 * Scoping annotation used by dependency-injection framework to allow injected object instances to live
 * only as long as [view] is alive; Allows "local singletons" that
 * do not need to be held onto for the entire lifetime of the host [android.app.Application].
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class ViewScope