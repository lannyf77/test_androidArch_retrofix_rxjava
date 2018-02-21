package com.example.linma9.mytechcruncharticlelistapplication

import android.app.Activity
import android.arch.lifecycle.*
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.linma9.mytechcruncharticlelistapplication.InfiniteScrollListener
import com.example.linma9.mykotlinapplication2.features.news.adapter.ArticleDelegateAdapter
import com.example.linma9.mykotlinapplication2.features.news.adapter.RecycleViewDataAdapter
import com.example.linma9.mytechcruncharticlelistapplication.DI.componenet.PresentorComponent
import com.example.linma9.mytechcruncharticlelistapplication.DI.module.PresentorModule
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.inflate
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.CTViewDataItem
import com.example.linma9.mytechcruncharticlelistapplication.eventbus.DataEvent
//import com.example.linma9.mytechcruncharticlelistapplication.eventbus.GlobalEventBus
import com.example.linma9.mytechcruncharticlelistapplication.presentor.viewModel.ListDataViewModel
//import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.news_fragment.*
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewTypeConstants
import com.example.linma9.mytechcruncharticlelistapplication.eventbus.RxBus
import com.example.linma9.mytechcruncharticlelistapplication.presentor.Presentor

import com.example.linma9.mytechcruncharticlelistapplication.presentor.viewModel.TheLifeCycleObserve
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import om.example.linma9.mywctcokhttprecycleviewapplication.viewModel.BundleAwareViewModelFactory
import om.example.linma9.mywctcokhttprecycleviewapplication.viewModel.ParcelableViewModel
import java.util.*
import javax.inject.Inject


//import kotlinx.android.synthetic.main.news_fragment.*  // with this it could directly access news_list, witout: findViewById<RecyclerView>(R.id.news_list)

/**
 * Created by linma9 on 1/23/18.
 */

class ArticlesFragment : Fragment(), ArticleDelegateAdapter.onViewSelectedListener {

//    private var redditNews: RedditNews? = null
//    private val newsManager by lazy { NewsManager() }

    companion object {
        private val KEY_REDDIT_NEWS = "redditNews"
    }

    private lateinit var presentorComponent: PresentorComponent
    private var theLifeCycleObserve: TheLifeCycleObserve? = null

    //private var databaseMgr: DataManager? = null
    var listDataViewModel: ListDataViewModel? = null

    private var articlesList: RecyclerView? = null

    //private lateinit var presentorComponenet: PresentorComponent

    private lateinit var swipeContainer : SwipeRefreshLayout


    @Inject
    lateinit var presentor: Presentor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = container?.inflate(R.layout.news_fragment)
        articlesList = view!!.findViewById<RecyclerView>(R.id.articles_list)


        //use dagger to inject viewScope presentor
        if (MyApp.graph != null) {
            presentorComponent = MyApp.graph
                    .addChildModle(PresentorModule(getContext()!!))

            presentorComponent
                    .inject(this)
        }

        setUpPullRefres(view)

