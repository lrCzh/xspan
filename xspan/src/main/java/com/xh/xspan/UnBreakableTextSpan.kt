package com.xh.xspan

import android.text.Spannable
import android.text.SpannableString

/**
 * 具有整体性的TextSpan
 * 只能被整个删除
 */
abstract class UnBreakableTextSpan : TextSpan() {

    final override fun getDisplaySpannableString(): SpannableString {
        val ss = SpannableString(displayText)
        //设置样式
        ss.setSpan(displaySpan, 0, ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //设置UnBreakable标识
        ss.setSpan(this, 0, ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }
}