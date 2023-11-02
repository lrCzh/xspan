package com.xh.xspan

import android.text.Selection
import android.text.SpanWatcher
import android.text.Spannable
import com.xh.xspan.textspan.IntegratedSpan
import com.xh.xspan.textspan.TextSpan
import kotlin.math.abs

/**
 * Span变化监听
 * 1、监听光标的变化并调整光标的位置，不让光标落在 IntegratedSpan 内部
 * 2、完整性监听，未标记为 IntegratedSpan 的 TextSpan 将在不完整时移除样式效果
 */
class XSpanWatcher : SpanWatcher {

    override fun onSpanAdded(text: Spannable?, what: Any?, start: Int, end: Int) {}

    override fun onSpanRemoved(text: Spannable?, what: Any?, start: Int, end: Int) {}

    override fun onSpanChanged(
        text: Spannable?, what: Any?, ostart: Int, oend: Int, nstart: Int, nend: Int
    ) {
        if (what === Selection.SELECTION_START && ostart != nstart) {
            text?.getSpans(nstart, nend, IntegratedSpan::class.java)?.firstOrNull()?.also {
                val spanStart = text.getSpanStart(it)
                val spanEnd = text.getSpanEnd(it)
                val index =
                    if (abs(nstart - spanEnd) > abs(nstart - spanStart)) spanStart else spanEnd
                Selection.setSelection(text, index, Selection.getSelectionEnd(text))
            }
        }
        if (what === Selection.SELECTION_END && ostart != nstart) {
            text?.getSpans(nstart, nend, IntegratedSpan::class.java)?.firstOrNull()?.also {
                val spanStart = text.getSpanStart(it)
                val spanEnd = text.getSpanEnd(it)
                val index =
                    if (abs(nstart - spanEnd) > abs(nstart - spanStart)) spanStart else spanEnd
                Selection.setSelection(text, Selection.getSelectionStart(text), index)
            }
        }

        val s = text ?: return
        if (what is TextSpan && what !is IntegratedSpan) {
            what.checkIntegratedAndRemoveSpanIfNeed(s)
        }
    }
}