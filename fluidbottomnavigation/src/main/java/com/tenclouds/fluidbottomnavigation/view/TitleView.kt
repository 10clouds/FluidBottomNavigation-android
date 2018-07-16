package com.tenclouds.fluidbottomnavigation.view

import android.animation.AnimatorSet
import android.content.Context
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.AttributeSet
import android.widget.TextView
import com.tenclouds.fluidbottomnavigation.KEY_FRAME_IN_MS
import com.tenclouds.fluidbottomnavigation.extension.alphaAnimator
import com.tenclouds.fluidbottomnavigation.extension.interpolators
import com.tenclouds.fluidbottomnavigation.extension.translationYAnimator

class TitleView @JvmOverloads constructor(context: Context,
                                          attrs: AttributeSet? = null,
                                          defStyleAttr: Int = 0)
    : TextView(context, attrs, defStyleAttr), AnimatedView {

    override val selectAnimator by lazy {
        AnimatorSet().apply {
            playTogether(
                    selectMoveAnimator,
                    selectAlphaAnimator)
        }
    }

    override val deselectAnimator by lazy {
        AnimatorSet().apply {
            playTogether(
                    deselectMoveAnimator,
                    deselectAlphaAnimator)
        }
    }

    private val selectMoveAnimator =
            AnimatorSet()
                    .apply {
                        playSequentially(
                                translationYAnimator(
                                        0f,
                                        getItemYTransitionYValue(context) * 11 / 10,
                                        7 * KEY_FRAME_IN_MS,
                                        interpolators[0]),
                                translationYAnimator(
                                        getItemYTransitionYValue(context) * 11 / 10,
                                        getItemYTransitionYValue(context),
                                        3 * KEY_FRAME_IN_MS,
                                        interpolators[4]))
                        startDelay = 11 * KEY_FRAME_IN_MS
                    }

    private val selectAlphaAnimator =
            AnimatorSet()
                    .apply {
                        play(alphaAnimator(0f, 1f, 8 * KEY_FRAME_IN_MS, LinearOutSlowInInterpolator()))
                        startDelay = 14 * KEY_FRAME_IN_MS
                    }


    private val deselectMoveAnimator =
            AnimatorSet()
                    .apply {
                        playSequentially(
                                translationYAnimator(
                                        getItemYTransitionYValue(context),
                                        getItemYTransitionYValue(context) * 11 / 10,
                                        3 * KEY_FRAME_IN_MS,
                                        interpolators[4]),
                                translationYAnimator(
                                        getItemYTransitionYValue(context) * 11 / 10,
                                        0f,
                                        7 * KEY_FRAME_IN_MS,
                                        interpolators[0]))
                    }

    private val deselectAlphaAnimator =
            AnimatorSet()
                    .apply {
                        play(alphaAnimator(1f, 0f, 8 * KEY_FRAME_IN_MS, LinearOutSlowInInterpolator()))
                        startDelay = 1 * KEY_FRAME_IN_MS
                    }
}