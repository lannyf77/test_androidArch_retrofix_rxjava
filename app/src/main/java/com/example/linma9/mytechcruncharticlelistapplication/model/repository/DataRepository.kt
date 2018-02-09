package com.example.linma9.mytechcruncharticlelistapplication.model.repository

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.linma9.mytechcruncharticlelistapplication.MyApp
import com.example.linma9.mytechcruncharticlelistapplication.database.DataManager
import com.example.linma9.mytechcruncharticlelistapplication.model.data.*

import com.example.linma9.mytechcruncharticlelistapplication.eventbus.DataEvent
import com.example.linma9.mytechcruncharticlelistapplication.eventbus.GlobalEventBus
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by linma9 on 1/23/18.
 */

/**
    DataRepository responsible for pulling the data from remote server
    DataRepository provides a MutableLiveData
    the MutableLiveData is filled after polled from remote, and the new data will be instered into database
    after the MutableLiveData is udated it will notify its observer
 */

@Singleton
class DataRepository @Inject constructor() {
//    class DataRepository @Inject private constructor() {

    var mCTListObservable: MutableLiveData<List<CTViewDataItem>> = MutableLiveData()

    init {
        //Log.d("eee888","+++ +++ DataRepository:init()"+"\nthread:"+Thread.currentThread().getId())

        /**
         * if using @Singleton & @Inject constructor(), then the dagger will provide the singleton
         * so set the dataRepository for later the call of DataRepository.instance
         */
        if (dataRepository == null) {
            dataRepository = this
        }

        // If any transformation is needed, this can be simply done by Transformations class ...
        //buildCTListObservable()
    }

    fun getDataList(): MutableLiveData<List<CTViewDataItem>> {
        return mCTListObservable
    }

