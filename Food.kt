package com.example.blingo

import android.os.Parcel
import android.os.Parcelable

data class food(val image: Int, val name: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(image)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<food> {
        override fun createFromParcel(parcel: Parcel): food {
            return food(parcel)
        }

        override fun newArray(size: Int): Array<food?> {
            return arrayOfNulls(size)
        }
    }
}
