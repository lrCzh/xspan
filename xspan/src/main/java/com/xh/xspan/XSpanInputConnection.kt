package com.xh.xspan

import android.view.KeyEvent
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper

class XSpanInputConnection(
    target: InputConnection,
    mutable: Boolean,
    private val onCommitText: ((CharSequence?) -> Boolean),
    private val onKeyEvent: ((KeyEvent?) -> Boolean)
) : InputConnectionWrapper(target, mutable) {

    override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
        val isIntercepted = onCommitText.invoke(text)
        if (isIntercepted) return false
        return super.commitText(text, newCursorPosition)
    }

    override fun sendKeyEvent(event: KeyEvent?): Boolean {
        return onKeyEvent(event) || super.sendKeyEvent(event)
    }

    override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
        if (beforeLength == 1 && afterLength == 0) {
            return onKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) ||
                    super.deleteSurroundingText(beforeLength, afterLength)
        }
        return super.deleteSurroundingText(beforeLength, afterLength)
    }
}