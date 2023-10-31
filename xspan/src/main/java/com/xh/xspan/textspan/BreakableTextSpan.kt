package com.xh.xspan.textspan

import android.text.Spannable
import android.text.SpannableString

/**
 * 不具有整体性的TextSpan
 * 可以被逐步删除，不完整时将丢失样式变回普通文本
 */
abstract class BreakableTextSpan : TextSpan() {

    final override fun getDisplaySpannableString(): SpannableString {
        val ss = SpannableString(displayText)
        //设置样式
        ss.setSpan(displaySpan, 0, ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //设置Breakable标识
        ss.setSpan(this, 0, ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    internal fun checkAndRemoveIfNeed(text: Spannable) {
        val spanStart = text.getSpanStart(this)
        val spanEnd = text.getSpanEnd(this)

        if (displayText != text.subSequence(spanStart, spanEnd).toString()) {
            text.removeSpan(displaySpan)
            text.removeSpan(this)
        }
    }
}