package com.example.linma9.mytechcruncharticlelistapplication.DI.componenet

import com.example.linma9.mytechcruncharticlelistapplication.DI.module.PresentorModule
import com.example.linma9.mytechcruncharticlelistapplication.DI.scope.ViewScope
import com.example.linma9.mytechcruncharticlelistapplication.presentor.Presentor
import dagger.Subcomponent

/**
 * Created by linma9 on 2/9/18.
 */

/**
 * Defines object graph used to provide dependencies for dataManager.
 * These dependencies are scoped within the {@link PresentorModule} scope.
 */

@ViewScope
@Subcomponent(modules = arrayOf(PresentorModule::class))
interface PresentorComponent {

    fun inject (presentor: Presentor)

    fun getPresenter(): Presentor

}
