package com.tenclouds.fluidbottomnavigation.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.tenclouds.fluidbottomnavigation.KEY_FRAME_IN_MS
import com.tenclouds.fluidbottomnavigation.extension.*

class IconView @JvmOverloads constructor(context: Context,
                                         attrs: AttributeSet? = null,
                                         defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr), AnimatedView {

    init {
        scaleX = 0.9f
        scaleY = 0.9f
    }

    var selectColor = 0
    var deselectColor = 0

    override val selectAnimator by lazy {
        AnimatorSet()
                .apply {
                    playTogether(
                            selectScaleAnimator,
                            selectMoveAnimator,
                            selectTintAnimator)
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) = Unit
                        override fun onAnimationEnd(animation: Animator?) = Unit
                        override fun onAnimationCancel(animation: Animator?) = Unit
                        override fun onAnimationStart(animation: Animator?) = cancelDeselectAnimationAndResetState()
                    })
                }
    }

    override val deselectAnimator by lazy {
        AnimatorSet()
                .apply {
                    playTogether(
                            deselectScaleAnimator,
                            deselectMoveAnimator,
                            deselectTintAnimator)
                }
    }

    override fun cancelDeselectAnimationAndResetState() {
        deselectAnimator.end()
        scaleX = 0.9f
        scaleY = 0.9f
        translationY = 0f
        setTintColor(selectColor)
    }

    private val selectScaleAnimator =
            AnimatorSet()
                    .apply {
                        playSequentially(
                                scaleAnimator(0.9f, 1.1f, 7 * KEY_FRAME_IN_MS, interpolators[0]),
                                scaleAnimator(1.1f, 0.84f, 4 * KEY_FRAME_IN_MS, interpolators[0]),
                                scaleAnimator(0.84f, 0.9f, 4 * KEY_FRAME_IN_MS, interpolators[3]))
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

    private val selectTintAnimator by lazy {
        AnimatorSet()
                .apply {
                    play(tintAnimator(
                            deselectColor,
                            selectColor,
                            3 * KEY_FRAME_IN_MS))
                }
    }

    private val deselectScaleAnimator =
            AnimatorSet()
                    .apply {
                        playSequentially(
                                scaleAnimator(0.9f, 0.84f, 4 * KEY_FRAME_IN_MS, interpolators[3]),
                                scaleAnimator(0.84f, 1.1f, 4 * KEY_FRAME_IN_MS, interpolators[0]),
                                scaleAnimator(1.1f, 0.9f, 7 * KEY_FRAME_IN_MS, interpolators[0]))
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
                        startDelay = 6 * KEY_FRAME_IN_MS
                    }

    private val deselectTintAnimator by lazy {
        AnimatorSet()
                .apply {
                    play(tintAnimator(
                            selectColor,
                            deselectColor,
                            3 * KEY_FRAME_IN_MS))
                    startDelay = 19 * KEY_FRAME_IN_MS
                }
    }
}
