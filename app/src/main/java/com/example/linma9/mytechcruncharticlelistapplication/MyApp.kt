package com.example.linma9.mytechcruncharticlelistapplication

import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.util.Log
import com.example.linma9.mytechcruncharticlelistapplication.DI.componenet.AppComponent
import com.example.linma9.mytechcruncharticlelistapplication.DI.componenet.DaggerAppComponent
import com.example.linma9.mytechcruncharticlelistapplication.DI.componenet.DataMgrComponent
import com.example.linma9.mytechcruncharticlelistapplication.DI.componenet.PresentorComponent
import com.example.linma9.mytechcruncharticlelistapplication.DI.module.AppModule
import com.example.linma9.mytechcruncharticlelistapplication.DI.module.DataManagerModule
import com.example.linma9.mytechcruncharticlelistapplication.DI.module.PresentorModule
import com.example.linma9.mytechcruncharticlelistapplication.presentor.Presentor
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import timber.log.Timber

import javax.inject.Inject

/**
 * Created by linma9 on 2/8/18.
 */
class MyApp : Application() {

    companion object {
        //allow access it from app
        @JvmStatic lateinit var graph: AppComponent
        @JvmStatic lateinit var dataMgrComponenet: DataMgrComponent
        //@JvmStatic lateinit var presentorComponenet: PresentorComponent

        fun watchRefOfThisContext(context: Context?, watch: Any) {
            if (BuildConfig.DEBUG) {
                if (context != null) {
                    val refWatcher = getRefWatcher(context)
                    refWatcher?.watch(watch)
                }
            }
        }

        private fun getRefWatcher(context: Context?): RefWatcher? {
            val application = context!!.applicationContext as MyApp
            return application.mReferenceWatcher
        }
    }

    @Inject
    lateinit var locationManager: LocationManager

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        graph = initDagger()
        graph.inject(this)

        dataMgrComponenet = graph.addChildModle(DataManagerModule(this))

        //presentorComponenet = graph.addChildModle(PresentorModule())

        installLeakCanary()

        Stetho.initializeWithDefaults(this)

        Log.e("MyApp", "+++ +++++++++++++++++++++++ App test the : $locationManager instance")
        //TODO do some other cool stuff here
    }

    private fun initDagger() : AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    /*
     * LeakCanary Helpers
     * Blog - https://medium.com/square-corner-blog/leakcanary-detect-all-memory-leaks-875ff8360745#.kp1z2wfxv
     * Customization - https://github.com/square/leakcanary/wiki/Customizing-LeakCanary
     * FAQ - https://github.com/square/leakcanary/wiki/FAQ#how-do-i-fix-a-memory-leak
     */

    private lateinit var mReferenceWatcher : RefWatcher ;
    private fun installLeakCanary() {
        if (BuildConfig.DEBUG) {
            if (!LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                mReferenceWatcher = LeakCanary.install(this)
            }
        } else {
            disableLeakCanary()
        }
    }

    private fun disableLeakCanary() {
        //Disabled the leakcanary for Productuion - Safer side.
        mReferenceWatcher = RefWatcher.DISABLED
    }
}