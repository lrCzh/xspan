package com.xh.xspan.xspan.span

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.xh.xspan.UnBreakableTextSpan
import com.xh.xspan.xspan.data.Topic

class TopicSpan(val topic: Topic) : UnBreakableTextSpan() {

    override val displayText = "#${topic.name} "

    override val displaySpan = ForegroundColorSpan(Color.RED)
}