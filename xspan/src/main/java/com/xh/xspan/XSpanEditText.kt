package com.xh.xspan

import android.content.Context
import android.text.NoCopySpan
import android.text.Selection
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import androidx.appcompat.widget.AppCompatEditText

class XSpanEditText : AppCompatEditText {

    private var onJumpTopicAction: (() -> Unit)? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val watchers = mutableListOf<NoCopySpan>().apply {
            add(XSpanWatcher())
        }
        setEditableFactory(XSpanEditableFactory(watchers))
        setOnKeyListener { _, _, event -> handleKeyEvent(event) }
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        var inputConnection = super.onCreateInputConnection(outAttrs)
        if (null != inputConnection) {
            inputConnection = CustomInputConnectionWrapper(inputConnection, true)
        }
        return inputConnection
    }

    private fun handleKeyEvent(event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
            val editable = text ?: return false
            val selectionStart = Selection.getSelectionStart(editable)
            val selectionEnd = Selection.getSelectionEnd(editable)
            if (selectionStart != selectionEnd) {
                return false
            }
            val unBreakableTextSpans =
                editable.getSpans(selectionStart, selectionEnd, UnBreakableTextSpan::class.java)
            unBreakableTextSpans?.firstOrNull { editable.getSpanEnd(it) == selectionStart }?.let {
                val spanStart = editable.getSpanStart(it)
                val spanEnd = editable.getSpanEnd(it)
                editable.replace(spanStart, spanEnd, "")
                return true
            }
        }
        return false
    }

    fun setOnJumpTopicAction(action: () -> Unit) {
        this.onJumpTopicAction = action
    }

    private inner class CustomInputConnectionWrapper(target: InputConnection, mutable: Boolean) :
        InputConnectionWrapper(target, mutable) {

        override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
            onJumpTopicAction?.let {
                if ("#" == text) {
                    it.invoke()
                    return false
                }
            }
            return super.commitText(text, newCursorPosition)
        }

        override fun sendKeyEvent(event: KeyEvent?): Boolean {
            return handleKeyEvent(event) || super.sendKeyEvent(event)
        }

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            if (beforeLength == 1 && afterLength == 0) {
                return handleKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) ||
                        super.deleteSurroundingText(beforeLength, afterLength)
            }
            return super.deleteSurroundingText(beforeLength, afterLength)
        }
    }
}