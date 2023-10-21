package com.xh.xspan

import android.text.Editable
import android.text.NoCopySpan
import android.text.SpannableStringBuilder
import android.text.Spanned

class XSpanEditableFactory(private val spans: List<NoCopySpan>) : Editable.Factory() {

    override fun newEditable(source: CharSequence): Editable {
        val builder = SpannableStringBuilder(source)
        for (span in spans) {
            builder.setSpan(
                span, 0, source.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE or Spanned.SPAN_PRIORITY
            )
        }
        return builder
    }
}