package com.tenclouds.fluidbottomnavigation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v4.view.animation.PathInterpolatorCompat
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.tenclouds.fluidbottomnavigation.interpolator.ReverseInterpolator
import kotlinx.android.synthetic.main.item.view.*

private val selectCircleScaleInterpolations =
        arrayOf(
                arrayOf(0.250f, 0.000f, 0.000f, 1.000f),
                arrayOf(0.420f, 0.000f, 0.580f, 1.000f),
                arrayOf(0.200f, 0.000f, 0.800f, 1.000f),
                arrayOf(0.200f, 0.000f, 0.800f, 1.000f),
                arrayOf(0.200f, 0.000f, 0.800f, 1.000f))


private val selectIconScaleInterpolations =
        arrayOf(
                arrayOf(0.250f, 0.000f, 0.000f, 1.000f),
                arrayOf(0.250f, 0.000f, 0.000f, 1.000f),
                arrayOf(0.270f, 0.000f, 0.000f, 1.000f))

internal fun FluidBottomNavigation.animateShow() =
        AnimatorSet()
                .apply {
                    play(translationYAnimator(
                            bottomBarHeight?.toFloat() ?: 0f,
                            0f,
                            HIDE_ANIMATION_DURATION,
                            LinearOutSlowInInterpolator()))
                }
                .start()

internal fun FluidBottomNavigation.animateHide() =
        AnimatorSet()
                .apply {
                    play(translationYAnimator(
                            0f,
                            bottomBarHeight?.toFloat() ?: 0f,
                            HIDE_ANIMATION_DURATION,
                            LinearOutSlowInInterpolator()))
                }
                .start()

internal fun FluidBottomNavigation.animateSelectItemView(itemView: View) {
    with(itemView) {
        AnimatorSet()
                .apply {
                    playTogether(
                            circle
                                    .getSelectCircleScaleAnimatorSet()
                                    .withStartAction {
                                        circle.visible()
                                        circle.setTintColor(getColor(accentColor))
                                    },
                            circle.getSelectCircleMoveAnimatorSet(),
                            icon.getSelectCircleMoveAnimatorSet(), // TODO: icon has another move
                            icon.getSelectIconScaleAnimatorSet(),
                            icon.getSelectIconTintAnimatorSet(iconColor, iconSelectedColor),
                            title.getSelectTextAlphaAnimatorSet(),
                            title.getSelectCircleMoveAnimatorSet(), // TODO: title has another move
                            topAnimatedImageView.getSelectTopContainerMoveAnimatorSet())
                }
                .start()
    }
}

// TODO we need create this animations one more time, not with reverse interpolation
internal fun FluidBottomNavigation.animateDeselectItemView(itemView: View) {
    with(itemView) {
        AnimatorSet()
                .apply {
                    playTogether(
                            circle
                                    .getSelectCircleScaleAnimatorSet()
                                    .withEndAction {
                                        circle.invisible()
                                        circle.setTintColor(getColor(backColor))
                                    }.withReverse(),
                            circle.getSelectCircleMoveAnimatorSet().withReverse(),
                            icon.getSelectCircleMoveAnimatorSet().withReverse(), // TODO: icon has another move
                            icon.getSelectIconScaleAnimatorSet().withReverse(),
                            icon.getSelectIconTintAnimatorSet(iconColor, iconSelectedColor).withReverse(),
                            title.getSelectTextAlphaAnimatorSet().withReverse(),
                            title.getSelectCircleMoveAnimatorSet().withReverse(), // TODO: title has another move
                            topAnimatedImageView.getSelectTopContainerMoveAnimatorSet().withReverse())
                }
                .start()
    }
}

private fun ImageView?.getSelectCircleScaleAnimatorSet() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            scaleAnimator(0.0f, 1.0f, 7.keyFramesToMs(), selectCircleScaleInterpolations[0].toInterpolation()),
                            scaleAnimator(1.0f, 0.33f, 4.keyFramesToMs(), selectCircleScaleInterpolations[1].toInterpolation()),
                            scaleAnimator(0.33f, 1.2f, 7.keyFramesToMs(), selectCircleScaleInterpolations[2].toInterpolation()),
                            scaleAnimator(1.2f, 0.8f, 3.keyFramesToMs(), selectCircleScaleInterpolations[3].toInterpolation()),
                            scaleAnimator(0.8f, 1.0f, 3.keyFramesToMs(), selectCircleScaleInterpolations[4].toInterpolation()))
                }

