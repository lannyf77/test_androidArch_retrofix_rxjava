package com.example.linma9.mytechcruncharticlelistapplication.DI.module

import android.app.Application
import com.example.linma9.mytechcruncharticlelistapplication.DI.scope.ViewScope
import com.example.linma9.mytechcruncharticlelistapplication.database.DataManager
import com.example.linma9.mytechcruncharticlelistapplication.presentor.Presentor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by linma9 on 2/9/18.
 */

@Module
class PresentorModule() {

    @Provides
    @ViewScope

    fun presentor() : Presentor = Presentor.instance
}