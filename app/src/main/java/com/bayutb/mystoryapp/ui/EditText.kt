package com.bayutb.mystoryapp.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bayutb.mystoryapp.R

class EditText: AppCompatEditText, View.OnTouchListener {

    private lateinit var errorImageLow : Drawable
    private lateinit var errorImageMedium : Drawable
    private lateinit var errorImageExcellent : Drawable


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
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        errorImageLow = ContextCompat.getDrawable(context, R.drawable.baseline_power_input_low) as Drawable
        errorImageMedium = ContextCompat.getDrawable(context, R.drawable.baseline_power_input_medium) as Drawable
        errorImageExcellent = ContextCompat.getDrawable(context, R.drawable.baseline_power_input_excellent) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) errorNotice()
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        errorNotice()
        return false
    }

    private fun errorNotice() {
        val counts = text?.length
        if (counts != null) {
            if (counts < 8) {
                setButtonDrawables(null,null,errorImageLow, null)
            } else if (counts > 12) {
                setButtonDrawables(null,null,errorImageExcellent, null)
            } else {
                setButtonDrawables(null,null,errorImageMedium, null)
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
}