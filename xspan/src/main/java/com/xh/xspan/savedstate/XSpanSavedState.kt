package com.xh.xspan.savedstate

import android.os.Parcel
import android.os.Parcelable
import android.view.View.BaseSavedState
import androidx.core.os.ParcelCompat

internal class XSpanSavedState : BaseSavedState {

    var spanInfoList: MutableList<TextSpanInfo> = mutableListOf()

    constructor(superState: Parcelable?) : super(superState)

    constructor(parcel: Parcel) : super(parcel) {
        ParcelCompat.readList(parcel, spanInfoList, TextSpanInfo::class.java.classLoader, TextSpanInfo::class.java)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeList(spanInfoList)
    }

    companion object CREATOR : Parcelable.Creator<XSpanSavedState> {
        override fun createFromParcel(parcel: Parcel): XSpanSavedState {
            return XSpanSavedState(parcel)
        }

        override fun newArray(size: Int): Array<XSpanSavedState?> {
            return arrayOfNulls(size)
        }
    }
}