package com.example.linma9.mytechcruncharticlelistapplication.DI

import com.example.linma9.mytechcruncharticlelistapplication.MyApp
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.DataRepository
import com.example.linma9.mytechcruncharticlelistapplication.presentor.Presentor
import dagger.Component
import javax.inject.Singleton

/**
 * Created by linma9 on 2/8/18.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(application: MyApp)

    fun getPresenter(): Presentor
    fun getDataRepository(): DataRepository
}