package com.example.linma9.mytechcruncharticlelistapplication.model.service

import android.util.Log
import com.example.linma9.mytechcruncharticlelistapplication.model.data.Posts
import com.example.linma9.mytechcruncharticlelistapplication.model.service.remote.ServiceFactory
import com.example.linma9.mytechcruncharticlelistapplication.model.service.remote.WCTCPostsService
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by linma9 on 1/23/18.
 */

/**
 * utility for making network calls
 */

class NetworkUtils {

    internal var offset = 0
    @Throws(Exception::class)
    fun getPostsFromWorldPressTC(callback: Callback): Call {

        val usrStr = TC_WC_URL + "&offset=" + (offset)
        //Log.w("eee888-testWCGson", "+++ +++ 111 $offset, usrStr:" + usrStr)

        offset += postNumber

        val request = Request.Builder()
                .url(usrStr)
                .build()

        val call = client.newCall(request)
        call.enqueue(callback)

        //Log.w("eee888-testWCGson", "+++ +++ $offset, call.enqueue(callback);, usrStr:" + usrStr)

        return call

        //Response response = client.newCall(request).execute();

    }

    fun init() {
        offset = 0
    }

    companion object {
        internal var client = OkHttpClient()

        internal var _this: NetworkUtils? = null
        val instance: NetworkUtils
            get() {
                //Log.w("eee888-testWCGson", "+++ +++ NetworkUtils:instance(), _this==null: ${_this==null}, _this.offset: ${_this?.offset ?: "null _this"}")
                if (_this == null) {
                    _this = NetworkUtils()
                }
                return _this!!
            }

        internal val postNumber = 20
        internal val TC_WC_URL = "https://public-api.wordpress.com/rest/v1.1/sites/techcrunch.com/posts/?number=" + postNumber  ///?number=12&pretty=true
    }


    //function for testing with retrofit
    fun getPostsFromWorldPressTCWithRx(observer: Observer<Posts>) : Unit {
        val TC_WC_URL_FOR_RX = "https://public-api.wordpress.com/rest/v1.1/sites/techcrunch.com/" //?number=12&pretty=true

        val service: WCTCPostsService = ServiceFactory.createRetrofitService(WCTCPostsService::class.java, TC_WC_URL_FOR_RX)

        service.getPosts(Companion.postNumber, offset)
                .subscribeOn(Schedulers.newThread())
                //.observeOn(AndroidSchedulers.mainThread())  //need this if directly consuming data in ui thread
                .subscribe(observer)

        //Log.w("+++", "getPostsFromWorldPressTCWithRx() $offset")

        offset += Companion.postNumber

    }

}
