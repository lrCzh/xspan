package com.xh.xspan.xspan.span

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.xh.xspan.textspan.IntegratedSpan
import com.xh.xspan.textspan.TextSpan
import com.xh.xspan.xspan.data.User
import kotlinx.parcelize.Parcelize

@Parcelize
class AtSpan(val user: User) : TextSpan(), IntegratedSpan {

    /**
     * 要展示的文本内容
     */
    override val displayText = "@${user.name} "

    /**
     * 要展示的样式
     * 注！！！，实现时只能以直接赋值的方式，不能以自定义属性访问器的方式
     */
    override val displaySpan = ForegroundColorSpan(Color.BLUE)
}