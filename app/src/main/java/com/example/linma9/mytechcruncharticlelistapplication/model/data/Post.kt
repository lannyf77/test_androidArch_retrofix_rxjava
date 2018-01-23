package com.example.linma9.mytechcruncharticlelistapplication.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by linma9 on 1/23/18.
 */

class Post {

    @SerializedName("ID")
    @Expose
    var ID: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("author")
    @Expose
    var author: Author? = null
    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("modified")
    @Expose
    var modified : String? = null

    @SerializedName("URL")
    @Expose
    var url: String? = null
    @SerializedName("short_URL")
    @Expose
    var shortURL: String? = null

//        @SerializedName("content")
//        @Expose
//        private var content: String? = null

    @SerializedName("excerpt")
    @Expose
    var excerpt: String? = null

    @SerializedName("featured_image")
    @Expose
    var featured_image: String? = null

    ///
    var categories: Map<String, Category>? = null

}