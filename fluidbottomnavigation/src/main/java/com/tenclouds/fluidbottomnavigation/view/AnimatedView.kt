package com.tenclouds.fluidbottomnavigation.view

import android.animation.Animator
import android.content.Context
import com.tenclouds.fluidbottomnavigation.R

internal interface AnimatedView {

    val selectAnimator: Animator
    val deselectAnimator: Animator

    fun cancelDeselectAnimationAndResetState()

    fun getItemYTransitionYValue(context: Context) =
            -(context.resources?.getDimension(R.dimen.fluidBottomNavigationItemTranslationY) ?: 0f)
}