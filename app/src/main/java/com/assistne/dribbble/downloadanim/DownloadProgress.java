package com.assistne.dribbble.downloadanim;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Random;

/**
 * https://dribbble.com/shots/2939772--Daily-gif-Download
 * Created by assistne on 16/9/8.
 */
public class DownloadProgress extends View {

    public static final int STAGE0 = 0;
    public static final int STAGE1 = 1;
    public static final int STAGE2 = 2;
    public static final int STAGE3 = 3;
    public static final int STAGE4 = 4;
    public static final int STAGE5 = 5;
    public static final int STAGE6 = 6;
    public static final int STAGE7 = 7;
    public static final int STAGE8 = 8;

    private Paint mWhitePaint;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Paint mTransPaint;

    private int mStage;

    private final int mCircleRadius = 150;
    private final int mMaxArrowMainLength = 130;
    private final int mMaxArrowEndDeltaX = 38;
    private final int mMaxArrowEndDeltaY = 44;
    private final int mMaxProgressLineLength = 600;

    private PointF mArrowStartPoint;
    private float mArrowMainLength = mMaxArrowMainLength;
    private float mArrowEndDeltaX = mMaxArrowEndDeltaX;
    private float mArrowEndDeltaY = mMaxArrowEndDeltaY;

    private float mStartAngle;
    private float mSwapAngle;
    private RectF mCircleRect;

    private PointF mLineStartPoint;
    private PointF mLineMidPoint;
    private PointF mLineEndPoint;
    private PointF mLineMidSPoint;

    private int mProgress;

    private ArrayList<PointF> mDotGroup;

    public DownloadProgress(Context context) {
        this(context, null);
    }

    public DownloadProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setStrokeCap(Paint.Cap.ROUND);
        mWhitePaint.setColor(Color.WHITE);
        mWhitePaint.setStyle(Paint.Style.STROKE);
        mWhitePaint.setStrokeWidth(8);

        mCirclePaint = new Paint(mWhitePaint);
        mCirclePaint.setStrokeWidth(6);
        mCirclePaint.setColor(Color.parseColor("#90ffffff"));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#00ffffff"));
        mTextPaint.setTextSize(100);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTransPaint = new Paint(mCirclePaint);

        mArrowStartPoint = new PointF();
        mCircleRect = new RectF();
        mLineStartPoint = new PointF();
        mLineEndPoint = new PointF();
        mLineMidPoint = new PointF();
        mLineMidSPoint = new PointF();

