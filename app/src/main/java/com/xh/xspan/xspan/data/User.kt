package com.xh.xspan.xspan.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val id: Int, val name: String) : Parcelable