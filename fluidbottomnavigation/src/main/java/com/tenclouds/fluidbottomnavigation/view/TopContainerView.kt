package com.tenclouds.fluidbottomnavigation.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.tenclouds.fluidbottomnavigation.KEY_FRAME_IN_MS
import com.tenclouds.fluidbottomnavigation.R
import com.tenclouds.fluidbottomnavigation.extension.interpolators
import com.tenclouds.fluidbottomnavigation.extension.scaleAnimator
import com.tenclouds.fluidbottomnavigation.extension.translationYAnimator

internal class TopContainerView @JvmOverloads constructor(context: Context,
                                                          attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr), AnimatedView {

    init {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.top))
        translationY = 100f
    }

    override val selectAnimator by lazy {
        AnimatorSet()
                .apply {
                    playTogether(
                            selectScaleAnimator,
                            selectMoveAnimator)
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
                            deselectMoveAnimator)
                }
    }

    override fun cancelDeselectAnimationAndResetState() {
        deselectAnimator.cancel()
        scaleX = 1.0f
        scaleY = 1.0f
        translationY = 100f
    }

    private val selectScaleAnimator =
            AnimatorSet()
                    .apply {
                        playSequentially(
                                scaleAnimator(1.0f, 1.25f, 6 * KEY_FRAME_IN_MS, interpolators[1]),
                                scaleAnimator(1.25f, 0.85f, 3 * KEY_FRAME_IN_MS, interpolators[1]),
                                scaleAnimator(0.85f, 1.0f, 3 * KEY_FRAME_IN_MS, interpolators[1]))
                        startDelay = 11 * KEY_FRAME_IN_MS
                    }

    private val selectMoveAnimator =
            AnimatorSet()
                    .apply {
                        play(translationYAnimator(
                                100f,
                                -getItemYTransitionYValue(context) * 1 / 6,
                                7 * KEY_FRAME_IN_MS,
                                interpolators[0]))
                        startDelay = 12 * KEY_FRAME_IN_MS
                    }

    private val deselectScaleAnimator =
            AnimatorSet()
                    .apply {
                        playSequentially(
                                scaleAnimator(1.0f, 0.85f, 3 * KEY_FRAME_IN_MS, interpolators[1]),
                                scaleAnimator(0.85f, 1.25f, 3 * KEY_FRAME_IN_MS, interpolators[1]),
                                scaleAnimator(1.25f, 1.0f, 7 * KEY_FRAME_IN_MS, interpolators[1]))
                    }

    private val deselectMoveAnimator =
            AnimatorSet()
                    .apply {
                        play(translationYAnimator(
                                -getItemYTransitionYValue(context) * 1 / 6,
                                100f,
                                10 * KEY_FRAME_IN_MS,
                                interpolators[0]))
                        startDelay = 8 * KEY_FRAME_IN_MS
                    }
}