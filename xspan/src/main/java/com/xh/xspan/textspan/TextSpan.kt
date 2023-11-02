package com.xh.xspan.textspan

import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString

/**
 * 文本 Span 抽象基类
 */
abstract class TextSpan : Parcelable {

    /**
     * 要展示的文本内容
     */
    abstract val displayText: String

    /**
     * 要展示的样式
     * 注！！！，实现时只能以直接赋值的方式，不能以自定义属性访问器的方式
     */
    abstract val displaySpan: Parcelable

    /**
     * 获取设置好 Span 后的富文本内容
     */
    fun getDisplaySpannableString(): SpannableString {
        val ss = SpannableString(displayText)
        ss.setSpan(displaySpan, 0, ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(this, 0, ss.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 检测 Span 对应文本完整性，如果不完整则从 text 上移除掉相关的 Span
     */
    fun checkIntegratedAndRemoveSpanIfNeed(text: Spannable) {
        val spanStart = text.getSpanStart(this)
        val spanEnd = text.getSpanEnd(this)
        if (displayText != text.subSequence(spanStart, spanEnd).toString()) {
            text.removeSpan(displaySpan)
            text.removeSpan(this)
        }
    }
}