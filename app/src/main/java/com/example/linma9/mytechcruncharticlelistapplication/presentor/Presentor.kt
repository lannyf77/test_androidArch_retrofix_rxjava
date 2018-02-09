package com.example.linma9.mytechcruncharticlelistapplication.presentor

import android.arch.lifecycle.MutableLiveData
import com.example.linma9.mytechcruncharticlelistapplication.MyApp
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.CTViewDataItem
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.DataRepository
import javax.inject.Inject

/**
 * Created by linma9 on 1/23/18.
 */
class Presentor {

    companion object {
        private var presentor: Presentor? = null
        val instance: Presentor
            @Synchronized get() {
                if (presentor == null) {
                    presentor = Presentor()
                }
                return presentor!!
            }
    }

    fun pullDataFromRemoteServer() {

        MyApp.graph.getDataRepository().pullDataFromRemoteServer()
        //DataRepository.instance.pullDataFromRemoteServer()
    }

    fun getDataList(): MutableLiveData<List<CTViewDataItem>> {

        return MyApp.graph.getDataRepository().getDataList()
        //return DataRepository.instance.getDataList()
    }
}