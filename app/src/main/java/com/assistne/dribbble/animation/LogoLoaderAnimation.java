package com.assistne.dribbble.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;

import com.assistne.dribbble.R;

/**
 * https://dribbble.com/shots/2657317-Logo-loader-animation
 * Created by assistne on 16/4/21.
 */
public class LogoLoaderAnimation extends View{
    private static final String TAG = "#LogoLoaderAnimation";
    private boolean mIsSpotDrawing;
    private boolean mIsDrawingLine;
    private boolean mIsShowing;

    //  线段粗细
    private final float mPaintWidth = 16;
    //  直线距竖直方向的夹角值
    private final float mLineSinA = 0.62f;
    private final float mLineCosA = 0.78f;
    //  圆弧的角度范围(单位度)
    private final float mDegreeRange = 220f;
    //  圆弧的起始角度(单位度), 水平位置为0度
    private final float mStartDegree = 320f;

    private float mDegree;
    private Paint mCirclePaint;
    private Paint mPink;
    private Paint mGreen;
    private RectF mRectF;
    private RectF mGreenRectF;
    private boolean mPinkFlag;
    private AnimatorSet mAnimatorSet;
    private ValueAnimator mPinkAnimator;
    //  当前图形起点, 用作画起点圆点
    private float mGreenHeadX;
    private float mGreenHeadY;
    //  当前图形末端, 用作画末端圆点
    private float mGreenTailX;
    private float mGreenTailY;
    //  当前直线的动态位置
    private float mGreenLineX;
    private float mGreenLineY;
    //  直线长度
    private final float mGreenLineLen = 100f;
    //  直线起始位置
    private final float mGreenLineStartX = 300f;
    private final float mGreenLineStartY = 300f;
    //  直线的结束位置
    private final float mGreenLineEndX = mLineSinA * mGreenLineLen + mGreenLineStartX;
    private final float mGreenLineEndY = mLineCosA * mGreenLineLen + mGreenLineStartY;

    private final long mLineSpeed = 350;
    private final long mArcSpeed = 550;

    private final float mGreenR = mGreenLineLen * mLineSinA / mLineCosA;
    private final float mCircleCenterX = mGreenLineStartX;
    private final float mCircleCenterY = mGreenLineLen / mLineCosA + mGreenLineStartY;

    public LogoLoaderAnimation(Context context) {
        this(context, null);
    }

    public LogoLoaderAnimation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogoLoaderAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRectF = new RectF(150f, 100f, 450f, 400f);
        mCirclePaint = new Paint();

