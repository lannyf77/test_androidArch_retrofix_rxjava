package com.example.linma9.mytechcruncharticlelistapplication.presentor.viewModel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.util.Log

import com.example.linma9.mytechcruncharticlelistapplication.MyApp
import com.example.linma9.mytechcruncharticlelistapplication.database.DataManager
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.CTViewDataItem
import com.example.linma9.mytechcruncharticlelistapplication.presentor.Presentor

import om.example.linma9.mywctcokhttprecycleviewapplication.viewModel.ParcelableViewModel

/**
 * Created by linma9 on 1/23/18.
 */


/**
 * android.arch.lifecycle ViewModel
 * provide MutableLiveData to the UI to observe
 */

class ListDataViewModel(application: Application) : ParcelableViewModel(application) {

    private var currentAuthorFilterId : Int? = null

    fun setAuthorFilter(currentAuthorFilterId: Int?) {
        this.currentAuthorFilterId = currentAuthorFilterId
    }

    override fun writeTo(bundle: Bundle) {

        ////Log.d("eee888","+++ +++ %%% ListDataViewModel:writeTo(), currentAuthorFilterId: $currentAuthorFilterId"+"\nthread:"+Thread.currentThread().getId())

        var authorFilter = currentAuthorFilterId
        if (authorFilter == null) {
            authorFilter = -1
        }
        bundle.putInt(ListDataViewModel.LAST_SAVED_AUTHOR_FILTER, authorFilter)
    }

    override fun readFrom(bundle: Bundle) {
        var authorFilter = bundle.getInt(ListDataViewModel.LAST_SAVED_AUTHOR_FILTER)

        ////Log.d("eee888","+++ +++ %%% ListDataViewModel:readFrom(), authorFilter: $authorFilter"+"\nthread:"+Thread.currentThread().getId())


        if (authorFilter == null || authorFilter < 0) {
            currentAuthorFilterId = null
        } else {
            currentAuthorFilterId = authorFilter
        }
    }

    fun getCurrentAuthorFilterId() : Int? {
        return currentAuthorFilterId
    }

    companion object {
        public val LAST_SAVED_AUTHOR_FILTER: String = "LAST_SAVED_AUTHOR_FILTER"
    }

    private var mDataMgr: DataManager

    /**
     * Expose the LiveData query so the UI can observe it.
     */
    var mTCListObservable: MutableLiveData<List<CTViewDataItem>> = MutableLiveData()

    init {
        ////Log.d("eee888","+++ +++ %%% ListDataViewModel:init(), currentAuthorFilterId: $currentAuthorFilterId"+"\nthread:"+Thread.currentThread().getId())

        mDataMgr = MyApp.dataMgrComponenet.getDataManager()
        //mDataMgr = DataManager(application)
        mDataMgr.createDb()
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    fun getListDataObservable(): MutableLiveData<List<CTViewDataItem>> {
        return mTCListObservable
    }

    fun subscribeToDbPostChanges(startInOtherThread: Boolean, presentor: Presentor) : MutableLiveData<List<CTViewDataItem>> {

        if (mDataMgr != null) {
            Log.d("eee888","+++ +++ +++ ListDataViewModel:subscribeToDbPostChanges()"+"\nthread:"+Thread.currentThread().getId())
            mTCListObservable =  mDataMgr!!.subscribeToDbPostChanges(startInOtherThread)

        } else {
            Log.w("eee888","+++ @@@@@@@@@@@@@ mDataMgr==null, ListDataViewModel:subscribeToDbPostChanges()"+"\nthread:"+Thread.currentThread().getId())

            //for testing, directly pull from repository
            mTCListObservable = presentor.getDataList()

            //mTCListObservable = MyApp.presentorComponenet.getPresenter().getDataList()
            //mTCListObservable = Presentor.instance.getDataList()
        }
        return mTCListObservable
    }
}