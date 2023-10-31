package com.xh.xspan

import android.content.Context
import android.text.NoCopySpan
import android.text.Selection
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatEditText
import com.xh.xspan.textspan.TextSpan
import com.xh.xspan.textspan.UnBreakableTextSpan
import kotlin.math.max

class XSpanEditText : AppCompatEditText {

    private var specialCharArr = charArrayOf()

    private var interceptSpecialChar = false

    private var onSpecialCharInputAction: ((Char) -> Unit)? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initSpecialChatArr(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initSpecialChatArr(attrs)
    }

    init {
        val watchers = mutableListOf<NoCopySpan>().apply {
            add(XSpanWatcher())
        }
        setEditableFactory(XSpanEditableFactory(watchers))
        setOnKeyListener { _, _, event -> handleKeyEvent(event) }
    }

    private fun initSpecialChatArr(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.XSpanEditText).apply {
            getString(R.styleable.XSpanEditText_specialCharSet)?.let {
                specialCharArr = it.toCharArray()
            }
            getBoolean(R.styleable.XSpanEditText_interceptSpecialChar, false).let {
                interceptSpecialChar = it
            }
        }.recycle()
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        var inputConnection = super.onCreateInputConnection(outAttrs)
        if (null != inputConnection) {
            inputConnection = XSpanInputConnection(target = inputConnection,
                mutable = true,
                onCommitText = { text -> handleCommitText(text) },
                onKeyEvent = { keyEvent -> handleKeyEvent(keyEvent) })
        }
        return inputConnection
    }

    private fun handleCommitText(text: CharSequence?): Boolean {
        specialCharArr.find { "$it" == text }?.let { specialChar ->
            onSpecialCharInputAction?.let { action ->
                action.invoke(specialChar)
                return interceptSpecialChar
            }
        }
        return false
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
            unBreakableTextSpans?.find { editable.getSpanEnd(it) == selectionStart }?.let {
                val spanStart = editable.getSpanStart(it)
                val spanEnd = editable.getSpanEnd(it)
                editable.replace(spanStart, spanEnd, "")
                return true
            }
        }
        return false
    }

    fun setOnSpecialCharInputAction(action: (Char) -> Unit) {
        this.onSpecialCharInputAction = action
    }

    fun insertTextSpan(textSpan: TextSpan) {
        val selStart = max(0, selectionStart)
        val dss = textSpan.getDisplaySpannableString()
        text?.insert(selStart, dss)
    }
}