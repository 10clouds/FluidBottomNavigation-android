package com.tenclouds.fluidbottomnavigation.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;

import androidx.core.content.res.ResourcesCompat;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Mocks out ResourcesCompat so getFont won't actually attempt to look up the FontRes as a real
 * resource, because of issues with Robolectric.
 * <p>
 * See: https://github.com/robolectric/robolectric/issues/3590
 */
@Implements(ResourcesCompat.class)
public class ShadowResourcesCompat {
    private static Map<Integer, Typeface> FONT_MAP = new HashMap<>();

    @Implementation
    public static Typeface getFont(@NonNull Context context,
                                   @FontRes int id) {
        return FONT_MAP.computeIfAbsent(id, new Function<Integer, Typeface>() {
            @Override
            public Typeface apply(Integer integer) {
                return ShadowResourcesCompat.buildTypeface(integer);
            }
        });
    }

    private static Typeface buildTypeface(@FontRes int id) {
        return Typeface.DEFAULT;
    }
}