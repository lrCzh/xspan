package com.xh.xspan.xspan.span

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.xh.xspan.BreakableTextSpan
import com.xh.xspan.UnBreakableTextSpan

class ToySpan : UnBreakableTextSpan() {

    override val displayText = "@ToySpan "

    override val displaySpan = ForegroundColorSpan(Color.RED)
}