package com.xh.xspan.xspan.span

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.xh.xspan.BreakableTextSpan

class ToySpan(val toy: String) : BreakableTextSpan() {

    override val displayText = "&$toy "

    override val displaySpan = ForegroundColorSpan(Color.GREEN)
}