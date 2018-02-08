package com.example.linma9.mytechcruncharticlelistapplication

import android.app.Application
import android.location.LocationManager
import com.example.linma9.mytechcruncharticlelistapplication.DI.AppComponent
import com.example.linma9.mytechcruncharticlelistapplication.DI.AppModule
import com.example.linma9.mytechcruncharticlelistapplication.DI.DaggerAppComponent
import com.example.linma9.mytechcruncharticlelistapplication.presentor.Presentor
import javax.inject.Inject

/**
 * Created by linma9 on 2/8/18.
 */
class MyApp : Application() {

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var graph: AppComponent
    }

    @Inject
    lateinit var locationManager: LocationManager

//    @Inject
//    lateinit var presentor: Presentor

    override fun onCreate() {
        super.onCreate()
        graph = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        graph.inject(this)

        println("App: $locationManager")
        //TODO do some other cool stuff here
    }
}