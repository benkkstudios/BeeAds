package com.benkkstudios.ads.shared

import android.app.Activity
import android.content.Context
import android.util.Log

internal fun Int.dpToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
fun Context.getActivity(): Activity? = try {
    this as Activity
} catch (e: Exception) {
    null
}

fun adLogging(value: Any) {
    Log.e("BENKKSTUDIOS : ", "BENKKSTUDIOS : $value")
}
