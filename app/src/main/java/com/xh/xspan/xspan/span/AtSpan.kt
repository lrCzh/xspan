package com.xh.xspan.xspan.span

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.xh.xspan.textspan.UnBreakableTextSpan
import com.xh.xspan.xspan.data.User

class AtSpan(val user: User) : UnBreakableTextSpan() {

    override val displayText = "@${user.name} "

    override val displaySpan = ForegroundColorSpan(Color.BLUE)
}