package com.example.linma9.mytechcruncharticlelistapplication.DI.module

import android.app.Application
import com.example.linma9.mytechcruncharticlelistapplication.DI.scope.ApplicationScope
import com.example.linma9.mytechcruncharticlelistapplication.database.DataManager
import dagger.Module
import dagger.Provides

/**
 * Created by linma9 on 2/9/18.
 */

@Module
class DataManagerModule(val application: Application) {

    @Provides
    @ApplicationScope

    fun dataManager() : DataManager {
        return DataManager(application)
    }
}