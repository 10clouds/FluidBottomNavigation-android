package com.tenclouds.fluidbottomnavigation.extension

import androidx.core.view.animation.PathInterpolatorCompat

internal val interpolators = arrayOf(
        arrayOf(0.250f, 0.000f, 0.000f, 1.000f).toInterpolator(),
        arrayOf(0.200f, 0.000f, 0.800f, 1.000f).toInterpolator(),
        arrayOf(0.420f, 0.000f, 0.580f, 1.000f).toInterpolator(),
        arrayOf(0.270f, 0.000f, 0.000f, 1.000f).toInterpolator(),
        arrayOf(0.500f, 0.000f, 0.500f, 1.000f).toInterpolator())

private fun Array<Float>.toInterpolator() = PathInterpolatorCompat.create(this[0], this[1], this[2], this[3])