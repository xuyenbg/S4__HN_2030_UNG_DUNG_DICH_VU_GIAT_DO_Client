package datn.fpoly.myapplication.utils

import android.view.View
import kotlin.math.abs
import kotlin.math.max

class ZoomOutPageTransformer : BaseTransformer() {

    override fun transformPage(view: View, position: Float) {
        val MIN_SCALE = 0.8f
        val MIN_ALPHA = 0.5f

        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }

                position <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horiMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horiMargin - vertMargin / 2
                    } else {
                        horiMargin + vertMargin / 2
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade the page relative to its size.
                    alpha =
                        (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }

                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}