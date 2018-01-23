package com.example.linma9.mytechcruncharticlelistapplication.presentor

import android.arch.lifecycle.MutableLiveData
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.CTViewDataItem
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.DataRepository

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

    public fun pullDataFromRemoteServer() {
        DataRepository.instance.pullDataFromRemoteServer()
    }

    public fun getDataList(): MutableLiveData<List<CTViewDataItem>> {
        return DataRepository.instance.getDataList()
    }
}