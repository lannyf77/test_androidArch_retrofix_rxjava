package com.example.linma9.mytechcruncharticlelistapplication.model.service.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by linma9 on 1/23/18.
 */
class ServiceFactory {

    companion object {

        /**
         * Creates a retrofit service from an arbitrary class (clazz)
         * @param clazz Java interface of the retrofit service
         * @param endPoint REST endpoint url
         * @return retrofit service with defined endpoint
         */
        fun <T> createRetrofitService(clazz: Class<T>, endPoint: String): T {

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClientBuilder = OkHttpClient.Builder().addInterceptor(logging)
            val client = httpClientBuilder.build()


            val restAdapter = Retrofit.Builder()
                    .baseUrl(endPoint)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //for RxJava2
                    //.addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //<== for using RxJava which is other than Call
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(client)  //for log raw response
                    .build()

            return restAdapter.create(clazz)
        }
    }
}