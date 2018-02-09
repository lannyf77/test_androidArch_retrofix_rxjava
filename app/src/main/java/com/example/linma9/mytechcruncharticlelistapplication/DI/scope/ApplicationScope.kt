package com.example.linma9.mytechcruncharticlelistapplication.DI.scope

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

/**
 * Created by linma9 on 2/9/18.
 */


/**
 * Scoping annotation used by dependency-injection framework to allow injected object instances to live
 * only as long as [Application] is alive;
 * it is same as @Singleton scopewise here, just for testing
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class ApplicationScope