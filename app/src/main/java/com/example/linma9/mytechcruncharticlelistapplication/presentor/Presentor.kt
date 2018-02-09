package com.example.linma9.mytechcruncharticlelistapplication.presentor

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.linma9.mytechcruncharticlelistapplication.MyApp
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.CTViewDataItem


/**
 * Created by linma9 on 1/23/18.
 */
class Presentor {

    /**
     * the companion object is used for global sington Presentor.instance
     *
     * now using dagger subcomponenet to scope it (one new instance for on subcomponenet), so not in use any more
     *
     * for this appit does not make difference since the downstream object are singleton,
     * but comment out for only experiment the subcomponent scop, i.e
     * whenever
     * val m1 = MyApp.graph
                .addChildModle(PresentorModule())
       val p1 = m1.getPresenter()

       val p2 = m1.getPresenter()
     *
     *   for this component the p1 and p2 will be same one,so the PresentorModule::presentor() only called at first time
     *
     * the PresentorComponent::getPresenter() will be called,
     * which dagger will controls whether to call into the provider of PresentorModule::presentor() for first instantiation or
     * get from dagger kept single sinstance for this component
     */
//    companion object {
//        private var presentor: Presentor? = null
//
//        val instance: Presentor
//            @Synchronized get() {
//                if (presentor == null) {
//
//                    Log.i("+++", "Presentor(). Presentor.instance was null, this:"+this)
//
//                    presentor = Presentor()
//                } else {
//                    Log.e("+++", "Presentor(), Presentor.instance exsit:"+presentor)
//                }
//                return presentor!!
//            }
//    }

    fun pullDataFromRemoteServer() {

        MyApp.graph.getDataRepository().pullDataFromRemoteServer()
        //DataRepository.instance.pullDataFromRemoteServer()
    }

    fun getDataList(): MutableLiveData<List<CTViewDataItem>> {

        return MyApp.graph.getDataRepository().getDataList()
        //return DataRepository.instance.getDataList()
    }
}