        return view
    }

    fun setUpPullRefres(view: View) {
        swipeContainer = view.findViewById(R.id.swipeContainer) as SwipeRefreshLayout

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener {
            presentor.pullDataFromRemoteServer()
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)

    }

    var showLoading: Boolean = true;

    var infiniteScrollListener: InfiniteScrollListener? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        articlesList!!.apply {
            setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout

            clearOnScrollListeners()

            infiniteScrollListener = presentorComponent.getInfiniteScrollListener()

            infiniteScrollListener?.setLayoutManager(linearLayout)

            // testing do custome set the handler instead of using the hardcoded in the PresentorModule
            infiniteScrollListener?.setHandler(
                    {
                        showLoading = true
                        presentor.pullDataFromRemoteServer()
                    }
            )

            infiniteScrollListener?.setListner(
                    {
                        showLoading = it
                    }
            )

// old non-injection way to instantiate the InfiniteScrollListener with
// a custom handling function passed in at this moment

//            infiniteScrollListener = InfiniteScrollListener(
//                    {
//                        presentor.pullDataFromRemoteServer()
//                        //MyApp.presentorComponenet.getPresenter().pullDataFromRemoteServer()
//                        //Presentor.instance.pullDataFromRemoteServer()
//                    },
//                    linearLayout)

            addOnScrollListener(infiniteScrollListener)

            if (savedInstanceState != null) {
                lastFirstVisiblePosition = savedInstanceState.getInt(CONTEXT_SAVED_SCROLL_POSITION, 0)
            }
        }


        theLifeCycleObserve = TheLifeCycleObserve((lifecycle), object : TheLifeCycleObserve.OnLifeCycleChange {
            override fun onCreate() {
                //Log.d("TheLifeCycleObserve","+++ +++ TheLifeCycleObserve:onCreate(), thread:"+Thread.currentThread().getId())

            }

            override fun onStop() {
//                Log.d("TheLifeCycleObserve","+++ +++ --- TheLifeCycleObserve:onStop(), thread:"+Thread.currentThread().getId()+
//                        "\nthis:"+this@ArticlesFragment+
//                        "\nisRegistered(ArticlesFragment): "+ GlobalEventBus.instance.isRegistered(this@ArticlesFragment))
//                if (GlobalEventBus.instance.isRegistered(this@ArticlesFragment)) {
//                    GlobalEventBus.instance.unregister(this@ArticlesFragment)
//                }
                if (mDisposable != null && mDisposable!!.isDisposed) {
                    mDisposable!!.dispose()
                }
                mDisposable = null
            }

            var mDisposable: Disposable? = null
            override fun onStar() {
//                Log.d("TheLifeCycleObserve","+++ +++ TheLifeCycleObserve:onStar(), call initStart(), thread:"+Thread.currentThread().getId()+
//                        "\nthis:"+this@ArticlesFragment+
//                        "\nisRegistered(ArticlesFragment): "+ GlobalEventBus.instance.isRegistered(this@ArticlesFragment)+
//                        "\nisRegistered(ArticlesFragment): "+ GlobalEventBus.instance.isRegistered(this@ArticlesFragment))
//                if (GlobalEventBus.instance.isRegistered(this@ArticlesFragment)) {
//                    GlobalEventBus.instance.unregister(this@ArticlesFragment)
//                }
//                GlobalEventBus.instance.register(this@ArticlesFragment)

                if (mDisposable != null && mDisposable!!.isDisposed) {
                    mDisposable?.dispose()
                }
                mDisposable = RxBus.listen(DataEvent::class.java)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                    //println("Im a Message event ${it.action} ${it.message}")
                    if (it.eventType == DataEvent.EVENT_TYPE_STRING) {
                        if ("settings".equals(it.getStringMessage())) {
                            resetPostFilter (null)
                        }
                    }
                })


                initStart(savedInstanceState)
            }

            override fun onDestroy() {
//                Log.d("TheLifeCycleObserve","+++ +++ --- TheLifeCycleObserve:onDestroy(), ifecycle.removeObserver, thread:"+Thread.currentThread().getId()+
//                        "\nthis:"+this@ArticlesFragment)
//                GlobalEventBus.instance.unregister(this@ArticlesFragment)

                if (mDisposable != null && mDisposable!!.isDisposed) {
                    mDisposable!!.dispose()
                }
                mDisposable = null

                lifecycle.removeObserver((theLifeCycleObserve as LifecycleObserver))
            }

        })
        lifecycle.addObserver((theLifeCycleObserve  as LifecycleObserver))

        //initStart()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun initStart(savedInstanceState: Bundle?) {
        initAdapter()
        observeViewModel(savedInstanceState)
    }

    private fun observeViewModel(savedInstanceState: Bundle?) {

        //Log.d("eee888","+++ +++ %%% ArticlesFragment:observeViewModel(), thread:"+Thread.currentThread().getId())

        mCategoryFilter = null

        val provider : ViewModelProvider.Factory = ViewModelProviders.DefaultFactory(this.getActivity()!!.getApplication())
        val bundleAwareViewModelFactory : BundleAwareViewModelFactory<ParcelableViewModel> = BundleAwareViewModelFactory(savedInstanceState, provider)

        listDataViewModel = ViewModelProviders.of(this, bundleAwareViewModelFactory).get(ListDataViewModel::class.java)

        if (savedInstanceState != null && listDataViewModel is ParcelableViewModel) {
            (listDataViewModel as ParcelableViewModel).writeTo(savedInstanceState)
        }

        // replaced with using bundleAwareViewModelFactory to let it use the savedInstanceState only when instantiate the listDataViewModel
        // listDataViewModel = ViewModelProviders.of(this).get(ListDataViewModel::class.java)

        var savedAuthorFilterId: Int? = listDataViewModel!!.getCurrentAuthorFilterId()

        //Log.i("eee888", "+++ +++ %%% articleFrgmt::observeViewModel(), savedAuthorFilterId:"+savedAuthorFilterId+", old currentAuthorFilterId:"+currentAuthorFilterId)

        currentAuthorFilterId = savedAuthorFilterId

        /*
        subscribe to the observable data source, so it can act on the datasource change
        and add to the ui's list adapter
         */

        listDataViewModel!!.subscribeToDbPostChanges(true, presentor)
        val observableData: MutableLiveData<List<CTViewDataItem>> = listDataViewModel!!.getListDataObservable()
        observableData.observe(this, object : Observer<List<CTViewDataItem>> {
            override fun onChanged(datalist: List<CTViewDataItem>?) {

//                Log.e("eee888-observeViewModel", "+++ +++ %%% <<<>>> observableData.observe:onChanged(), ${datalist!!.size}, currentAuthorFilterId:"+currentAuthorFilterId+
//                        "\nlastFirstVisiblePosition: $lastFirstVisiblePosition, thread: ${Thread.currentThread().getId()}")

                swipeContainer.setRefreshing(false)

                addPostsToRecycleViewData(datalist!!)
                if (lastFirstVisiblePosition > 0) {
                    (articlesList!!.getLayoutManager() as LinearLayoutManager).scrollToPositionWithOffset(lastFirstVisiblePosition, 0)
                    lastFirstVisiblePosition = 0
                }
            }
        })
    }

    val CONTEXT_SAVED_SCROLL_POSITION: String = "CONTEXT_SAVED_SCROLL_POSITION"
    var lastFirstVisiblePosition: Int = 0
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        lastFirstVisiblePosition = (articles_list.getLayoutManager() as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        outState.putInt(CONTEXT_SAVED_SCROLL_POSITION, lastFirstVisiblePosition)

        //Log.d("articlfrgmt", "+++ +++ %%% onSaveInstanceState , currentAuthorFilterId:"+currentAuthorFilterId)
        // let the viewModel save whatever it wants to save to the bundle OS is saving, viewModel's factory will pick it up when fragment's oncreate passes the savedInstance bundle from OS to it
        (listDataViewModel as ParcelableViewModel).writeTo(outState)
    }

    var currentAuthorFilterId: Int? = null
    fun addPostsToRecycleViewData(datalist: List<CTViewDataItem>) : List<CTViewDataItem>{
        var list = datalist
        Log.d("tag", "+++ +++ %%% addPostsToRecycleViewData(),  list: ${list!!.size} currentFilterId: ${currentAuthorFilterId}")
        showLoading = false
        if (datalist != null) {
            if (currentAuthorFilterId != null || mCategoryFilter != null) {
                list = datalist.filter {
                    ////Log.d("tag", "+++ +++ %%% addPostsToRecycleViewData() ${it},  currentFilterId: ${currentAuthorFilterId}")
                    (currentAuthorFilterId == null && (it.categories!!.indexOf(mCategoryFilter!!)>=0)) || (mCategoryFilter == null && it.authorId == currentAuthorFilterId)
                }
                if (list.size < 5) {
                    Log.d("tag", "+++ +++ %%% @@@ addPostsToRecycleViewData(), call pullDataFromRemoteServer(), currentAuthorFilterId: $currentAuthorFilterId")

                    showLoading = true
                    presentor.pullDataFromRemoteServer()

                    //MyApp.presentorComponenet.getPresenter().pullDataFromRemoteServer()

                    //Presentor.instance.pullDataFromRemoteServer()
                }
            }
        } else {
            //Log.e("eee888-observeViewModel", "+++ +++ %%% !!! datalist==null")
        }

        if (list != null) {

            Log.e("eee888-observeViewModel", "+++ +++ %%% call .addArticls(list), list.size:"+list.size)

            (articles_list.adapter as RecycleViewDataAdapter).addArticls(list)
        } else {
            Log.e("eee888-observeViewModel", "+++ +++ %%% !!! datalist==null")
        }
        return list
    }

    /**
     * filter the observable's LiveData
     * reset the RecyclerView articles_list's adapter with filtered data
     */
    fun resetPostFilter (filter: Int?) {
        mCategoryFilter = null
        currentAuthorFilterId = filter

        listDataViewModel!!.setAuthorFilter(currentAuthorFilterId)

        var allLiveDataData: MutableLiveData<List<CTViewDataItem>> = listDataViewModel!!.getListDataObservable()

        var postList: List<CTViewDataItem> = allLiveDataData.value!!
        if (postList != null) {
            var list = postList.filter {
                ////Log.d("tag", "+++ ++ &&& ${it},  currentFilterId: ${currentAuthorFilterId}")
                (currentAuthorFilterId == null) ||
                        (it.getViewType() == ViewTypeConstants.ARTICL && (it as CTViewDataItem).authorId == currentAuthorFilterId)
            }.map {it as CTViewDataItem}

            if (list != null) {
                if (list.size < 10) {
                    //Log.d("tag", "+++ +++ %%% @@@ resetPostFilter(): currentAuthorFilterId: $currentAuthorFilterId")

                    showLoading = true
                    presentor.pullDataFromRemoteServer()

                    //MyApp.presentorComponenet.getPresenter().pullDataFromRemoteServer()
                    //Presentor.instance.pullDataFromRemoteServer()
                }

                infiniteScrollListener!!.reset()

                //Log.w("eee888", "+++ +++ %%% 111 list.size: ${list.size}, bf articles_list.layoutManager.itemCount: ${articles_list.layoutManager.itemCount}")

                if (articles_list != null) {
                    (articles_list.adapter as RecycleViewDataAdapter).clearAndAddArticls(list)
                }

                //Log.w("eee888", "+++ +++ %%% 222 aft articles_list.layoutManager.itemCount: ${articles_list.layoutManager.itemCount}")
            } else {
                //Log.e("eee888-observeViewModel", "+++ +++ %%%  !!! resetPostFilter() datalist==null")
            }

        }
    }

    var mCategoryFilter: String? = null
    fun onSelectedCategory(catefilter: String?) {

        //Log.d("eee888", "+++ +++ onSelectedCategory(), filter: $catefilter")
        mCategoryFilter = null
        currentAuthorFilterId = null

        if (catefilter != null) {
            mCategoryFilter = catefilter

            var allLiveDataData: MutableLiveData<List<CTViewDataItem>> = listDataViewModel!!.getListDataObservable()

            var postList: List<CTViewDataItem> = allLiveDataData.value!!
            if (postList != null) {
                var list = postList.filter {
                    //Log.d("tag", "+++ +++ 111 ${it},  mCategoryFilter: ${mCategoryFilter}")
                    var ret = mCategoryFilter == null
                    if (!ret) {
                        ret = (it.categories!!.indexOf(mCategoryFilter!!)>=0)
                    }
                    ret == true
                }.map {it as CTViewDataItem}

                //Log.e("eee888-observeViewModel", "+++ +++ ++++++ list: $list")

                if (list != null) {

                    infiniteScrollListener!!.reset()

                    if (list.size < 10) {

                        showLoading = true
                        presentor.pullDataFromRemoteServer()

                        //MyApp.presentorComponenet.getPresenter().pullDataFromRemoteServer()

                        //Presentor.instance.pullDataFromRemoteServer()
                    }

                    //Log.w("eee888", "+++ +++ %%% 111 onSelectedCategory(), list.size: ${list.size}, bf articles_list.layoutManager.itemCount: ${articles_list.layoutManager.itemCount}")

                    (articles_list.adapter as RecycleViewDataAdapter).clearAndAddArticls(list)

                    //Log.w("eee888", "+++ +++ %%% 222 onSelectedCategory(), aft articles_list.layoutManager.itemCount: ${articles_list.layoutManager.itemCount}")
                } else {
                    //Log.e("eee888-observeViewModel", "+++ +++ %%%  !!! resetPostFilter() datalist==null")
                }
            }
        }
    }

    private fun initAdapter() {
        if (articles_list.adapter == null) {
            articles_list.adapter = RecycleViewDataAdapter(this)
        }
    }

    override fun onItemSelected(url: String?) {
        if (url.isNullOrEmpty()) {
            Snackbar.make(articlesList!!, "No URL assigned to this news", Snackbar.LENGTH_LONG).show()
        } else {
            launchActionViewIntent(activity, url!!)
        }
    }

    override fun filterOn(filter: Int) {
        resetPostFilter (filter)
    }

    override fun filterOnCategory(catefilter: String?) {

        //Log.d("eee888", "+++ +++ filterOnCategory(), filter: $catefilter")

        showAlertWithThreeButton(catefilter)
    }

    private fun showAlertWithThreeButton(catefilter: String?) {

        if (catefilter != null && catefilter[0] == '{') {
            var newCatefilter = catefilter.substring(1)
            if (newCatefilter.length > 1 && newCatefilter[newCatefilter.length - 1] == '}') {
                newCatefilter = newCatefilter.substring(0, newCatefilter.length - 1)

                var categoryList: ArrayList<String> = newCatefilter.split(",") as ArrayList<String>

                openAlertDialog(DIALOG_WITH_RADIO_BUTTON, "Select category", "show alert with three buttons", categoryList)

            }
        }
    }

    val DIALOG_WITH_RADIO_BUTTON = 1
    val DIALOG_WITH_ALERT_MESSAGE = 2
    protected fun openAlertDialog(id: Int, title: String, alertMessage: String, items: ArrayList<String>) {
        var alertDialog: AlertDialog? = null
        when (id) {
            DIALOG_WITH_RADIO_BUTTON -> {
                val list = items.toTypedArray<CharSequence>()
                val poistion = 0
                val alertDilogBuilder = AlertDialog.Builder(getContext()!!, R.style.myDialogeTheme)
                alertDilogBuilder.setTitle(title)
                        .setSingleChoiceItems(list, poistion, object : DialogInterface.OnClickListener {

                            override fun onClick(dialog: DialogInterface, index: Int) {

                                onSelectedCategory(items[index])

                                Toast.makeText(activity!!.applicationContext,
                                        items[index],  //"which:"+index,
                                        Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }

                        })
//                        .setSingleChoiceItems(list, poistion) {  // syntax using lambdar
//                            dialogInterface, item -> Toast.makeText(activity.applicationContext,
//                                items[item],
//                                Toast.LENGTH_SHORT).show()
//                        }

                alertDialog = alertDilogBuilder.create()
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", {
                    dialogInterface, i ->
                    onSelectedCategory(items[0])
                })
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", {
                    dialogInterface, k ->
                    dialogInterface.dismiss()  //Toast.makeText(activity, "You clicked on NEUTRAL Button:"+k, Toast.LENGTH_SHORT).show()
                })
            }
            else -> {
                val alertDilogBuilder = AlertDialog.Builder(getContext()!!)

                alertDilogBuilder.setTitle(title)
                alertDilogBuilder.setMessage(alertMessage)

                alertDialog = alertDilogBuilder.create()

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "POSITIVE", {
                    c, i ->
                    Toast.makeText(activity, "You clicked on POSITIVE Button", Toast.LENGTH_SHORT).show()
                })

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NEGATIVE", {
                    dialogInterface, j ->
                    Toast.makeText(activity, "You clicked on NEGATIVE Button", Toast.LENGTH_SHORT).show()
                })
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NEUTRAL", {
                    dialogInterface, k ->
                    Toast.makeText(activity, "You clicked on NEUTRAL Button", Toast.LENGTH_SHORT).show()
                })
            }
        }
        alertDialog!!.show()

        val strings = listOf("a", "ab", "abc")
        val oddLength = compose(this::isOdd, this::isLength)
        strings.filter(oddLength)

    }

    fun isOdd(x: Int) = x% 2 != 0
    fun isLength(x: String) = x.length

    fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C {
        return { x -> f(g(x)) }
    }

    internal inner class PositiveButtonClickListener : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            dialog.dismiss()
        }
    }

    // syntax of object
    var selectItemListener: DialogInterface.OnClickListener = object : DialogInterface.OnClickListener {

        override fun onClick(dialog: DialogInterface, which: Int) {
            // process
            //which means position
            Toast.makeText(activity!!.applicationContext,
                    "which:"+which,
                    Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

    }

    fun launchActionViewIntent(context: Context?/* activity context*/, url: String): Boolean {
        var opened = false
        // just try out if the chrome tab is supported
        try {
            if (context != null && context is Activity) {
                val builder = CustomTabsIntent.Builder()
//                builder.setToolbarColor(ContextCompat.getColor(context!!, if (ThemeUtils.isDarkTheme())
//                    R.color.dark_action_bar_color
//                else
//                    R.color.actionbar_background_color))
                builder.setStartAnimations(context, R.anim.slide_in_from_right, R.anim.slide_left_out)
                builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, Uri.parse(url))
                opened = true
            }
        } catch (e: Exception) {
            opened = false
        }

        if (!opened) {// fallback to using normal browser
            try {
                if (context != null && context is Activity) {
                    context!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } else {
                    return false
                }
            } catch (e: Exception) {
                //Log.e("eee888", "launchActionViewIntent() Exception !!!, e:" + e+"\nurl:"+url)
                return false
            }

        }
        return true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        super.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        super.onPause()
    }

    // greenrobot EventBus
    fun onEvent(event: DataEvent) {
        //Log.e("eee888", "+++ +++ +++ %%% articlFrgmt::onEvent(), ${event.eventType} urrentAuthorFilterId: $currentAuthorFilterId")
        if (event.eventType == DataEvent.EVENT_TYPE_STRING) {
            if ("settings".equals(event.getStringMessage())) {
                resetPostFilter (null)
            }
        }
    }

}