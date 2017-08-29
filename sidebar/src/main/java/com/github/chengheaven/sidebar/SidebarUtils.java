package com.github.chengheaven.sidebar;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Heaven・Cheng Created on 17/7/17.
 */

public class SidebarUtils {

    static void changeImageColorFilter(boolean isAnimator, final ImageView image, int fromColor, int toColor) {
        if (isAnimator) {
            ValueAnimator imageColorChangeAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
            imageColorChangeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    image.setColorFilter((Integer) animator.getAnimatedValue());
                }
            });
            imageColorChangeAnimation.setDuration(150);
            imageColorChangeAnimation.start();
        } else {
            image.setColorFilter(toColor);
        }
    }

    static void changeViewBackgroundColor(boolean isAnimator, final View view, int fromColor, int toColor) {
        if (isAnimator) {
            ValueAnimator imageColorChangeAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
            imageColorChangeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    view.setBackgroundColor((Integer) animator.getAnimatedValue());
                }
            });
            imageColorChangeAnimation.setDuration(150);
            imageColorChangeAnimation.start();
        } else {
            view.setBackgroundColor(toColor);
        }
    }

    static void changeViewTopPadding(final View view, int fromPadding, int toPadding) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromPadding, toPadding);
        animator.setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                view.setPadding(view.getPaddingLeft(),
                        (int) animatedValue,
                        view.getPaddingRight(),
                        view.getPaddingBottom());
            }
        });
        animator.start();
    }

    static void changeRightPadding(final View view, int fromPadding, int toPadding) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromPadding, toPadding);
        animator.setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), (int) animatedValue,
                        view.getPaddingBottom());
            }
        });
        animator.start();
    }

    static void changeViewLeftPadding(final View view, int fromMargin, int toMargin) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromMargin, toMargin);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                view.setPadding((int) animatedValue,
                        view.getPaddingTop(),
                        view.getPaddingRight(),
                        view.getPaddingBottom());
                view.requestLayout();

            }
        });
        animator.start();
    }

    static void changeTextSize(final TextView textView, float from, float to) {
        ValueAnimator textSizeChangeAnimator = ValueAnimator.ofFloat(from, to);
        textSizeChangeAnimator.setDuration(150);
        textSizeChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) valueAnimator.getAnimatedValue());
            }
        });
        textSizeChangeAnimator.start();
    }

    static void changeTextColor(boolean isAnimator, final TextView textView, int fromColor, int toColor) {
        if (isAnimator) {
            ValueAnimator changeTextColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
            changeTextColorAnimation.setDuration(150);
            changeTextColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    textView.setTextColor((Integer) animator.getAnimatedValue());
                }
            });
            changeTextColorAnimation.start();
        } else {
            textView.setTextColor(toColor);
        }
    }

    static int getActionbarSize(Context context) {
        int actionbarSize = -1;
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionbarSize = TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
        }
        return actionbarSize;
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
