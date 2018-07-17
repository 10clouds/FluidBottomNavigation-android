package com.tenclouds.fluidbottomnavigation.extension

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView

internal fun View?.scaleAnimator(from: Float = this?.scaleX ?: 0f,
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

internal fun View?.scaleYAnimator(from: Float = this?.scaleX ?: 0f,
                                  to: Float,
                                  animationDuration: Long,
                                  animationInterpolator: Interpolator = LinearInterpolator()) =
        ValueAnimator.ofFloat(from, to)
                .apply {
                    duration = animationDuration
                    interpolator = animationInterpolator
                    addUpdateListener {
                        this@scaleYAnimator?.scaleY = animatedValue as Float
                    }
                }

internal fun View?.translationYAnimator(from: Float = 0f,
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

internal fun View?.alphaAnimator(from: Float = 1f,
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

internal fun ImageView?.tintAnimator(from: Int,
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
