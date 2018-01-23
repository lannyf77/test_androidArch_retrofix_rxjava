package com.example.linma9.mytechcruncharticlelistapplication.model.data

import android.arch.persistence.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Author {

    @SerializedName("ID")
    @Expose
    var id: Int? = null

    @SerializedName("login")
    @Expose
    @ColumnInfo(name = "login")
    var login: String? = null

    @SerializedName("email")
    @Expose
    @ColumnInfo(name = "email")
    var email: Boolean? = null

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    var name: String? = null

    @SerializedName("first_name")
    @Expose
    @ColumnInfo(name = "firstName")
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    @ColumnInfo(name = "lastName")
    var lastName: String? = null

    @SerializedName("nice_name")
    @Expose
    @ColumnInfo(name = "niceName")
    var niceName: String? = null

    @SerializedName("URL")
    @Expose
    @ColumnInfo(name = "url")
    var url: String? = null

    @SerializedName("avatar_URL")
    @Expose
    @ColumnInfo(name = "avatarURL")
    var avatarURL: String? = null

    @SerializedName("profile_URL")
    @Expose
    @ColumnInfo(name = "profileURL")
    var profileURL: String? = null

    @SerializedName("site_ID")
    @Expose
    @ColumnInfo(name = "siteID")
    var siteID: Int? = null


    override fun toString() : String {
        return "id:"+id+", firstName:"+firstName+", lastName:"+lastName+", avatarURL:"+avatarURL;
    }
}