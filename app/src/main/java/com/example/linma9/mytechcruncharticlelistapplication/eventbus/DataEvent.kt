package com.example.linma9.mytechcruncharticlelistapplication.eventbus

import com.example.linma9.mytechcruncharticlelistapplication.model.data.Posts
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.CTViewDataItem
import java.util.ArrayList

/**
 * Created by linma9 on 1/23/18.
 */

class DataEvent {

    companion object {
        val EVENT_TYPE_UNKNOWN = -1
        val EVENT_TYPE_POSTS_DATA = 1
        val EVENT_TYPE_TCDATA = 2
        val EVENT_TYPE_STRING = 3
    }
    var eventType: Int = EVENT_TYPE_UNKNOWN


    var postsData: Posts? = null

    fun setPostData(d: Posts) {
        postsData = d
        eventType = EVENT_TYPE_POSTS_DATA
    }

    fun getPostData() : Posts? {
        eventType = EVENT_TYPE_POSTS_DATA
        return postsData
    }


    var CTData: ArrayList<CTViewDataItem>? = null

    fun setEnentData(d: ArrayList<CTViewDataItem>) {
        CTData = d
        eventType = EVENT_TYPE_TCDATA
    }

    fun getEventData() : ArrayList<CTViewDataItem>? {
        eventType = EVENT_TYPE_TCDATA
        return CTData
    }



    ////
    var strMessage: String = ""
    fun setStringMessage(d: String) {
        strMessage = d
        eventType = EVENT_TYPE_STRING
    }

    fun getStringMessage() : String {
        return strMessage
    }
}