package com.benkkstudios.ads.yandex

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRatingBar
import com.yandex.mobile.ads.nativeads.Rating

internal class RatingView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.ratingBarStyle
) : AppCompatRatingBar(context, attrs, defStyleAttr), Rating {

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        androidx.appcompat.R.attr.ratingBarStyle
    )
}