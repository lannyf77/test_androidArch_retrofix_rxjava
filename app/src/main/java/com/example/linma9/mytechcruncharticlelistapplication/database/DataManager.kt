package com.example.linma9.mytechcruncharticlelistapplication.database

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import android.util.Log
import com.example.linma9.mytechcruncharticlelistapplication.MyApp
import com.example.linma9.mytechcruncharticlelistapplication.database.DatabaseUtils.DatabaseInitializer
import com.example.linma9.mytechcruncharticlelistapplication.model.data.Author
import com.example.linma9.mytechcruncharticlelistapplication.model.data.Category
import com.example.linma9.mytechcruncharticlelistapplication.model.data.Post
import com.example.linma9.mytechcruncharticlelistapplication.model.data.Posts
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.CTViewDataItem
import com.example.linma9.mytechcruncharticlelistapplication.eventbus.DataEvent
//import com.example.linma9.mytechcruncharticlelistapplication.eventbus.GlobalEventBus
import com.example.linma9.mytechcruncharticlelistapplication.eventbus.RxBus
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable

import java.util.ArrayList
import javax.inject.Singleton
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver


/**
 * Created by linma9 on 1/23/18.
 */


/**
    DataManager manages the database
 it provides MutableLiveData for monitoring the data chnage
 */

//@Singleton  // since we use scope @ApplicationScope, so it is application wide singleton managered by dagger
class DataManager {

    private var mDb: AppDatabase? = null
    private var mApplication: Application? = null

    private var mTCListObservable: MutableLiveData<List<CTViewDataItem>> = MutableLiveData()

    companion object {
        val TAG = DataManager::class.java.name

        private var dataMgr: DataManager? = null
        val instance: DataManager?
            @Synchronized get() {
                return dataMgr
            }
    }

    /**
     * the use of DataManager.instance have been replaced with dagger injection, no place is using DataManager.instance
     * in case wnat to do that again, constructor has to be called before DataManager.instance
     * for that use case senario here to set the dataMgr for later the call of DataManager.instance
     */

    constructor(application: Application) {
        mApplication = application
        dataMgr = this

        Log.e("eee888", "+++ +++ @@@ +++++++++++++++++++ DataManager:ctor(), this: ${this}")
    }

    var mDisposable: Disposable? = null
    fun registerEvtBus() {
//        Log.e("eee888", "+++ +++ @@@ TheLifeCycleObserve+++++++++++++++++++ DataManager:registerEvtBus(), this: ${this}"+
//                "\nisRegistered(DataManager): "+ GlobalEventBus.instance.isRegistered(this))
//        if (GlobalEventBus.instance.isRegistered(this)) {
//            GlobalEventBus.instance.unregister(this)
//        }
//        GlobalEventBus.instance.register(this)

        if (mDisposable != null && mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }

        mDisposable = registerRxBus();

//        mDisposable = RxBus.listen(DataEvent::class.java).subscribeWith(object : DisposableObserver<DataEvent>() {
//            override fun onComplete() {
//                Log.e(TAG, "+++eee888 onComplete: All Done!")        }
//
//            override fun onNext(t: DataEvent) {
//                Log.e(TAG, "+++eee888on Next: " + t)
//                onDataReady(t)        }
//
//            override fun onError(e: Throwable) {
//                Log.e(TAG, "+++eee888 onError: ")
//            }
//        })
    }

    fun registerRxBus() : Disposable {
        return RxBus.listen(DataEvent::class.java).subscribeWith(object : DisposableObserver<DataEvent>() {
            override fun onComplete() {
                Log.e(TAG, "+++eee888 onComplete: All Done!")        }

            override fun onNext(t: DataEvent) {
                Log.e(TAG, "+++eee888on Next: " + t)
                onDataReady(t)        }

            override fun onError(e: Throwable) {
                Log.e(TAG, "+++eee888 onError: ")
            }
        })
    }

