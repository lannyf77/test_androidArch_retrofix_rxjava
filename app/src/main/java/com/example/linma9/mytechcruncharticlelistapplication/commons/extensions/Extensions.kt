package com.example.linma9.mytechcruncharticlelistapplication.commons.extensions

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.linma9.mytechcruncharticlelistapplication.R
import com.squareup.picasso.Picasso

/**
 * Created by linma9 on 1/23/18.
 */

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {

    ////Log.w("eee888", "+++ +++ ViewGroup.inflate(), layoutId:"+layoutId)

    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun ImageView.loadImg(imageUrl: String) {
    if (TextUtils.isEmpty(imageUrl)) {
        Picasso.with(context).load(R.mipmap.ic_launcher).into(this)
    } else {
        Picasso.with(context).load(imageUrl).into(this)
    }
}