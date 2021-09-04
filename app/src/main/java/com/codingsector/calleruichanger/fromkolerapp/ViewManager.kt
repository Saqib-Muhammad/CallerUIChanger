package com.codingsector.calleruichanger.fromkolerapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

class ViewManager(private val context: Context) {

    val selectableItemBackgroundBorderlessDrawable: Drawable?
        get() {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                android.R.attr.selectableItemBackgroundBorderless,
                typedValue,
                true
            )
            return ContextCompat.getDrawable(context, typedValue.resourceId)
        }

    fun getSizeInDp(sizeInDp: Int): Int =
        (sizeInDp * context.resources.displayMetrics.density + 0.5f).toInt()

    @ColorInt
    fun getAttrColor(attrRes: Int): Int =
        TypedValue().also { context.theme.resolveAttribute(attrRes, it, true) }.data
}