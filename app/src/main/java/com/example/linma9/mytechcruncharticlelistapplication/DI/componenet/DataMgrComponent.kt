package com.example.linma9.mytechcruncharticlelistapplication.DI.componenet

import com.example.linma9.mytechcruncharticlelistapplication.DI.module.DataManagerModule
import dagger.Subcomponent

import com.example.linma9.mytechcruncharticlelistapplication.DI.scope.ApplicationScope
import com.example.linma9.mytechcruncharticlelistapplication.database.DataManager


/**
 * Created by linma9 on 2/9/18.
 */

/**
 * Defines object graph used to provide dependencies for dataManager.
 * These dependencies are scoped within the {@link DataManagerModule} scope.
 */

@ApplicationScope
@Subcomponent(modules = arrayOf(DataManagerModule::class))
interface DataMgrComponent {

    fun getDataManager() : DataManager
}