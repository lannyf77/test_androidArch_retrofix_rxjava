package com.example.linma9.mytechcruncharticlelistapplication.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * Created by linma9 on 1/23/18.
 */

@Entity(tableName = "post", indices = arrayOf(Index(value = "postID", unique = true)))
class DbPost (

    @ColumnInfo(name = "postID")
    var postId: Int? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "authorId")
    var authorId: Int? = null,

    @ColumnInfo(name = "date")
    var date: String? = null,

    @ColumnInfo(name = "modified")
    var modified : String? = null,

    @ColumnInfo(name = "url")
    var url: String? = null,

    @ColumnInfo(name = "shortURL")
    var shortURL: String? = null,

//        private var content: String? = null

    @ColumnInfo(name = "excerpt")
    var excerpt: String? = null,

    @ColumnInfo(name = "featured_image")
    var featured_image: String? = null,

    @ColumnInfo(name = "categories")
    var categories: String? = null

) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate=true)
    var id: Int? = null

    override fun toString() : String {
        return "dbPost: id:"+id+", authorid:"+authorId+", title:"+title+", excerpt:"+excerpt+", url:"+url;
    }
}



//@Entity(tableName = "post")
//CTData class DbPost(
//        @ColumnInfo(name = "title")
//        var title: String? = null,
//        @ColumnInfo(name = "authorId")
//        var authorId: String? = null,
//        @ColumnInfo(name = "date")
//        var date: String? = null,
//        @ColumnInfo(name = "modified")
//        var modified : String? = null,
//        @ColumnInfo(name = "url")
//        var url: String? = null,
//        @ColumnInfo(name = "shortURL")
//        var shortURL: String? = null,
//        @ColumnInfo(name = "excerpt")
//        var excerpt: String? = null,
//
//        @ColumnInfo(name = "featured_image")
//        var featured_image: String?=null,
//
//        @ColumnInfo(name = "categories")
//        var categories: String) {
//    @ColumnInfo(name = "id")
//    @PrimaryKey(autoGenerate = true) var id: Long = 0
//}