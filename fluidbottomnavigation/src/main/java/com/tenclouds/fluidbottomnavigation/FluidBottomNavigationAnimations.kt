package com.tenclouds.fluidbottomnavigation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v4.view.animation.PathInterpolatorCompat
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import kotlinx.android.synthetic.main.item.view.*

private val circleScaleInterpolations =
        arrayOf(
                arrayOf(0.250f, 0.000f, 0.000f, 1.000f),
                arrayOf(0.420f, 0.000f, 0.580f, 1.000f),
                arrayOf(0.200f, 0.000f, 0.800f, 1.000f),
                arrayOf(0.200f, 0.000f, 0.800f, 1.000f),
                arrayOf(0.200f, 0.000f, 0.800f, 1.000f))

private val iconScaleInterpolations =
        arrayOf(
                arrayOf(0.250f, 0.000f, 0.000f, 1.000f),
                arrayOf(0.250f, 0.000f, 0.000f, 1.000f),
                arrayOf(0.270f, 0.000f, 0.000f, 1.000f))

private val itemsMoveInterpolations =
        arrayOf(
                arrayOf(0.250f, 0.000f, 0.000f, 1.000f),
                arrayOf(0.500f, 0.000f, 0.500f, 1.000f))

private val topContainerScaleInterpolation = arrayOf(0.250f, 0.000f, 0.000f, 1.000f)

private fun getItemTranslationY(context: Context?) =
        context?.resources?.getDimension(R.dimen.fluidBottomNavigationItemTranslationY) ?: 0f

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
                            circle.getSelectItemsMoveAnimator(),
                            icon.getSelectIconScaleAnimatorSet(),
                            icon.getSelectItemsMoveAnimator(),
                            icon.getSelectIconTintAnimatorSet(iconColor, iconSelectedColor),
                            title.getSelectTextAlphaAnimatorSet(),
                            title.getSelectItemsMoveAnimator(),
                            topAnimatedImageView.getSelectTopContainerMoveAnimatorSet(),
                            topAnimatedImageView.getSelectTopContainerScaleAnimatorSet())
                }
                .start()
    }
}

internal fun FluidBottomNavigation.animateDeselectItemView(itemView: View) {
    with(itemView) {
        AnimatorSet()
                .apply {
                    playTogether(
                            circle
                                    .getDeselectCircleScaleAnimatorSet()
                                    .withEndAction {
                                        circle.invisible()
                                        circle.setTintColor(getColor(backColor))
                                    },
                            circle.getDeselectItemsMoveAnimator(),
                            icon.getDeselectIconScaleAnimatorSet(),
                            icon.getDeselectItemsMoveAnimator(),
                            icon.getDeselectIconTintAnimatorSet(iconSelectedColor, iconColor),
                            title.getDeselectTextAlphaAnimatorSet(),
                            title.getDeselectItemsMoveAnimator(),
                            topAnimatedImageView.getDeselectTopContainerMoveAnimatorSet(),
                            topAnimatedImageView.getDeselectTopContainerScaleAnimatorSet())
                }
                .start()
    }
}

private fun View?.getSelectCircleScaleAnimatorSet() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            scaleAnimator(0.0f, 1.0f, 7.keyFramesToMs(), circleScaleInterpolations[0].toInterpolation()),
                            scaleAnimator(1.0f, 0.33f, 4.keyFramesToMs(), circleScaleInterpolations[1].toInterpolation()),
                            scaleAnimator(0.33f, 1.2f, 7.keyFramesToMs(), circleScaleInterpolations[2].toInterpolation()),
                            scaleAnimator(1.2f, 0.8f, 3.keyFramesToMs(), circleScaleInterpolations[3].toInterpolation()),
                            scaleAnimator(0.8f, 1.0f, 3.keyFramesToMs(), circleScaleInterpolations[4].toInterpolation()))
                }

private fun View?.getDeselectCircleScaleAnimatorSet() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            scaleAnimator(1.0f, 0.8f, 3.keyFramesToMs(), circleScaleInterpolations[4].toInterpolation()),
                            scaleAnimator(0.8f, 1.2f, 3.keyFramesToMs(), circleScaleInterpolations[3].toInterpolation()),
                            scaleAnimator(1.2f, 0.33f, 7.keyFramesToMs(), circleScaleInterpolations[2].toInterpolation()),
                            scaleAnimator(0.33f, 1.0f, 4.keyFramesToMs(), circleScaleInterpolations[1].toInterpolation()),
                            scaleAnimator(1.0f, 0.0f, 7.keyFramesToMs(), circleScaleInterpolations[0].toInterpolation()))

                }