private fun View?.getSelectCircleMoveAnimatorSet() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            translationYAnimator(
                                    0f,
                                    -(this@getSelectCircleMoveAnimatorSet?.resources?.getDimension(R.dimen.fluidBottomNavigationItemTranslationY)
                                            ?: 0f), 11.keyFramesToMs(), OvershootInterpolator()))
                    startDelay = 11.keyFramesToMs()
                }


private fun ImageView?.getSelectIconScaleAnimatorSet() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            scaleAnimator(0.9f, 1.1f, 7.keyFramesToMs(), selectIconScaleInterpolations[0].toInterpolation()),
                            scaleAnimator(1.1f, 0.84f, 4.keyFramesToMs(), selectIconScaleInterpolations[1].toInterpolation()),
                            scaleAnimator(0.84f, 0.9f, 4.keyFramesToMs(), selectIconScaleInterpolations[2].toInterpolation()))
                }

private fun ImageView?.getSelectIconTintAnimatorSet(colorFrom: Int?,
                                                    colorTo: Int?) =
        AnimatorSet()
                .apply {
                    play(tintAnimator(
                            getColor(colorFrom),
                            getColor(colorTo),
                            3.keyFramesToMs()))
                }

private fun TextView?.getSelectTextAlphaAnimatorSet() =
        AnimatorSet()
                .apply {
                    play(alphaAnimator(0f, 1f, 8.keyFramesToMs(), LinearOutSlowInInterpolator()))
                            .apply { startDelay = 11.keyFramesToMs() }
                }

private fun ImageView?.getSelectTopContainerMoveAnimatorSet() =
        AnimatorSet()
                .apply {
                    play(translationYAnimator(100f, 8f, 7.keyFramesToMs(), selectIconScaleInterpolations[1].toInterpolation()))
                            .apply { startDelay = 11.keyFramesToMs() }
                }

private fun View?.scaleAnimator(from: Float = this?.scaleX ?: 0f,
                                to: Float,
                                animationDuration: Long,
                                animationInterpolator: Interpolator = LinearInterpolator()) =
        ValueAnimator.ofFloat(from, to)
                .apply {
                    duration = animationDuration
                    interpolator = animationInterpolator
                    addUpdateListener {
                        this@scaleAnimator?.scaleX = animatedValue as Float
                        this@scaleAnimator?.scaleY = animatedValue as Float
                    }
                }

private fun View?.translationYAnimator(from: Float = 0f,
                                       to: Float,
                                       animationDuration: Long,
                                       animationInterpolator: Interpolator = LinearInterpolator()) =
        ValueAnimator.ofFloat(from, to)
                .apply {
                    duration = animationDuration
                    interpolator = animationInterpolator
                    addUpdateListener {
                        this@translationYAnimator?.translationY = it.animatedValue as Float
                    }
                }

private fun View?.alphaAnimator(from: Float = 1f,
                                to: Float,
                                animationDuration: Long,
                                animationInterpolator: Interpolator = LinearInterpolator()) =
        ValueAnimator.ofFloat(from, to)
                .apply {
                    duration = animationDuration
                    interpolator = animationInterpolator
                    addUpdateListener {
                        this@alphaAnimator?.alpha = it.animatedValue as Float
                    }
                }

private fun ImageView?.tintAnimator(from: Int,
                                    to: Int,
                                    animationDuration: Long,
                                    animationInterpolator: Interpolator = LinearInterpolator()) =
        ValueAnimator.ofObject(ArgbEvaluator(), from, to)
                .apply {
                    duration = animationDuration
                    interpolator = animationInterpolator
                    addUpdateListener {
                        this@tintAnimator?.setTintColor(it.animatedValue as Int)
                    }
                }

private fun AnimatorSet.withStartAction(function: () -> Unit) =
        this
                .apply {
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) = function.invoke()
                        override fun onAnimationEnd(animation: Animator?) = Unit
                        override fun onAnimationRepeat(animation: Animator?) = Unit
                        override fun onAnimationCancel(animation: Animator?) = Unit
                    })
                }

private fun AnimatorSet.withEndAction(function: () -> Unit) =
        this
                .apply {
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationEnd(animation: Animator?) = function.invoke()
                        override fun onAnimationRepeat(animation: Animator?) = Unit
                        override fun onAnimationCancel(animation: Animator?) = Unit
                        override fun onAnimationStart(animation: Animator?) = Unit
                    })
                }

private fun AnimatorSet.withReverse() = this.apply { interpolator = ReverseInterpolator() }

private fun Int.keyFramesToMs() = ((this.toFloat() / 24f) * 1000).toLong()

private fun Array<Float>.toInterpolation() = PathInterpolatorCompat.create(this[0], this[1], this[2], this[3])