        mDotGroup = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        reset();
    }

    private void reset() {
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;
        mArrowStartPoint.set(centerX, centerY - mMaxArrowMainLength / 2);
        mCircleRect.set(centerX - mCircleRadius, centerY - mCircleRadius, centerX + mCircleRadius, centerY + mCircleRadius);

        mLineStartPoint.set(0.866f * mCircleRadius + centerX, centerY - mCircleRadius / 2);
        mLineEndPoint.set(mLineStartPoint);
        mLineMidPoint.set(mLineStartPoint);

        mCirclePaint.setColor(Color.parseColor("#90ffffff"));
        mTextPaint.setColor(Color.parseColor("#00ffffff"));
        mDotGroup.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        for (PointF dot : mDotGroup) {
            canvas.drawLine(dot.x, dot.y, dot.x + 1, dot.y + 1, mTransPaint);
        }
        switch (mStage) {
            case STAGE0:
                canvas.drawCircle(centerX, centerY, mCircleRadius, mCirclePaint);
                canvas.drawLine(mArrowStartPoint.x, mArrowStartPoint.y, mArrowStartPoint.x, mArrowStartPoint.y + mArrowMainLength, mWhitePaint);
                float arrowMainEndY = centerY + mMaxArrowMainLength / 2;
                float arrowY = arrowMainEndY - mArrowEndDeltaY;
                canvas.drawLine(centerX, arrowMainEndY, centerX + mArrowEndDeltaX, arrowY, mWhitePaint);
                canvas.drawLine(centerX, arrowMainEndY, centerX - mArrowEndDeltaX, arrowY, mWhitePaint);
                break;
            case STAGE1:
                canvas.drawCircle(centerX, centerY, mCircleRadius, mCirclePaint);
                canvas.drawArc(mCircleRect, mStartAngle, mSwapAngle, false, mWhitePaint);
                break;
            case STAGE2:
                mTextPaint.setTextSize(100);
                canvas.drawText("0", centerX, centerY + 50, mTextPaint);
                mTextPaint.setTextSize(50);
                canvas.drawText("%", centerX + 60, centerY + 50, mTextPaint);
                canvas.drawCircle(centerX, centerY, mCircleRadius, mCirclePaint);
                canvas.drawArc(mCircleRect, mStartAngle, mSwapAngle, false, mWhitePaint);
                canvas.drawLine(mLineStartPoint.x, mLineStartPoint.y, mLineMidPoint.x, mLineMidPoint.y, mWhitePaint);
                canvas.drawLine(mLineMidPoint.x, mLineMidPoint.y, mLineEndPoint.x, mLineEndPoint.y, mWhitePaint);
                break;
            case STAGE3:
                mTextPaint.setTextSize(100);
                canvas.drawText("0", centerX, centerY + 50, mTextPaint);
                mTextPaint.setTextSize(50);
                canvas.drawText("%", centerX + 60, centerY + 50, mTextPaint);
                canvas.drawCircle(centerX, centerY, mCircleRadius, mCirclePaint);
                canvas.drawLine(mLineStartPoint.x, mLineStartPoint.y, mLineMidSPoint.x, mLineMidSPoint.y, mWhitePaint);
                canvas.drawLine(mLineMidPoint.x, mLineMidPoint.y, mLineEndPoint.x, mLineEndPoint.y, mWhitePaint);
                canvas.drawLine(mLineStartPoint.x, mLineStartPoint.y, mLineEndPoint.x, mLineEndPoint.y, mTransPaint);
                break;
            case STAGE4:
                mTextPaint.setTextSize(100);
                canvas.drawText(String.valueOf(mProgress), centerX, centerY + 50, mTextPaint);
                mTextPaint.setTextSize(50);
                int len = String.valueOf(mProgress).length();
                int gap = 60 + (len - 1) * 30;
                canvas.drawText("%", centerX + gap, centerY + 50, mTextPaint);
                canvas.drawLine(mLineStartPoint.x, mLineStartPoint.y, mLineEndPoint.x, mLineEndPoint.y, mTransPaint);
                canvas.drawLine(mLineEndPoint.x, mLineEndPoint.y, mLineEndPoint.x + 600 * mProgress / 100, mLineEndPoint.y, mWhitePaint);
                break;
            case STAGE5:
                mTextPaint.setTextSize(100);
                canvas.drawText("100", centerX, centerY + 50, mTextPaint);
                mTextPaint.setTextSize(50);
                canvas.drawText("%", centerX + 120, centerY + 50, mTextPaint);
                canvas.drawLine(mLineStartPoint.x, mLineStartPoint.y, mLineEndPoint.x, mLineEndPoint.y, mWhitePaint);
                canvas.drawLine(mLineMidPoint.x, mLineMidPoint.y, mLineMidSPoint.x, mLineMidSPoint.y, mWhitePaint);
                break;
            case STAGE6:
                canvas.drawArc(mCircleRect, mStartAngle, mSwapAngle, false, mTransPaint);
                break;
            case STAGE7:
                canvas.drawCircle(centerX, centerY, mCircleRadius, mTransPaint);
                canvas.drawArc(mCircleRect, mStartAngle, mSwapAngle, false, mWhitePaint);
                break;
            case STAGE8:
                canvas.drawCircle(centerX, centerY, mCircleRadius, mTransPaint);
                canvas.drawLine(mLineStartPoint.x, mLineStartPoint.y, mLineMidPoint.x, mLineMidPoint.y, mWhitePaint);
                canvas.drawLine(mLineMidPoint.x, mLineMidPoint.y, mLineEndPoint.x, mLineEndPoint.y, mWhitePaint);
                break;
        }
    }

    public void setProgress(int progress) {
        mProgress = progress;
        mStage = STAGE4;
        invalidate();
        if (progress == 100) {
            finishDownload();
        }
    }

    public void startDownload() {
        mProgress = 0;
        reset();
        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;
        AnimatorSet animationSet = new AnimatorSet();
        ValueAnimator stage0 = ValueAnimator.ofFloat(0, 1);
        stage0.setDuration(500);
        // 收缩箭头
        stage0.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStage = STAGE0;
                float fraction = animation.getAnimatedFraction();
                mArrowEndDeltaX = mMaxArrowEndDeltaX * (1 - fraction);
                mArrowEndDeltaY = mMaxArrowEndDeltaY * (1 - fraction);

                mArrowMainLength = mMaxArrowMainLength * (1 - fraction);
                mArrowStartPoint.x = centerX;
                mArrowStartPoint.y = centerY - mMaxArrowMainLength / 2 - (mCircleRadius - mMaxArrowMainLength / 2) * fraction;
                invalidate();
            }
        });

        ValueAnimator stage1 = ValueAnimator.ofFloat(0, 1);
        stage1.setDuration(1500);
        // 点绕2圈
        stage1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStage = STAGE1;
                float fraction = animation.getAnimatedFraction();
                if (fraction < 0.9) {
                    mSwapAngle = 10;
                } else {
                    mSwapAngle = Math.max(1, (1 - (fraction - 0.9f) / 0.1f) * 10);
                }
                mStartAngle = -90 + 720 * fraction;
                invalidate();
            }
        });

        ValueAnimator stage2 = ValueAnimator.ofFloat(0, 1);
        stage2.setDuration(1200);
        // 白色线段运动至水平线
        stage2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStage = STAGE2;
                float fraction = animation.getAnimatedFraction();
                float circleFraction;
                // 画弧
                if (fraction < 0.3) {
                    circleFraction = fraction / 0.3f;
                    mStartAngle = -90;
                    mSwapAngle = Math.max(1, circleFraction * 60);
                } else if (fraction < 0.5){
                    circleFraction = (fraction - 0.3f) / 0.2f;
                    mStartAngle = -30;
                    mSwapAngle = Math.min(-1, -60 + circleFraction * 60);
                    float x = (float) (centerX - mCircleRadius * Math.sin(Math.toRadians(mStartAngle + mSwapAngle)));
                    float y = (float) (centerY - mCircleRadius * Math.cos(Math.toRadians(mStartAngle + mSwapAngle)));
                    addDot(x, y);
                } else {
                    mSwapAngle = 0;
                }
                float lineFraction;
                // 画线
                if (fraction >= 0.3) {
                    if (fraction < 0.7) {// 画斜线
                        lineFraction = (fraction - 0.3f) / 0.4f;
                        mLineMidPoint.x = mLineStartPoint.x + 350 / 3.5f * lineFraction;
                        mLineMidPoint.y = mLineStartPoint.y + 350 * 0.9f * lineFraction;
                        mLineEndPoint.set(mLineMidPoint);
                    } else {
                        lineFraction = (fraction - 0.7f) / 0.3f;
                        mLineStartPoint.x = mLineMidPoint.x - 350 / 3.5f * (1 - lineFraction);
                        mLineStartPoint.y = mLineMidPoint.y - 350 * 0.9f * (1 - lineFraction);
                        addDot(mLineStartPoint.x, mLineStartPoint.y);
                        mLineEndPoint.x = mLineMidPoint.x - 300 * lineFraction;
                    }

                    float textColorFraction = (fraction - 0.3f) / 0.7f;
                    int txtColor = Color.argb((int) (125 * textColorFraction), 255, 255, 255);
                    mTextPaint.setColor(txtColor);
                    int circleColor = Color.argb((int) (144 - 100 * textColorFraction), 255, 255, 255);
                    mCirclePaint.setColor(circleColor);
                }
                invalidate();
            }
        });
        ValueAnimator stage3 = ValueAnimator.ofFloat(0, 1);
        stage3.setDuration(600);
        stage3.setInterpolator(new LinearInterpolator());
        // 形成进度条, 进度条归0
        stage3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStage = STAGE3;
                float fraction = animation.getAnimatedFraction();
                float lineFraction;
                if (fraction < 0.4) {
                    lineFraction = fraction / 0.4f;
                    int txtColor = Color.argb((int) (125 + 130 * lineFraction), 255, 255, 255);
                    mTextPaint.setColor(txtColor);
                    int circleColor = Color.argb((int) (44 * (1 - lineFraction)), 255, 255, 255);
                    mCirclePaint.setColor(circleColor);
                    mLineMidSPoint.set(mLineMidPoint);
                    mLineStartPoint.x = mLineMidPoint.x + (centerX + mMaxProgressLineLength / 2 - mLineMidPoint.x) * lineFraction;
                    mLineEndPoint.x = mLineMidPoint.x - 300 - (mLineMidPoint.x - 300 - centerX + mMaxProgressLineLength / 2) * lineFraction;
                } else {
                    lineFraction = (fraction - 0.4f) / 0.6f;
                    float startX = centerX + 226.4f;
                    mLineMidSPoint.x = startX + ((mLineStartPoint.x - startX) * lineFraction);
                    mLineMidPoint.x = startX - (startX - mLineEndPoint.x) * lineFraction;
                    addDot(mLineMidPoint.x, mLineMidPoint.y);
                }
                if (fraction > 0.9) {
                    mDotGroup.clear();
                }
                invalidate();
            }
        });
        animationSet.playSequentially(stage0, stage1, stage2, stage3);
        animationSet.start();
    }

    private void finishDownload() {
        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;
        mLineMidSPoint.x = mLineMidPoint.x = centerX;
        mLineMidSPoint.y = mLineMidPoint.y = mLineStartPoint.y;
        final float deltaS = mLineStartPoint.x - centerX;
        final float deltaE = centerX - mLineEndPoint.x;
        AnimatorSet animationSet = new AnimatorSet();
        ValueAnimator stage0 = ValueAnimator.ofFloat(Math.max(deltaE, deltaS), 0);
        stage0.setDuration(400);
        stage0.setInterpolator(new AccelerateInterpolator());
        // 收缩进度条
        stage0.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStage = STAGE5;
                mLineStartPoint.x = centerX + Math.min(deltaS, (Float) animation.getAnimatedValue());
                mLineEndPoint.x = centerX - Math.min(deltaE, (Float) animation.getAnimatedValue());
                int txtColor = Color.argb((int) (255 * (1 - animation.getAnimatedFraction())), 255, 255, 255);
                mTextPaint.setColor(txtColor);
                invalidate();
            }
        });

        ValueAnimator stage1 = ValueAnimator.ofFloat(0, 1);
        stage1.setDuration(400);
        stage1.setStartDelay(350);
        // 弹起点
        stage1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStage = STAGE5;
                float fraction = animation.getAnimatedFraction();
                mLineMidPoint.y = mLineStartPoint.y - (mLineStartPoint.y - centerY + mCircleRadius) * fraction;
                if (fraction >= 0.2f) {
                    mLineMidSPoint.y = mLineStartPoint.y - (mLineStartPoint.y - centerY + mCircleRadius) * (fraction - 0.2f) / 0.8f;
                }
                invalidate();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playTogether(stage0, stage1);

        ValueAnimator stage2 = ValueAnimator.ofFloat(0, 360);
        stage2.setDuration(600);
        stage2.setInterpolator(new LinearInterpolator());
        // 画圆
        stage2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStage = STAGE6;
                mStartAngle = -90;
                mSwapAngle = - (float)animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator stage3 = ValueAnimator.ofFloat(0, 70);
        stage3.setDuration(600);
        stage3.setInterpolator(new LinearInterpolator());
        // 点运动至打钩的起点
        stage3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStage = STAGE7;
                mStartAngle = -90 - (float)animation.getAnimatedValue();
                mSwapAngle = 1;
                invalidate();
            }
        });

        final float cos = 0.93969f;
        final float sin = 0.34202f;
        final float startX = centerX - cos * mCircleRadius;
        final float startY = centerY - sin * mCircleRadius;
        ValueAnimator stage4 = ValueAnimator.ofFloat(0, 170);
        stage4.setDuration(450);
        stage4.setInterpolator(new LinearInterpolator());
        // 延长线段1
        stage4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStage = STAGE8;
                float len = (float) animation.getAnimatedValue();
                mLineStartPoint.x = startX;
                mLineStartPoint.y = startY;

                mLineMidPoint.x = mLineStartPoint.x + len * 0.7071f;
                mLineMidPoint.y = mLineStartPoint.y + len * 0.7071f;
                mLineEndPoint.set(mLineMidPoint);
                invalidate();
            }
        });

        ValueAnimator stage5 = ValueAnimator.ofFloat(0, 140);
        stage5.setDuration(350);
        stage5.setInterpolator(new BounceInterpolator());
        // 缩小线段1, 延长线段2, 形成钩
        stage5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStage = STAGE8;
                float len = (float) animation.getAnimatedValue();
                mLineStartPoint.x = startX + len * 9 / 14 * 0.7071f;
                mLineStartPoint.y = startY + len * 9 / 14 * 0.7071f;

                mLineEndPoint.x = mLineMidPoint.x + len * 0.8f;
                mLineEndPoint.y = mLineMidPoint.y - len * 0.65f;
                invalidate();
            }
        });
        animationSet.playSequentially(set, stage2, stage3, stage4, stage5);
        animationSet.start();
    }

    private void addDot(float x, float y) {
        Random random = new Random();
        PointF dot = new PointF(x + random.nextFloat() * 30, y - random.nextFloat() * 30);
        if (mDotGroup.size() > 25) {
            mDotGroup.remove(0);
        }
        mDotGroup.add(dot);
    }
}
