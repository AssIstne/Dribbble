package com.assistne.dribbble.logoloader;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * 处理动画
 * Created by assistne on 16/4/24.
 */
public class PointAnimator {
    //  目标对象
    private PointContainer mPointContainer;
    //  显示阶段的动画
    private AnimatorSet mShowAnimatorSet;
    //  隐藏阶段的动画
    private AnimatorSet mHideAnimatorSet;
    //  动点阶段的动画
    private AnimatorSet mSpotAnimatorSet;

    private final long mLineSpeed = 200;
    private final long mArcSpeed;
    private final long mTailSpeed = 100;
    //  直线动画的延迟时间, 美化动画
    private long mDelay;

    public PointAnimator(PointContainer pointContainer, long delay) {
        mDelay = delay;
        mPointContainer = pointContainer;
        //  动态计算弧阶段时间, 使直线速度与弧速度一致, 过渡时候才流畅
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
        mSpotAnimatorSet.playSequentially(initSpotCircleAnimator());
    }

    //  画直线动画
    private Animator initShowLineAnimator() {
        ValueAnimator lineAnimator = ValueAnimator.ofFloat(0, 1);
        lineAnimator.setDuration(mLineSpeed);
        //  默认是加速减速, 使用线性变化
        lineAnimator.setInterpolator(new LinearInterpolator());
        lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                //  计算坐标
                mPointContainer.showLine(fraction);
            }
        });
        if (mDelay != 0) {
            lineAnimator.setStartDelay(mDelay);
        }
        return lineAnimator;
    }

    //  画弧
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

    //  画尾端弧
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

    //  隐藏直线
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

    //  隐藏弧
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

    //  隐藏尾端弧
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

    //  动点直线阶段
    @Deprecated
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

    //  动点弧阶段
    @Deprecated
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

    //  动点画圆回到起点, 显得更流畅
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
