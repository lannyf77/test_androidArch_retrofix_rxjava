package com.example.linma9.mytechcruncharticlelistapplication

import android.app.Application
import android.location.LocationManager
import android.util.Log
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
        //allow access it from app
        @JvmStatic lateinit var graph: AppComponent
    }

    @Inject
    lateinit var locationManager: LocationManager

    override fun onCreate() {
        super.onCreate()
        graph = initDagger()
        graph.inject(this)

        Log.d("MyApp", "App test the : $locationManager instance")
        //TODO do some other cool stuff here
    }

    private fun initDagger() : AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}