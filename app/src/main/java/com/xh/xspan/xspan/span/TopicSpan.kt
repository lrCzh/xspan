package com.xh.xspan.xspan.span

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.xh.xspan.UnBreakableTextSpan

class TopicSpan(val topicName: String, val topicId: Int = 0) : UnBreakableTextSpan() {

    override val displayText = "#$topicName "

    override val displaySpan = ForegroundColorSpan(Color.BLUE)
}