        mPink = new Paint();
        mPink.setAntiAlias(true);
        mPink.setStyle(Paint.Style.STROKE);
        mPink.setStrokeWidth(mPaintWidth);
        mPink.setColor(context.getResources().getColor(R.color.pink));
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatorSet.start();
            }

        });
        initGreenAnimator();
        mAnimatorSet.setInterpolator(new LinearInterpolator());
        mPinkAnimator = ValueAnimator.ofFloat(0, 360);
        mPinkAnimator.setDuration(5000);
        mPinkAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mPinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDegree = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mPinkAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mPinkFlag = !mPinkFlag;
                Log.d(TAG, "onAnimationRepeat: " + mPinkFlag);
            }
        });
    }

    private void initGreenAnimator() {
        mGreenRectF = new RectF(mCircleCenterX - mGreenR, mCircleCenterY - mGreenR,
                mCircleCenterX + mGreenR, mCircleCenterY + mGreenR);
        mGreen = new Paint();
        mGreen.setAntiAlias(true);
        mGreen.setStyle(Paint.Style.STROKE);
        mGreen.setStrokeWidth(mPaintWidth);
        mGreen.setColor(getContext().getResources().getColor(R.color.green));
        ValueAnimator lineAnimator = ValueAnimator.ofFloat(0, 1);
        lineAnimator.setDuration(mLineSpeed);
        lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue() * mGreenLineLen;
                mGreenLineX = mLineSinA * value + mGreenLineStartX;
                mGreenLineY = mLineCosA * value + mGreenLineStartY;
                mGreenHeadX = mGreenLineStartX;
                mGreenHeadY = mGreenLineStartY;
                mGreenTailX = mGreenLineX;
                mGreenTailY = mGreenLineY;
                mIsSpotDrawing = false;
                mIsDrawingLine = true;
                mIsShowing = true;
                invalidate();
            }
        });

        ValueAnimator arcAnimator = ValueAnimator.ofFloat(0, mDegreeRange);
        arcAnimator.setDuration(mArcSpeed);
        arcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDegree = (float) animation.getAnimatedValue();
                //  计算弧的动点位置, 用作图形末端
                float deltaDegree = (360f - mStartDegree) - mDegree;// 40 -> 0 -> -180
                mGreenTailX = (float) (mGreenR * Math.cos(deltaDegree * Math.PI / 180)) + mCircleCenterX;
                mGreenTailY = mCircleCenterY - (float) (mGreenR * Math.sin(deltaDegree * Math.PI / 180));
                mIsDrawingLine = false;
                mIsShowing = true;
                invalidate();
            }
        });

        ValueAnimator lineHideAnimator = ValueAnimator.ofFloat(0, 1);
        lineHideAnimator.setDuration(mLineSpeed);
        lineHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue() * mGreenLineLen;
                mGreenLineX = mLineSinA * value + mGreenLineStartX;
                mGreenLineY = mLineCosA * value + mGreenLineStartY;
                mGreenHeadX = mGreenLineX;
                mGreenHeadY = mGreenLineY;
                mIsSpotDrawing = false;
                mIsDrawingLine = true;
                mIsShowing = false;
                invalidate();
            }
        });

        ValueAnimator arcHideAnimator = ValueAnimator.ofFloat(0, mDegreeRange);
        arcHideAnimator.setDuration(mArcSpeed);
        arcHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDegree = (float) animation.getAnimatedValue();
                //  计算弧的动点位置, 用作图形起点
                float deltaDegree = (360f - mStartDegree) - mDegree;// 40 -> 0 -> -180
                mGreenHeadX = (float) (mGreenR * Math.cos(deltaDegree * Math.PI / 180)) + mCircleCenterX;
                mGreenHeadY = mCircleCenterY - (float) (mGreenR * Math.sin(deltaDegree * Math.PI / 180));
                mIsSpotDrawing = false;
                mIsDrawingLine = false;
                mIsShowing = false;
                invalidate();
            }
        });

        ValueAnimator spotLineAnimator = ValueAnimator.ofFloat(0, 1);
        spotLineAnimator.setDuration(mLineSpeed);
        spotLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue() * mGreenLineLen;
                mGreenHeadX = mGreenTailX - mLineSinA * value;
                mGreenHeadY = mGreenTailY - mLineCosA * value;
                mIsSpotDrawing = true;
                invalidate();
            }
        });

        final float mirrorCenterX = mGreenLineStartX - mGreenR;
        final float mirrorCenterY = mGreenLineStartY;
        ValueAnimator spotArcAnimator = ValueAnimator.ofFloat(0, mDegreeRange);
        spotArcAnimator.setDuration(mArcSpeed);
        spotArcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float degree = (float) animation.getAnimatedValue();
                //  计算弧的动点位置, 用作图形末端
                float deltaDegree = mDegreeRange - degree;// 220 -> 0
                mGreenHeadX = (float) (mGreenR * Math.cos(deltaDegree * Math.PI / 180)) + mirrorCenterX;
                mGreenHeadY = mirrorCenterY - (float) (mGreenR * Math.sin(deltaDegree * Math.PI / 180));
                mIsSpotDrawing = true;
                invalidate();
            }
        });
        mAnimatorSet.playSequentially(
                lineAnimator, arcAnimator,
                lineHideAnimator, arcHideAnimator,
                spotLineAnimator, spotArcAnimator);
    }

    private void onDrawGreen(Canvas canvas) {
        if (!mIsSpotDrawing) {
            mCirclePaint.setColor(mGreen.getColor());
            //  起点圆点
            canvas.drawCircle(mGreenHeadX, mGreenHeadY, mPaintWidth/2, mCirclePaint);
            if (mIsShowing) {// 显示图形
                //  画线
                canvas.drawLine(mGreenLineStartX, mGreenLineStartY, mGreenLineX, mGreenLineY, mGreen);
                if (!mIsDrawingLine) {//  进入画弧阶段
                    //  画弧
                    canvas.drawArc(mGreenRectF, mStartDegree, mDegree, false, mGreen);
                }
            } else {//  隐藏图形
                if (mIsDrawingLine) {
                    canvas.drawLine(mGreenLineX, mGreenLineY, mGreenLineEndX, mGreenLineEndY, mGreen);
                    canvas.drawArc(mGreenRectF, mStartDegree, mDegree, false, mGreen);
                } else {
                    canvas.drawArc(mGreenRectF, mStartDegree + mDegree, mDegreeRange - mDegree, false, mGreen);
                }
            }
            //  画末端的圆点
            canvas.drawCircle(mGreenTailX, mGreenTailY, mPaintWidth/2, mCirclePaint);
        } else {
            canvas.drawCircle(mGreenHeadX, mGreenHeadY, mPaintWidth/2, mCirclePaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawGreen(canvas);
        mPink.setStyle(Paint.Style.FILL);
        mPink.setStrokeWidth(1);
        canvas.drawCircle(mGreenLineStartX, mGreenLineStartY, mPaintWidth/2, mPink);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            mAnimatorSet.start();
        } else {
            mAnimatorSet.cancel();
        }
    }
}
