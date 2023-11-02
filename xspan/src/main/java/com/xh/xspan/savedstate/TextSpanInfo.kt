package com.xh.xspan.savedstate

import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelCompat
import com.xh.xspan.textspan.TextSpan

/**
 * 销毁重建时要保存的状态信息
 */
internal data class TextSpanInfo(
    val span: TextSpan,
    val spanStart: Int,
    val spanEnd: Int,
    val spanFlags: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        ParcelCompat.readParcelable(parcel, TextSpan::class.java.classLoader, TextSpan::class.java)!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(span, flags)
        parcel.writeInt(spanStart)
        parcel.writeInt(spanEnd)
        parcel.writeInt(spanFlags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TextSpanInfo> {
        override fun createFromParcel(parcel: Parcel): TextSpanInfo {
            return TextSpanInfo(parcel)
        }

        override fun newArray(size: Int): Array<TextSpanInfo?> {
            return arrayOfNulls(size)
        }
    }
}