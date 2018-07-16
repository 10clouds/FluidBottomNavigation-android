package com.tenclouds.fluidbottomnavigation

import android.os.Build
import android.view.ViewTreeObserver

@Suppress("DEPRECATION")
fun ViewTreeObserver.removeOnGlobalLayoutListenerCompat(listener: ViewTreeObserver.OnGlobalLayoutListener) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        this.removeOnGlobalLayoutListener(listener)
    } else {
        this.removeGlobalOnLayoutListener(listener)
    }
}