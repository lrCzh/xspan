package com.xh.xspan

import kotlin.math.max

fun XSpanEditText.insertTextSpan(textSpan: TextSpan) {
    val selStart = max(0, selectionStart)
    val dss = textSpan.getDisplaySpannableString()
    text?.insert(selStart, dss)
}