package com.example.linma9.mytechcruncharticlelistapplication

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.linma9.mytechcruncharticlelistapplication.DI.scope.ViewScope
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by linma9 on 1/23/18.
 */

class InfiniteScrollListener (val func: () -> Unit
                              ) : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    private var visibleThreshold = 2
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private lateinit var layoutManager: LinearLayoutManager

    lateinit var mHandlerFunc: (() -> Unit)

    fun setLayoutManager(layoutMgr: LinearLayoutManager) {
        layoutManager = layoutMgr
    }

    fun setHandler(func: () -> Unit) {
        mHandlerFunc = func
    }

    lateinit var mListenFunc: ((loading: Boolean) -> Unit)
    fun setListner(func: (loading: Boolean) -> Unit) {
        mListenFunc = func
    }

    init{
        //Log.i("InfiniteScrollListener", "+++ +++ in init ")
        setHandler(func)
    }

    fun reset() {
        previousTotal = 0
        loading = true
        visibleThreshold = 2
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy) //RecyclerView.onScrolled() does nothing
        if (dy > 0) {
            visibleItemCount = recyclerView.childCount
            totalItemCount = layoutManager.itemCount
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

//            Log.i("InfiniteScrollListener", "+++ +++ %%% onScrolled(), \nvisibleItemCount:"+visibleItemCount+
//                    "\ntotalItemCount:"+totalItemCount+
//                    "\npreviousTotal:"+previousTotal+
//                    "\nfirstVisibleItem:"+firstVisibleItem+
//                    "\nloading:"+loading+
//                    "\n(totalItemCount - visibleItemCount):"+(totalItemCount - visibleItemCount)+
//                    ", firstVisibleItem + visibleThreshold:"+(firstVisibleItem + visibleThreshold))

            // normal case the totalItemCount is >= than previousTotal
            // the filter change may reduce the layoutManager.itemCount
            if (totalItemCount < previousTotal) {
                previousTotal = totalItemCount
                if (loading) {
                    loading = false
                }
            }

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                Log.e("InfiniteScrollListener", "+++ +++ %%% call func() End reached")
                mHandlerFunc()
                loading = true
            }

//            Log.d("InfiniteScrollListener", "+++ +++ %%% loading: "+loading)

            if(mListenFunc != null) {
                mListenFunc(loading)
            }
        } else {
            if(mListenFunc != null) {
                mListenFunc(false)
            }
        }

    }
}