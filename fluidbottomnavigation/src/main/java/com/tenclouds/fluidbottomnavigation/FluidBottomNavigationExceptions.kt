package com.tenclouds.fluidbottomnavigation

import android.content.res.Resources

internal class TooMuchItemsException(resources: Resources) :
        IllegalStateException(resources.getString(R.string.exception_too_much_items))

internal class TooLittleItemsException(resources: Resources) :
        IllegalStateException(resources.getString(R.string.exception_too_little_items))