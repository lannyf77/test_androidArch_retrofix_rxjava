package com.example.linma9.mykotlinapplication2.features.news

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewType
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewTypeDelegateAdapter
import com.example.linma9.mytechcruncharticlelistapplication.R
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.inflate
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.CTViewDataItem
import java.util.*
import kotlin.concurrent.schedule


/**
 * Created by linma9 on 1/23/18.
 */

class LoadingDelegateAdapter : ViewTypeDelegateAdapter {

    var showLoading: Boolean = true

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        ////Log.w("eee888", "+++ +++ LoadingDelegateAdapter:onBindViewHolder(), item:"+item+", holder:"+holder)
        (holder as LoadingViewHolder).bind(showLoading)
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
        fun bind(show: Boolean) = with(itemView) {
            itemView?.visibility = if (show) View.VISIBLE else View.GONE

            if (show) {
               try {
                   Handler().postDelayed({
                       itemView?.visibility = View.GONE
                   }, 3000)
               } catch(e: Exception){

               }
            }
        }
    }
}