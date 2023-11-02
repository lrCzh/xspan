package com.xh.xspan.xspan.span

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.xh.xspan.textspan.TextSpan
import kotlinx.parcelize.Parcelize

@Parcelize
class ToySpan : TextSpan() {

    /**
     * 要展示的文本内容
     */
    override val displayText: String
        get() = "&HHH "

    /**
     * 要展示的样式
     * 注！！！，实现时只能以直接赋值的方式，不能以自定义属性访问器的方式
     */
    override val displaySpan = ForegroundColorSpan(Color.GREEN)
}