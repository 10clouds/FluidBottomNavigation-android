package com.tenclouds.fluidbottomnavigation

import android.animation.AnimatorSet
import android.view.View
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.tenclouds.fluidbottomnavigation.extension.translationYAnimator
import kotlinx.android.synthetic.main.item.view.*

internal fun View.animateSelectItemView() =
        AnimatorSet()
                .apply {
                    playTogether(
                            circle.selectAnimator,
                            icon.selectAnimator,
                            title.selectAnimator,
                            rectangle.selectAnimator,
                            topContainer.selectAnimator)
                }
                .start()

internal fun View.animateDeselectItemView() =
        AnimatorSet()
                .apply {
                    playTogether(
                            circle.deselectAnimator,
                            icon.deselectAnimator,
                            title.deselectAnimator,
                            rectangle.deselectAnimator,
                            topContainer.deselectAnimator)
                }
                .start()

internal fun View.animateShow() =
        AnimatorSet()
                .apply {
                    play(translationYAnimator(
                            height.toFloat(),
                            0f,
                            3 * KEY_FRAME_IN_MS,
                            LinearOutSlowInInterpolator()))
                }
                .start()

internal fun View.animateHide() =
        AnimatorSet()
                .apply {
                    play(translationYAnimator(
                            0f,
                            height.toFloat(),
                            3 * KEY_FRAME_IN_MS,
                            LinearOutSlowInInterpolator()))
                }
                .start()