package com.example.linma9.mytechcruncharticlelistapplication.DI

import com.example.linma9.mytechcruncharticlelistapplication.model.service.NetworkUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by linma9 on 2/8/18.
 */

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun networkUtils() : NetworkUtils = NetworkUtils.instance
}