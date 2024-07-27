package com.benkkstudios.ads.shared

import android.app.Activity
import android.os.Build
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowMetrics

fun BannerSize.asLayoutParam(activity: Activity): LayoutParams = LayoutParams(MATCH_PARENT, height.dpToPx(activity))

data class BannerSize(
    val width: Int,
    val height: Int
) {
    companion object {
        val BANNER = BannerSize(320, 50)
        val FULL_BANNER = BannerSize(320, 50)
        val LARGE_BANNER = BannerSize(320, 50)
        val LEADERBOARD = BannerSize(320, 50)
        val MEDIUM_RECTANGLE = BannerSize(320, 50)
        var ADAPTIVE = BannerSize(320, 50)

        fun createAdaptive(activity: Activity) {
//            try {
//                val size = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adaptiveWidth(activity))
//                ADAPTIVE = BannerSize(size.width, size.height)
//            } catch (e: ClassNotFoundException) {
//                ADAPTIVE = BANNER
//            }
        }

        private fun adaptiveWidth(activity: Activity): Int = with(activity) {
            val displayMetrics = resources.displayMetrics
            val adWidthPixels = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
                windowMetrics.bounds.width()
            } else {
                displayMetrics.widthPixels
            }
            val density = displayMetrics.density
            (adWidthPixels / density).toInt()
        }

    }
}