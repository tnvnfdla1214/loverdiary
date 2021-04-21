package com.example.loverdiary.data

import android.os.Parcel
import android.os.Parcelable

data class Notes(
    var notesTitle: String? = "",
    var notesTitleImage: String? = "",
    var notesPageCount: Int? = 0,
    var notesUid: String? = "",
    var notesDate: String?
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notesTitle)
        parcel.writeString(notesTitleImage)
        // parcel.writeInt(notesPageCount!!) 아니면 !! 지우고 다른 방법 하기
        parcel.writeInt(notesPageCount!!)
        parcel.writeString(notesUid)
        parcel.writeString(notesDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notes> {
        override fun createFromParcel(parcel: Parcel): Notes {
            return Notes(parcel)
        }

        override fun newArray(size: Int): Array<Notes?> {
            return arrayOfNulls(size)
        }
    }
}
