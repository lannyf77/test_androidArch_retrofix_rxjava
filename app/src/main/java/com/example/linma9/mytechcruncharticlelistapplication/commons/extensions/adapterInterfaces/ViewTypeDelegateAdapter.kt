package com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by linma9 on 1/23/18.
 */

interface ViewTypeDelegateAdapter {

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}