    private fun simulateDelay() {
        try {
            Thread.sleep(10)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    /**
     * if using @Singleton & @Inject constructor(), then the dagger will provide the singleton
     * so no place should use DataRepository.instance to get this
     */
    companion object {
        private var dataRepository: DataRepository? = null
        val instance: DataRepository
            @Synchronized get() {
                if (dataRepository == null) {
                    dataRepository = DataRepository()
                }
                return dataRepository!!
            }
    }

    fun buildCTListObservable() {
        pullDataFromRemoteServer()
    }

    val usingRetrofitRxJava = true

    var disposeable : Disposable? = null
    fun pulldataFromRemoteServerWithRetrofit() {

        disposeable?.dispose()
        disposeable = null

        val okHtppUtil = MyApp.graph.getNetworkUtils()  //NetworkUtils.instance
        okHtppUtil.getPostsFromWorldPressTCWithRx(object : Observer<Posts> {
            override fun onNext(posts: Posts) {

                //Log.i("+++", "Observer:onNext() ${posts.toString()}")

                onPostsReady (posts)
            }

            override fun onComplete() {
                //Log.i("+++", "Observer:onComplete()")
            }

            override fun onError(e: Throwable) {
                //Log.e("+++", "Observer:onError() {$e.oString()}")
            }

            override fun onSubscribe(d: Disposable) {
                //Log.e("+++", "Observer:onSubscribe() {$d}")
                disposeable = d   //need  to call d.dispose() somewhere
            }
        })}

    fun pullDataFromRemoteServer() {

        if (usingRetrofitRxJava) {
            pulldataFromRemoteServerWithRetrofit()
            return
        }

        val gson = Gson()
        val okHtppUtil = MyApp.graph.getNetworkUtils()  //NetworkUtils.instance

        //Log.w("eee888-testWCGson", "+++ +++ @@@ pullDataFromRemoteServer(), call okHtppUtil.getPostsFromWorldPressTC()")

        okHtppUtil.getPostsFromWorldPressTC(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                //Log.w("eee888-testWCGson", "+++ +++ @@@ pullDataFromRemoteServer():onFailure(), \nresp:"+e.toString())
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response!!.isSuccessful()) {

                    val posts = gson.fromJson(response.body()!!.charStream(), Posts::class.java)
                    onPostsReady (posts)

                } else {
                    //Log.w("eee888-testWCGson", "+++ !!! okHttp_testWCGson():onResponse(), !response!!.isSuccessful(), \nresponse:"+response)
                }
            }

        })
    }

    fun onPostsReady (posts: Posts) {
        //Log.w("eee888-testWCGson", "+++ +++ @@@ onPostsReady(), ${posts.posts!!.size}, ==\n ${posts}")

//                    var postsJsonStr = gson.toJson(posts)
//
//                    var jsonObj = JSONObject(postsJsonStr)
//                    //Log.w("eee888-testWCGson", "+++ okHttp_testGson():onResponse(), gson.toJson(gist): "+jsonObj.toString(2))

        var postsArray: ArrayList<Post>? = posts!!.posts
        if (postsArray != null && postsArray.size > 0) {

            val convertedPosts = ArrayList<CTViewDataItem>()
            for (post: Post in postsArray) {

                var postId: Int? = post.ID
                val author: Author? = post.author
                val authorName = author!!.firstName + " " + author!!.lastName
                val avatarUrl: String = author!!.avatarURL ?: ""
                val title = post.title ?: " == no title == "
                val dateStr: String = post.date ?: "2017-06-20T01:19:14-07:00"
                var excerpt: String = post.excerpt ?: ""
                val url: String = post.url ?: ""

                var categories: String = MyApp.dataMgrComponenet.getDataManager().buildCategoryString(post)
                //var categories: String = DataManager.instance!!.buildCategoryString(post)

                val tcData = buildOneTCData(author, postId!!, authorName, avatarUrl, title, dateStr, excerpt, url,
                        categories)
                convertedPosts.add(tcData)
            }

            mCTListObservable.postValue(convertedPosts)   //setValue(convertedPosts) will get IllegalStateException: Cannot invoke setValue on a background thread

            ////Log.w("eee888-testWCGson", "+++ +++ pullDataFromRemoteServer():onResponse(), thread: ${Thread.currentThread().getId()},\n ${convertedPosts}")

            // let databse to do insert
            var dataEvt: DataEvent = DataEvent()
            dataEvt.setPostData(posts)

            //Log.w("eee888-testWCGson", "+++ +++ %%% pullDataFromRemoteServer():onResponse(), call GlobalEventBus.instance.post(dataEvt): ${dataEvt} thread: ${Thread.currentThread().getId()}")

            //have no way to to unregister in the DataManager so use directly call
            GlobalEventBus.instance.post(dataEvt)

            //DataManager.instance!!.onDataReady(dataEvt)
        }
    }

    fun buildOneTCData(author: Author, postId: Int, authorName: String, avatarUrl:
                        String, title: String, dateStr: String, excerpt: String, url: String,
                       categories: String) : CTViewDataItem {

        //val dateFormatter = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z")

        //val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX") //ISO 8601

        //val isoFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        //var simpleFormatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        var longDate: Long = 1457207701L

        try {

            val newdate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(dateStr)
            longDate = newdate.getTime()

            //val convertdate = Date(longDate)
            //strDate = parser.format(convertdate)

        } catch (e: Exception) {
            ////Log.e("eee888", "+++ !!! date convert exception SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ssX\"), ${e.toString()}")


            try {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(dateStr)
                longDate = date.getTime()
            } catch(e: Exception) {
                ////Log.e("eee888", "+++ !!! date convert exception SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss.SSSXXX\"), ${e.toString()}")
                try {
                    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr)
                    longDate = date.getTime()
                } catch(e: Exception) {
                    ////Log.e("eee888", "+++ !!! date convert exception SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\"), ${e.toString()}")
                    try {
                        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateStr)
                        longDate = date.getTime()
                    } catch(e: Exception) {
                        ////Log.d("eee888", "+++ !!! date convert exception SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\"), ${e.toString()}")

                        try {
                            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z").parse(dateStr)
                            longDate = date.getTime()
                        } catch (e: Exception) {
                            ////Log.d("eee888", "+++ !!! date convert exception SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss Z\"), ${e.toString()}")
                            try {
                                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(dateStr)  //<== this worked on api 23
                                longDate = date.getTime()
                            } catch (e: Exception) {
                                ////Log.w("eee888", "+++ !!! date convert exception SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ssZ\"), ${e.toString()}")
                            }
                        }
                    }
                }
            }
        }

        var ctData = CTViewDataItem(
                postId,
                authorName,
                title,
                longDate, //date, // time
                avatarUrl, // image url
                url,
                excerpt,
                categories,
                author.id!!
        )

        return ctData
    }
}