private fun View?.getSelectIconScaleAnimatorSet() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            scaleAnimator(0.9f, 1.1f, 7.keyFramesToMs(), iconScaleInterpolations[0].toInterpolation()),
                            scaleAnimator(1.1f, 0.84f, 4.keyFramesToMs(), iconScaleInterpolations[1].toInterpolation()),
                            scaleAnimator(0.84f, 0.9f, 4.keyFramesToMs(), iconScaleInterpolations[2].toInterpolation()))
                }

private fun View?.getDeselectIconScaleAnimatorSet() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            scaleAnimator(0.9f, 0.84f, 4.keyFramesToMs(), iconScaleInterpolations[2].toInterpolation()),
                            scaleAnimator(0.84f, 1.1f, 4.keyFramesToMs(), iconScaleInterpolations[1].toInterpolation()),
                            scaleAnimator(1.1f, 0.9f, 7.keyFramesToMs(), iconScaleInterpolations[0].toInterpolation()))
                }


private fun View?.getSelectItemsMoveAnimator() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            translationYAnimator(
                                    0f,
                                    -(getItemTranslationY(this@getSelectItemsMoveAnimator?.context) * 11 / 10),
                                    7.keyFramesToMs(),
                                    itemsMoveInterpolations[0].toInterpolation()),
                            translationYAnimator(
                                    -(getItemTranslationY(this@getSelectItemsMoveAnimator?.context) * 11 / 10),
                                    -(getItemTranslationY(this@getSelectItemsMoveAnimator?.context)),
                                    3.keyFramesToMs(),
                                    itemsMoveInterpolations[1].toInterpolation()))
                    startDelay = 11.keyFramesToMs()
                }

private fun View?.getDeselectItemsMoveAnimator() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            translationYAnimator(
                                    -(getItemTranslationY(this@getDeselectItemsMoveAnimator?.context)),
                                    -(getItemTranslationY(this@getDeselectItemsMoveAnimator?.context) * 11 / 10),
                                    3.keyFramesToMs(),
                                    itemsMoveInterpolations[1].toInterpolation()),
                            translationYAnimator(
                                    -(getItemTranslationY(this@getDeselectItemsMoveAnimator?.context) * 11 / 10),
                                    0f,
                                    7.keyFramesToMs(),
                                    itemsMoveInterpolations[0].toInterpolation()))
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

private fun ImageView?.getDeselectIconTintAnimatorSet(colorFrom: Int?,
                                                      colorTo: Int?) =
        AnimatorSet()
                .apply {
                    play(tintAnimator(
                            getColor(colorFrom),
                            getColor(colorTo),
                            3.keyFramesToMs()))
                    startDelay = 20.keyFramesToMs()
                }

private fun View?.getSelectTextAlphaAnimatorSet() =
        AnimatorSet()
                .apply {
                    play(alphaAnimator(0f, 1f, 8.keyFramesToMs(), LinearOutSlowInInterpolator()))
                    startDelay = 14.keyFramesToMs()
                }

private fun View?.getDeselectTextAlphaAnimatorSet() =
        AnimatorSet()
                .apply {
                    play(alphaAnimator(1f, 0f, 8.keyFramesToMs(), LinearOutSlowInInterpolator()))
                    startDelay = 1.keyFramesToMs()
                }

private fun ImageView?.getSelectTopContainerMoveAnimatorSet() =
        AnimatorSet()
                .apply {
                    play(translationYAnimator(100f, 8f, 7.keyFramesToMs(), iconScaleInterpolations[1].toInterpolation()))
                    startDelay = 11.keyFramesToMs()
                }

private fun ImageView?.getDeselectTopContainerMoveAnimatorSet() =
        AnimatorSet()
                .apply {
                    play(translationYAnimator(8f, 100f, 7.keyFramesToMs(), iconScaleInterpolations[1].toInterpolation()))
                    startDelay = 4.keyFramesToMs()
                }

private fun ImageView?.getSelectTopContainerScaleAnimatorSet() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            scaleAnimator(0.9f, 1.05f, 1.keyFramesToMs(), itemsMoveInterpolations[0].toInterpolation()),
                            scaleAnimator(1.05f, 0.9f, 2.keyFramesToMs(), itemsMoveInterpolations[1].toInterpolation()))
                    startDelay = 16.keyFramesToMs()
                }

private fun ImageView?.getDeselectTopContainerScaleAnimatorSet() =
        AnimatorSet()
                .apply {
                    playSequentially(
                            scaleAnimator(1.05f, 0.9f, 2.keyFramesToMs(), topContainerScaleInterpolation.toInterpolation()),
                            scaleAnimator(0.9f, 1.05f, 1.keyFramesToMs(), topContainerScaleInterpolation.toInterpolation()))
                    startDelay = 1.keyFramesToMs()
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

private fun Int.keyFramesToMs() = ((this.toFloat() / 24f) * 1000).toLong()

private fun Array<Float>.toInterpolation() = PathInterpolatorCompat.create(this[0], this[1], this[2], this[3])