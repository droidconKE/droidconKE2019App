package droiddevelopers254.droidconke.ui.filters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import com.google.android.material.chip.Chip
import droiddevelopers254.droidconke.R

class FilterChip @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : Chip(context, attrs, defStyle) {

    init {
        isCheckable = true
        isClickable = true
        isCheckedIconVisible = false
        isCloseIconVisible = false
        closeIconTint = ColorStateList.valueOf(Color.WHITE)
        setTextColor(Color.WHITE)
        setChipBackgroundColorResource(R.color.selector_chip_background)
    }

    fun disable() {
        isCheckable = false
        isClickable = false
    }

}