    //  cannot use the created instance, You basically MUST instantiate a new observer each time.
    // The protocol says that an observer may only receive one subscribe event.
    // see https://stackoverflow.com/questions/47020972/android-rxjava2-subscription-within-onclick

//    var theObserver: DisposableObserver<DataEvent> = object : DisposableObserver<DataEvent>() {
//        override fun onComplete() {
//            Log.e(TAG, "+++eee888 onComplete: All Done!")        }
//
//        override fun onNext(t: DataEvent) {
//            Log.e(TAG, "+++eee888on Next: " + t)
//            onDataReady(t)        }
//
//        override fun onError(e: Throwable) {
//            Log.e(TAG, "+++eee888 onError: ")
//        }
//    }
    ///

    fun unregisterEvtBus() {
//        //Log.e("eee888", "+++ +++ @@@ TheLifeCycleObserve+++++++++++++++++++ DataManager:unregisterEvtBus(), this: ${this}")
//        GlobalEventBus.instance.unregister(this)

        if (mDisposable != null && mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
        mDisposable = null
    }

    fun createDb() {
        mDb = AppDatabase.getInMemoryDatabase(mApplication!!)

        //Log.i("eee888", "+++ +++ @@@ createDb(), mDb: ${mDb}");
    }

    fun subscribeToDbPostChanges(startInOtherThread: Boolean) : MutableLiveData<List<CTViewDataItem>> {

        //Log.i(DatabaseInitializer.TAG, "+++ %%% %%% enter subscribeToDbPostChanges(), mDb: ${mDb}");

        var ctListObservable: MutableLiveData<List<CTViewDataItem>> = mTCListObservable

        if (startInOtherThread) {
            val thread = Thread({
                //Log.d(TAG, "+++ ~~~~~~~~~~~~~ runThreadExample(): ${Thread.currentThread().name}, threadId: ${Thread.currentThread().id}")
                doSubscribeToDbPostChanges(ctListObservable)
            })
            thread.start()
        } else {
            doSubscribeToDbPostChanges(ctListObservable)
        }
        // return LiveData object so updates are observed (populated or not).
        return ctListObservable
    }

    fun doSubscribeToDbPostChanges(ctListObservable: MutableLiveData<List<CTViewDataItem>>) : MutableLiveData<List<CTViewDataItem>> {

        //Log.i(DatabaseInitializer.TAG, "+++ ((())) enter doSubscribeToDbPostChanges(), mDb: ${mDb}");

        // Populate it with initial CTData
        DatabaseInitializer.instance.populateSync(mDb!!)  // for pull in initial CTData

        val authorList: List<DbAuthor> = mDb!!.authModel().loadAllAuthors()
        val postList = mDb!!.postModel().loadAllPosts()  // load from database

        //Log.w("eee888", "+++ +++ @@@ ((())) in doSubscribeToDbPostChanges postList: " + postList?.size+"\nauthorList:"+authorList?.size +", thread:"+Thread.currentThread().getId())

        if (postList == null || postList.size < 10) {

            MyApp.graph.getDataRepository().buildCTListObservable()
            //DataRepository.instance.buildCTListObservable()
        }

        if (postList != null) {

            val convertedPosts = buildTCPostViewData(postList)
            ctListObservable.postValue(convertedPosts)   //setValue(convertedPosts) will get IllegalStateException: Cannot invoke setValue on a background thread
        }

        //Log.i("eee888", "+++ ((())) --- exit doSubscribeToDbPostChanges(), mDb: ${mDb}, thread:"+Thread.currentThread().getId())

        return ctListObservable
    }

    fun buildTCPostViewData(postList: List<DbPost>) : ArrayList<CTViewDataItem> {
        val convertedPosts = ArrayList<CTViewDataItem>()
        for (dbpost: DbPost in postList) {

            ////Log.w("eee888", "+++ +++ getTCPostDataFromDatabase(), in loop dbpost: $dbpost");

            val id = dbpost.authorId!!
            val dbAuthor: DbAuthor = mDb!!.authModel().loadUserById(id)

            ////Log.w("eee888", "+++ +++ getTCPostDataFromDatabase(), in loop, dbpost.authorId!! $id,  dbAuthor: ${dbAuthor.toString()}");

            val postId: Int = dbpost.postId!!
            val authorName = dbAuthor!!.firstName + " " + dbAuthor!!.lastName
            val avatarUrl: String = dbAuthor!!.avatarURL ?: ""
            val title = dbpost.title ?: " == no title == "
            val dateStr: String = dbpost.date ?: "2017-06-20T01:19:14-07:00"
            var excerpt: String = dbpost.excerpt ?: ""
            val url: String = dbpost.url ?: ""
            var categories: String = dbpost.categories ?: ""

            var author: Author = copyToAuthor(dbAuthor)

            val tcData = MyApp.graph.getDataRepository().buildOneTCData(author, postId, authorName, avatarUrl, title, dateStr, excerpt, url,
                    categories)
//            val tcData = DataRepository.instance.buildOneTCData(author, postId, authorName, avatarUrl, title, dateStr, excerpt, url,
//                    categories)
            convertedPosts.add(tcData)
        }

        return convertedPosts
    }

    fun getTCPostDataFromDatabase() : List<CTViewDataItem>? {
        var ctPostDataList: List<CTViewDataItem>? = null

        val authorList: List<DbAuthor> = mDb!!.authModel().loadAllAuthors()
        val postList = mDb!!.postModel().loadAllPosts()

        //Log.w("eee888", "+++ +++ @@@ ((())) in getTCPostDataFromDatabase postList: " + postList?.size+"\nauthorList:"+authorList?.size +", thread:"+Thread.currentThread().getId())

        if (postList == null || postList.size < 10) {
            MyApp.graph.getDataRepository().buildCTListObservable()
            //DataRepository.instance.buildCTListObservable()
        }

        if (postList != null) {
            ctPostDataList = buildTCPostViewData(postList)
        }
        return ctPostDataList;
    }

    //
    // greenrobot EventBus
    fun onEvent(event: DataEvent) {
        //Log.e("eee888", "+++ +++ %%% ====== dataMgr::onEvent(), ${this}, ${event}")
        onDataReady(event)
    }

    fun onDataReady(event: DataEvent) {
        Log.e("eee888", "+++ +++ %%% 111 eee888 dataMgr::onDataReady(), ${event} thread: ${Thread.currentThread().getId()}")
        if (event.eventType == DataEvent.EVENT_TYPE_TCDATA ||
                event.eventType == DataEvent.EVENT_TYPE_POSTS_DATA) {
            var ctDataList: List<CTViewDataItem>? = null
            if (event.eventType == DataEvent.EVENT_TYPE_TCDATA) {
                ctDataList = event!!.getEventData() as List<CTViewDataItem>

            } else if (event.eventType == DataEvent.EVENT_TYPE_POSTS_DATA) {
                var post: Posts = event!!.getPostData() as Posts
                //Log.e("eee888", "+++ +++ @@@ 222 eee888 dataMgr::onDataReady(), post.posts.size: ${post.posts?.size}")
                insertPostsToDatabase(post)
                ctDataList = getTCPostDataFromDatabase()

                //Log.e("eee888", "+++ +++ @@@ 333 eee888 dataMgr::onDataReady(), ctDataList.size: ${ctDataList?.size}")
            }
            updateTCLiveData(ctDataList)
        }
    }

    // for directly update liveData
    fun updateTCLiveData(ctDataList: List<CTViewDataItem>?) {

        ////Log.i("eee888", "+++ +++ ((())) udataMgr::pdateTCLiveData(), ctDataList: ${ctDataList!!.size}, thread:"+Thread.currentThread().getId())

        if (ctDataList != null && ctDataList.size > 0) {
            mTCListObservable.postValue(ctDataList)
        }
    }

    fun addAuther(author: DbAuthor): Long {
        var rowId: Long = mDb!!.authModel().insertAuthor(author)
        ////Log.d("eee888", "+++ +++ {{{}}} addAuther(), rowId: " + rowId+", author :"+author.toString())
        return rowId  // row Id
    }

    fun addPost(dbPost: DbPost): DbPost {
        mDb!!.postModel().insertDbPost(dbPost)
        return dbPost
    }

    fun insertPostsToDatabase(ctPosts: Posts?) {
        //Log.i("eee888", "+++ +++ @@@ ((())) udataMgr::insertToDatabase(), ctDataList: ${ctPosts?.posts?.size}, thread:"+Thread.currentThread().getId())

        if (ctPosts != null) {
            val posts: ArrayList<Post>? = ctPosts.posts
            if (posts != null && posts.size > 0) {
                for (postData: Post in posts) {
                    var author: Author = postData.author!!
                    var dbAuthor: DbAuthor = copyToDbAuthor(author)
                    addAuther(dbAuthor)

                    val dbPost: DbPost = copToDbPost(postData)
                    addPost(dbPost)
                }
            }
        }
    }

    fun resetDtabase() {
        mDb!!.authModel().deleteAll()
        mDb!!.postModel().deleteAll()

        MyApp.graph.getDataRepository().pullDataFromRemoteServer()
        //DataRepository.instance.pullDataFromRemoteServer()
    }


    // helper

    fun buildCategoryString(post: Post) : String {
        var categories: String = ""
        val categoriesMap: Map<String, Category>? = post.categories
        if (categoriesMap != null && !categoriesMap.isEmpty()) {
            var list = ArrayList<String>()
            for ((key, categry) in categoriesMap) {
                if (categry != null && !TextUtils.isEmpty(categry.name)) {
                    list.add(categry.name!!)
                }
            }
            if (!list.isEmpty()) {
                categories = "{"+list.joinToString()+"}"
            }
        }
        return categories
    }

    fun copyToAuthor(dbAuthor: DbAuthor) : Author{
        var au = Author()
        au.id = dbAuthor.authorid   //dbAuthor has its own 'id' field for different use
        au.avatarURL = dbAuthor.avatarURL
        au.name = dbAuthor.name
        au.firstName = dbAuthor.firstName
        au.email = dbAuthor.email
        au.lastName = dbAuthor.lastName
        au.login = dbAuthor.login
        au.niceName = dbAuthor.niceName
        au.profileURL = dbAuthor.profileURL
        au.url = dbAuthor.url
        au.siteID = dbAuthor.siteID

        return au
    }

    fun copyToDbAuthor(author: Author) : DbAuthor{
        var dbAuthor = DbAuthor()
        dbAuthor.authorid = author.id
        dbAuthor.avatarURL = author.avatarURL
        dbAuthor.name = author.name
        dbAuthor.firstName = author.firstName
        dbAuthor.email = author.email
        dbAuthor.lastName = author.lastName
        dbAuthor.login = author.login
        dbAuthor.niceName = author.niceName
        dbAuthor.profileURL = author.profileURL
        dbAuthor.url = author.url
        dbAuthor.siteID = author.siteID

        return dbAuthor
    }

    fun copToDbPost(post: Post) : DbPost {
        var dbPost = DbPost()

        dbPost.postId = post.ID
        dbPost.title = post.title
        dbPost.authorId = post.author!!.id
        dbPost.date = post.date
        dbPost.modified = post.modified
        dbPost.url = post.url
        dbPost.shortURL = post.shortURL
        dbPost.excerpt = post.excerpt
        dbPost.featured_image = post.featured_image
        dbPost.categories = buildCategoryString(post)

        return dbPost
    }
}