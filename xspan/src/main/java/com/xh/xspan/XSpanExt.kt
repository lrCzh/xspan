package com.xh.xspan

import android.text.SpannableString

fun XSpanEditText.insertSpannableString(ss: SpannableString) {
    if (selectionStart < 0) {
        text?.insert(0, ss)
    } else {
        text?.insert(selectionStart, ss)
    }
}