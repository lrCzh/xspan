package com.xh.xspan

import android.text.Spannable
import android.text.SpannableString

/**
 * 不具有整体性的TextSpan
 */
abstract class BreakableTextSpan : TextSpan() {

    final override fun getDisplaySpannableString(): SpannableString {
        val ss = SpannableString(displayText)
        //设置样式
        ss.setSpan(displaySpan, 0, ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(this, 0, ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    fun checkAndRemoveIfNeed(text: Spannable) {
        val spanStart = text.getSpanStart(this)
        val spanEnd = text.getSpanEnd(this)

        if (displayText != text.subSequence(spanStart, spanEnd).toString()) {
            text.removeSpan(displaySpan)
            text.removeSpan(this)
        }
    }
}