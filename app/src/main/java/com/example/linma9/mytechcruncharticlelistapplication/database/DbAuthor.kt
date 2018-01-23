package com.example.linma9.mytechcruncharticlelistapplication.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by linma9 on 1/23/18.
 */


@Entity(tableName = "author")
class DbAuthor(

        @ColumnInfo(name = "authorid")
        var authorid: Int? = null,

        @ColumnInfo(name = "login")
        var login: String? = null,

        @ColumnInfo(name = "email")
        var email: Boolean? = null,

        @ColumnInfo(name = "name")
        var name: String? = null,

        @ColumnInfo(name = "firstName")
        var firstName: String? = null,

        @ColumnInfo(name = "lastName")
        var lastName: String? = null,

        @ColumnInfo(name = "niceName")
        var niceName: String? = null,

        @ColumnInfo(name = "url")
        var url: String? = null,

        @ColumnInfo(name = "avatarURL")
        var avatarURL: String? = null,

        @ColumnInfo(name = "profileURL")
        var profileURL: String? = null,

        @ColumnInfo(name = "siteID")
        var siteID: Int? = null

) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate=false)
    var id: Int? = null

    override fun toString() : String {
        return "dbAuthor: id:"+id+", authorid:"+authorid+", firstName:"+firstName+", lastName:"+lastName+", avatarURL:"+avatarURL;
    }
}