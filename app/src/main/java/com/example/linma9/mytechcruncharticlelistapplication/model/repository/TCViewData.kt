package com.example.linma9.mytechcruncharticlelistapplication.model.repository

import android.os.Parcel
import android.os.Parcelable
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewTypeConstants
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewType

/**
 * Created by linma9 on 1/23/18.
 */

/**
 * CTViewDataItem is merged data for ui to comsume
 */

data class CTViewDataItem(
        val postId: Int,
        val author: String,
        val title: String,
        val created: Long,
        val thumbnail: String,
        val url: String?,
        val snippet: String?,
        val categories: String?,
        val authorId: Int
) : ViewType, Parcelable {
    override fun getViewType(): Int {
        return ViewTypeConstants.ARTICL
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(postId)
        parcel.writeString(author)
        parcel.writeString(title)
        parcel.writeLong(created)
        parcel.writeString(thumbnail)
        parcel.writeString(url)
        parcel.writeString(snippet)
        parcel.writeString(categories)
        parcel.writeInt(authorId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CTViewDataItem> {
        override fun createFromParcel(parcel: Parcel): CTViewDataItem {
            return CTViewDataItem(parcel)
        }

        override fun newArray(size: Int): Array<CTViewDataItem?> {
            return arrayOfNulls(size)
        }
    }

}