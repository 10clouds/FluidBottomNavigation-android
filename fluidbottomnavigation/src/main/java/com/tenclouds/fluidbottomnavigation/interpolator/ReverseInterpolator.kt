package com.tenclouds.fluidbottomnavigation.interpolator

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator


internal class ReverseInterpolator constructor(private val interpolator: Interpolator = LinearInterpolator())
    : Interpolator {

    override fun getInterpolation(input: Float): Float {
        return 1 - interpolator.getInterpolation(input)
    }
}