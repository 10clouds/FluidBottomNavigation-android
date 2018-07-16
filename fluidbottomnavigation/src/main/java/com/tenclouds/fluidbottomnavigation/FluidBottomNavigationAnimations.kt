package com.tenclouds.fluidbottomnavigation

import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.LinearOutSlowInInterpolator


internal fun FluidBottomNavigation.animateShow() =
        ViewCompat.animate(this)
                .translationY(0f)
                .setInterpolator(LinearOutSlowInInterpolator())
                .setDuration(HIDE_ANIMATION_DURATION.toLong())
                .start()

internal fun FluidBottomNavigation.animateHide() =
        ViewCompat.animate(this)
                .translationY(height?.toFloat() ?: 0.0f)
                .setInterpolator(LinearOutSlowInInterpolator())
                .setDuration(HIDE_ANIMATION_DURATION.toLong())
                .start()
