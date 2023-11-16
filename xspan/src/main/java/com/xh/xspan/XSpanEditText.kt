package com.xh.xspan

import android.content.Context
import android.os.Parcelable
import android.text.NoCopySpan
import android.text.Selection
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatEditText
import com.xh.xspan.savedstate.TextSpanInfo
import com.xh.xspan.savedstate.XSpanSavedState
import com.xh.xspan.textspan.IntegratedSpan
import com.xh.xspan.textspan.TextSpan
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
            val integratedSpans =
                editable.getSpans(selectionStart, selectionEnd, IntegratedSpan::class.java)
            integratedSpans?.find { editable.getSpanEnd(it) == selectionStart }?.let {
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

    /**
     * 插入 TextSpan 到光标位置
     */
    fun insertTextSpan(textSpan: TextSpan) {
        val selStart = max(0, selectionStart)
        val dss = textSpan.getDisplaySpannableString()
        text?.insert(selStart, dss)
    }

    /**
     * 根据传入的类类型获取对应的 Span 列表
     */
    fun <T : TextSpan> getSpans(clazz: Class<T>): List<T> {
        val editable = text ?: return emptyList()
        return editable.getSpans(0, editable.length, clazz).toList()
    }

    /**
     * 重写 onSaveInstanceState 来保存 TextSpan 的相关信息
     */
    override fun onSaveInstanceState(): Parcelable? {
        val editable = text ?: return super.onSaveInstanceState()

        val textSpanArray = editable.getSpans(0, editable.length, TextSpan::class.java)
        val spanInfoList = textSpanArray.map { textSpan ->
            TextSpanInfo(
                textSpan,
                editable.getSpanStart(textSpan),
                editable.getSpanEnd(textSpan),
                editable.getSpanFlags(textSpan)
            )
        }.toMutableList()

        val superState = super.onSaveInstanceState()
        val savedState = XSpanSavedState(superState)
        savedState.spanInfoList = spanInfoList
        return savedState
    }

    /**
     * 重写 onRestoreInstanceState 来恢复 TextSpan 的相关信息
     */
    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is XSpanSavedState -> {
                super.onRestoreInstanceState(state.superState)
                val editable = text ?: return
                val spanInfoList = state.spanInfoList

                spanInfoList.forEach { info ->

                    val existingSpans = editable.getSpans(info.spanStart, info.spanEnd, Any::class.java)
                    existingSpans.forEach { span ->
                        if (editable.getSpanStart(span) == info.spanStart &&
                            editable.getSpanEnd(span) == info.spanEnd &&
                            editable.getSpanFlags(span) == info.spanFlags
                        ) {
                            editable.removeSpan(span)
                        }
                    }

                    // 根据[TextSpan.getDisplaySpannableString]我们可以知道，
                    // TextSpan 成员变量 displaySpan 的 spanStart、spanEnd、spanFlags 与 TextSpan 是一致的
                    editable.setSpan(info.span.displaySpan, info.spanStart, info.spanEnd, info.spanFlags)
                    editable.setSpan(info.span, info.spanStart, info.spanEnd, info.spanFlags)
                }
            }

            else -> {
                super.onRestoreInstanceState(state)
            }
        }
    }
}