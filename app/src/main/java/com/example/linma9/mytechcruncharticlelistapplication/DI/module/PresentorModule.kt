package com.example.linma9.mytechcruncharticlelistapplication.DI.module

import android.app.Application
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.linma9.mytechcruncharticlelistapplication.DI.scope.ViewScope
import com.example.linma9.mytechcruncharticlelistapplication.InfiniteScrollListener
import com.example.linma9.mytechcruncharticlelistapplication.database.DataManager
import com.example.linma9.mytechcruncharticlelistapplication.presentor.Presentor
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by linma9 on 2/9/18.
 */

@Module
class PresentorModule() {

    @Provides
    @ViewScope

    fun presentor() : Presentor {
        return Presentor()  // if want to keep returning same instance, then could use Presntor.instance
    }
}