package com.tenclouds.fluidbottomnavigation

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ImageViewCompat
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.ImageView

internal fun View.visible() {
    this.visibility = View.VISIBLE
}

internal fun View.invisible() {
    this.visibility = View.INVISIBLE
}

internal fun View.gone() {
    this.visibility = View.GONE
}

internal fun View?.getColor(colorResource: Int?) =
        this?.context?.let {
            ContextCompat.getColor(
                    it,
                    colorResource ?: 0)
        } ?: 0

internal fun ImageView.setTintColor(color: Int) =
        ImageViewCompat.setImageTintList(
                this,
                ColorStateList.valueOf(color))

@Suppress("DEPRECATION")
fun ViewTreeObserver.removeOnGlobalLayoutListenerCompat(listener: ViewTreeObserver.OnGlobalLayoutListener) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        this.removeOnGlobalLayoutListener(listener)
    } else {
        this.removeGlobalOnLayoutListener(listener)
    }
}

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