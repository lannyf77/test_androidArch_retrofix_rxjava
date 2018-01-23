package com.example.linma9.mykotlinapplication2.features.news

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewType
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewTypeDelegateAdapter
import com.example.linma9.mytechcruncharticlelistapplication.R
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.inflate


/**
 * Created by linma9 on 1/23/18.
 */

class LoadingDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        ////Log.w("eee888", "+++ +++ LoadingDelegateAdapter:onBindViewHolder(), item:"+item+", holder:"+holder)
    }

    /**
     * could be :
     * override fun onCreateViewHolder(parent: ViewGroup) = TurnsViewHolder(parent)
     */
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        ////Log.w("eee888", "+++ +++ LoadingDelegateAdapter:onCreateViewHolder(), ViewGroup:"+parent+", layoutId:"+ R.layout.news_item_loading)
        return LoadingViewHolder(parent)
    }

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder (parent.inflate(R.layout.news_item_loading)) {
    }
}