package com.tenclouds.fluidbottomnavigation.view

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
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
                                getItemTransitionYValue(context),
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
                                getItemTransitionYValue(context),
                                100f,
                                10 * KEY_FRAME_IN_MS,
                                interpolators[0]))
                        startDelay = 8 * KEY_FRAME_IN_MS
                    }

    override fun getItemTransitionYValue(context: Context): Float {
        return -super.getItemTransitionYValue(context) * 1 / 6
    }
}