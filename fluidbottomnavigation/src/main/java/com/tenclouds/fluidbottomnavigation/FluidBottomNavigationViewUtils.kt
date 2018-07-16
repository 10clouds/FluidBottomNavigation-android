package com.tenclouds.fluidbottomnavigation

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

@SuppressLint("NewApi", "ResourceType")
@Suppress("NAME_SHADOWING")
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
internal fun FluidBottomNavigation.calculateHeight(layoutHeight: Int): Int {
    var layoutHeight = layoutHeight
    var navigationBarHeight = 0

    resources.getIdentifier("navigation_bar_height", "dimen", "android")
            .let {
                if (it > 0)
                    navigationBarHeight = resources.getDimensionPixelSize(it)
            }

    intArrayOf(android.R.attr.windowTranslucentNavigation)
            .let {
                with(context.theme
                        .obtainStyledAttributes(it)) {
                    val translucentNavigation = getBoolean(0, true)
                    if (hasImmersive(context) && !translucentNavigation) {
                        layoutHeight += navigationBarHeight
                    }
                    recycle()
                }
            }

    return layoutHeight
}


@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
private fun hasImmersive(context: Context): Boolean {
    val displayMetrics = DisplayMetrics()
    val realDisplayMetrics = DisplayMetrics()
    with((context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay) {
        getMetrics(displayMetrics)
        getRealMetrics(realDisplayMetrics)
    }

    val displayHeight = displayMetrics.heightPixels
    val displayWidth = displayMetrics.widthPixels
    val realHeight = realDisplayMetrics.heightPixels
    val realWidth = realDisplayMetrics.widthPixels

    return realWidth > displayWidth || realHeight > displayHeight
}