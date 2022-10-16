package com.atharianr.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.atharianr.storyapp.R

class AutoCheckEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (inputType) {
            PERSON_NAME -> {
                hint = context.getString(R.string.user_name)
            }
            EMAIL -> {
                hint = context.getString(R.string.email)
            }
            PASSWORD -> {
                hint = context.getString(R.string.password)
            }
        }
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing.
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when (inputType) {
                    EMAIL -> {
                        if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                            showIndicator(context.getString(R.string.email_error_info))
                        } else {
                            hideIndicator()
                        }
                    }
                    PASSWORD -> {
                        if ((text?.length ?: 0) < 6) {
                            showIndicator(context.getString(R.string.password_error_info))
                        } else {
                            hideIndicator()
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // do nothing.
            }
        })
    }

    private fun showIndicator(message: String) {
        error = message
    }

    private fun hideIndicator() {
        error = null
    }

    companion object {
        const val PASSWORD = 129
        const val EMAIL = 33
        const val PERSON_NAME = 97
    }
}