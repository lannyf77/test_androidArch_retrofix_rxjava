package com.example.linma9.mytechcruncharticlelistapplication.DI.componenet

import com.example.linma9.mytechcruncharticlelistapplication.DI.module.AppModule
import com.example.linma9.mytechcruncharticlelistapplication.DI.module.DataManagerModule
import com.example.linma9.mytechcruncharticlelistapplication.DI.module.NetworkModule
import com.example.linma9.mytechcruncharticlelistapplication.DI.module.PresentorModule
import com.example.linma9.mytechcruncharticlelistapplication.MyApp
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.DataRepository
import com.example.linma9.mytechcruncharticlelistapplication.model.service.NetworkUtils
import com.example.linma9.mytechcruncharticlelistapplication.presentor.Presentor
import dagger.Component
import javax.inject.Singleton

/**
 * Created by linma9 on 2/8/18.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class))
interface AppComponent {

    fun addChildModle(dataMgrModule: DataManagerModule): DataMgrComponent
    fun addChildModle(presentorModule: PresentorModule): PresentorComponent

    fun inject(application: MyApp)

    fun getDataRepository(): DataRepository
    fun getNetworkUtils(): NetworkUtils
}