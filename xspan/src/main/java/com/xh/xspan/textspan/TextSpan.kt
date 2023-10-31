package com.xh.xspan.textspan

import android.text.SpannableString

abstract class TextSpan {

    abstract val displayText: String

    abstract val displaySpan: Any

    abstract fun getDisplaySpannableString(): SpannableString
}