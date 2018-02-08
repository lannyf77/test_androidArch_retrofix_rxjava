package com.example.linma9.mytechcruncharticlelistapplication.DI

import android.app.Application
import android.content.Context
import android.location.LocationManager
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.DataRepository
import com.example.linma9.mytechcruncharticlelistapplication.presentor.Presentor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by linma9 on 2/8/18.
 */

@Module
class AppModule (private val app: Application){
    @Provides
    @Singleton
    fun provideAppContext(): Context = app

    @Provides
    @Singleton
    fun presentor() : Presentor = Presentor.instance

    @Provides
    @Singleton
    fun dataRepository() : DataRepository = DataRepository.instance

    @Provides
    @Singleton
    fun provideLocationManager(): LocationManager =
            app.getSystemService(Context.LOCATION_SERVICE) as LocationManager


}