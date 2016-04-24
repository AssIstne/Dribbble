package com.assistne.dribbble.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by assistne on 16/4/24.
 */
public class PointAnimator {
    private static final String TAG = "#PointAnimator";
    private PointContainer mPointContainer;
    private AnimatorSet mShowAnimatorSet;
    private AnimatorSet mHideAnimatorSet;
    private AnimatorSet mSpotAnimatorSet;

    private final long mLineSpeed = 200;
    private final long mArcSpeed;// = 1100;
    private final long mTailSpeed = 100;
    private long mDelay;

    public PointAnimator(PointContainer pointContainer, long delay) {
        mDelay = delay;
        mPointContainer = pointContainer;
        mArcSpeed = (long) (mLineSpeed * mPointContainer.arcLength() / mPointContainer.lineLength);
        mShowAnimatorSet = new AnimatorSet();
        mHideAnimatorSet = new AnimatorSet();
        mSpotAnimatorSet = new AnimatorSet();
        initAnimator();
    }

    private void initAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(initHideArcAnimator(), initHideTailAnimator());
        mShowAnimatorSet.playSequentially(initShowLineAnimator(), initShowArcAnimator(), initShowTailAnimator());
        mHideAnimatorSet.playSequentially(initHideLineAnimator(), set);
        mSpotAnimatorSet.playSequentially(initSpotCircleAnimator());//initSpotLineAnimator(), initSpotArcAnimator());
    }

    private Animator initShowLineAnimator() {
        ValueAnimator lineAnimator = ValueAnimator.ofFloat(0, 1);
        lineAnimator.setDuration(mLineSpeed);
        lineAnimator.setInterpolator(new LinearInterpolator());
        lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                mPointContainer.showLine(fraction);
            }
        });
        if (mDelay != 0) {
            lineAnimator.setStartDelay(mDelay);
        }
        return lineAnimator;
    }

    private Animator initShowArcAnimator() {
        ValueAnimator arcAnimator = ValueAnimator.ofFloat(0, 1);
        arcAnimator.setDuration(mArcSpeed);
        arcAnimator.setInterpolator(new LinearInterpolator());
        arcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                mPointContainer.showArc(fraction);
            }
        });
        return arcAnimator;
    }

    private Animator initShowTailAnimator() {
        ValueAnimator arcTailAnimator = ValueAnimator.ofFloat(0, 1);
        arcTailAnimator.setDuration(mTailSpeed);
        arcTailAnimator.setInterpolator(new LinearInterpolator());
        arcTailAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                mPointContainer.showTailArc(fraction);
            }
        });
        return arcTailAnimator;
    }

    private Animator initHideLineAnimator() {
        ValueAnimator lineHideAnimator = ValueAnimator.ofFloat(0, 1);
        lineHideAnimator.setDuration(mLineSpeed);
        lineHideAnimator.setInterpolator(new LinearInterpolator());
        lineHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                mPointContainer.hideLine(fraction);
            }
        });

        if (mDelay != 0) {
            lineHideAnimator.setStartDelay(mDelay);
        }
        return lineHideAnimator;
    }

    private Animator initHideArcAnimator() {
        ValueAnimator arcHideAnimator = ValueAnimator.ofFloat(0, 1);
        arcHideAnimator.setDuration(mArcSpeed);
        arcHideAnimator.setInterpolator(new LinearInterpolator());
        arcHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                mPointContainer.hideArc(fraction);
            }
        });
        return arcHideAnimator;
    }

    private Animator initHideTailAnimator() {
        ValueAnimator arcTailHideAnimator = ValueAnimator.ofFloat(0, 1);
        arcTailHideAnimator.setDuration(mArcSpeed);
        arcTailHideAnimator.setInterpolator(new LinearInterpolator());
        arcTailHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                mPointContainer.hideTailArc(fraction);
            }
        });
        return arcTailHideAnimator;
    }


    private Animator initSpotLineAnimator() {
        ValueAnimator spotLineAnimator = ValueAnimator.ofFloat(0, 1);
        spotLineAnimator.setDuration(mLineSpeed);
        spotLineAnimator.setInterpolator(new LinearInterpolator());
        spotLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                mPointContainer.showSpotLine(fraction);
            }
        });
        return spotLineAnimator;
    }

    private Animator initSpotArcAnimator() {
        ValueAnimator spotArcAnimator = ValueAnimator.ofFloat(0, 1);
        spotArcAnimator.setDuration(mArcSpeed);
        spotArcAnimator.setInterpolator(new LinearInterpolator());
        spotArcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                mPointContainer.showSpotArc(fraction);
            }
        });
        return spotArcAnimator;
    }

    private Animator initSpotCircleAnimator() {
        ValueAnimator spotCircleAnimator = ValueAnimator.ofFloat(0, 1);
        spotCircleAnimator.setDuration(mArcSpeed);
        spotCircleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        spotCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                mPointContainer.showSpotCircle(fraction);
            }
        });
        if (mDelay != 0) {
            spotCircleAnimator.setStartDelay(mDelay);
        }
        return spotCircleAnimator;
    }

    public AnimatorSet getShowAnimatorSet() {
        return mShowAnimatorSet;
    }

    public AnimatorSet getHideAnimatorSet() {
        return mHideAnimatorSet;
    }

    public AnimatorSet getSpotAnimatorSet() {
        return mSpotAnimatorSet;
    }
}
