package com.skala.runloop_app.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

import com.skala.runloop_app.AlphaSatColorMatrixEvaluator;

/**
 * @author Skala
 */
public class Utility {
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void animationLoadImage(final Drawable drawable, long time) {
        AlphaSatColorMatrixEvaluator evaluator = new AlphaSatColorMatrixEvaluator();
        final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(evaluator.getColorMatrix());
        drawable.setColorFilter(filter);

        ObjectAnimator animator = ObjectAnimator.ofObject(filter, "colorMatrix", evaluator, evaluator.getColorMatrix());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawable.setColorFilter(filter);
            }
        });
        animator.setDuration(time);
        animator.start();
    }
}
