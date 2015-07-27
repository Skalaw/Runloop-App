package com.skala.runloop_app.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * @author Skala
 */
public class FontUtils {
    public final static String FONT_TYPE_ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";

    public static Typeface getTypeface(AssetManager assets, String fontType) {
        return Typeface.createFromAsset(assets, fontType);
    }
}
