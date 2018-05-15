package com.fan.slideimageview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by f on 2018/5/15.
 */

public class SlideFinishImageView extends FrameLayout {
    private ViewDragHelper helper;
    private ImageView mChild;
    private Activity mActivity;
    private int startColor = Color.parseColor("#ff000000");
    private int endColor = Color.parseColor("#00000000");

    public SlideFinishImageView(Context context) {
        this(context, null);
    }

    public SlideFinishImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        helper = ViewDragHelper.create(this, new DragCallback());
        mActivity = (Activity) context;
        setBackgroundColor(startColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        helper.processTouchEvent(event);
        return true;
    }

    public void setImageView(ImageView img) {
        mChild = img;
        addView(img);
        Drawable drawable = img.getDrawable();
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        LayoutParams params = (LayoutParams) mChild.getLayoutParams();
        params.gravity = Gravity.CENTER;
        params.width = screenWidth;
        params.height = (int) (screenWidth * ((float) height / (float) width));
        mChild.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    class DragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == mChild;
        }


        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return top;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float center = getHeight() / 2;
            int curCenter = (mChild.getTop() + mChild.getBottom()) / 2;
            float fractor = 0;
            if (curCenter < 0) {
                curCenter = 1;
            }
            if (curCenter > center) {
                fractor = center / curCenter;
            } else {
                fractor = curCenter / center;
            }
            Integer alpha = (int) (fractor * 0xff);
            String hexStr = alpha.toHexString(alpha);
            if (hexStr.length() == 1) {
                hexStr = "0" + hexStr;
            }
            setBackgroundColor(Color.parseColor("#" + hexStr + "000000"));
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int cur = mChild.getTop();
            int center = getHeight() / 2;
            int curCenter = (mChild.getTop() + mChild.getBottom()) / 2;
            ValueAnimator animator = new ValueAnimator();
            if (curCenter < center) {
                //往上
                animator.setIntValues(cur, -mChild.getHeight());
            }
            if (curCenter > center) {
                animator.setIntValues(cur, getHeight());
            }
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    mChild.layout(0, value, getRight(), value + mChild.getHeight());
                    if (animation.getAnimatedFraction() == 1) {
                        mActivity.finish();
                    }
                }
            });
            animator.setDuration(1000);
            animator.start();
        }
    }
}
