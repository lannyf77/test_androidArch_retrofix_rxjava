package com.example.linma9.mytechcruncharticlelistapplication.DI.module

import android.app.Application
import android.content.Context
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
class PresentorModule {

    var mContext: Context
    var mPresentor: Presentor

    constructor(context: Context) {
        mContext = context
        mPresentor = Presentor()
    }
    @Provides
    @ViewScope
    internal fun context(): Context {// not sure if it has to implement to provide this mContext
        return mContext
    }

    @Provides
    @ViewScope

    fun presentor() : Presentor {
        return mPresentor
    }

    @Provides
    @ViewScope

    fun linearLayoutManager() : LinearLayoutManager {
        return LinearLayoutManager(mContext)
    }

    @Provides
    @ViewScope
    fun infiniteScrollListener() : InfiniteScrollListener {
        return InfiniteScrollListener(
                {
                    //  default handler
                    presentor().pullDataFromRemoteServer()
                },
                linearLayoutManager())
    }

    // really would like to have the flexibility of letting the consumer provides the
    // handling function to the InfiniteScrollListener instance,
    // but don’t know how to do it, so have to hard code the handling function
    // in the provider of InfiniteScrollListener listed above

    //    @Provides
    //    @ViewScope
    //    fun infiniteScrollListener(func: () -> Unit, layoutManager: LinearLayoutManager) :     InfiniteScrollListener {
    //        return InfiniteScrollListener(func, layoutManager)
    //    }
}