package com.tenclouds.fluidbottomnavigation.view

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.tenclouds.fluidbottomnavigation.KEY_FRAME_IN_MS
import com.tenclouds.fluidbottomnavigation.extension.alphaAnimator
import com.tenclouds.fluidbottomnavigation.extension.interpolators
import com.tenclouds.fluidbottomnavigation.extension.translationYAnimator

internal class TitleView @JvmOverloads constructor(context: Context,
                                                   attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0)
    : AppCompatTextView(context, attrs, defStyleAttr), AnimatedView {

    override val selectAnimator by lazy {
        AnimatorSet()
                .apply {
                    playTogether(
                            selectMoveAnimator,
                            selectAlphaAnimator)
                }
    }

    override val deselectAnimator by lazy {
        AnimatorSet()
                .apply {
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
                                        getItemOvershootTransitionYValue(context),
                                        7 * KEY_FRAME_IN_MS,
                                        interpolators[0]),
                                translationYAnimator(
                                        getItemOvershootTransitionYValue(context),
                                        getItemTransitionYValue(context),
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
                                        getItemTransitionYValue(context),
                                        getItemOvershootTransitionYValue(context),
                                        3 * KEY_FRAME_IN_MS,
                                        interpolators[4]),
                                translationYAnimator(
                                        getItemOvershootTransitionYValue(context),
                                        0f,
                                        11 * KEY_FRAME_IN_MS,
                                        interpolators[0]))
                        startDelay = 4 * KEY_FRAME_IN_MS
                    }

    private val deselectAlphaAnimator =
            AnimatorSet()
                    .apply {
                        play(alphaAnimator(1f, 0f, 8 * KEY_FRAME_IN_MS, LinearOutSlowInInterpolator()))
                        startDelay = 7 * KEY_FRAME_IN_MS
                    }
}