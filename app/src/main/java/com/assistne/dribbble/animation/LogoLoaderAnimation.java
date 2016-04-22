package com.assistne.dribbble.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
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
    private boolean mIsDrawingTail;

    private Paint mCirclePaint;
    private Paint mMainPaint;
    private AnimatorSet mAnimatorSet;
    //  圆弧的角度范围(单位度)
    public static final float DEGREE_RANGE = 220f;
    //  圆弧的起始角度(单位度), 水平位置为0度
    public static final float START_DEGREE = 320f;
    public static final float LINE_DEGREE = 128.5f;
    private final long mLineSpeed = 3700;
    private final long mArcSpeed = 3850;
    //  直线距竖直方向的夹角值
    public static final float SIN_ALPHA = 0.6225f;
    public static final float COS_ALPHA = 0.7826f;
    //  直线长度
    private final float mLineLen = 100f;

    private PointContainer mGreenContainer;
    private PointContainer mPinkContainer1;
    private PointContainer mPinkContainer2;

    //  直线起始位置
    private final float mGreenLineStartX = 300f;
    private final float mGreenLineStartY = 300f;
    private final float mGreenR = mLineLen * SIN_ALPHA / COS_ALPHA;
    private final float mCircleCenterX = mGreenLineStartX;
    private final float mCircleCenterY = mLineLen / COS_ALPHA + mGreenLineStartY;

    public LogoLoaderAnimation(Context context) {
        this(context, null);
    }

    public LogoLoaderAnimation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogoLoaderAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        mGreenContainer = new PointContainer(mGreenLineStartX, mGreenLineStartY, mLineLen, LINE_DEGREE,
                mCircleCenterX, mCircleCenterY, mGreenR, START_DEGREE, DEGREE_RANGE);
        final float mPinkLineStartX1 = mGreenLineStartX + (PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN) * COS_ALPHA;
        final float mPinkLineStartY1 = mGreenLineStartY - (PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN) * SIN_ALPHA;
        final float mPinkR1 = mGreenR + PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN;
        mPinkContainer1 = new PointContainer(mPinkLineStartX1, mPinkLineStartY1, mLineLen, LINE_DEGREE,
                mCircleCenterX, mCircleCenterY, mPinkR1, START_DEGREE, DEGREE_RANGE);
        mPinkContainer1.hasTailArc = true;
        mPinkContainer1.tailStartDegree = 0;

        final float mPinkLineStartX2 = mPinkLineStartX1 + (PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN) * COS_ALPHA;
        final float mPinkLineStartY2 = mPinkLineStartY1 - (PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN) * SIN_ALPHA;
        final float mPinkR2 = mPinkR1 + PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN;
        mPinkContainer2 = new PointContainer(mPinkLineStartX2, mPinkLineStartY2, mLineLen, LINE_DEGREE,
                mCircleCenterX, mCircleCenterY, mPinkR2, START_DEGREE, DEGREE_RANGE);
        mPinkContainer2.hasTailArc = true;
        mPinkContainer2.tailStartDegree = 180;

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatorSet.start();
            }

        });
        mAnimatorSet.setInterpolator(new LinearInterpolator());
        initAnimator();
    }

    private void initPaint() {
        mCirclePaint = new Paint();
        mMainPaint = new Paint();
        mMainPaint.setAntiAlias(true);
        mMainPaint.setStyle(Paint.Style.STROKE);
        mMainPaint.setStrokeWidth(PointContainer.PAINT_WIDTH);
        mMainPaint.setColor(getContext().getResources().getColor(R.color.green));
    }

    private void initAnimator() {

        ValueAnimator arcTailAnimator = ValueAnimator.ofFloat(0, 90);
        arcTailAnimator.setDuration(500);
        arcTailAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float degree = (float) animation.getAnimatedValue();
                if (!mIsDrawingTail) {
                    float circleX = (mPinkContainer1.tail.x + mPinkContainer2.tail.x)/2;
                    float circleY = mPinkContainer1.tail.y;
                    float radius = (mPinkContainer1.tail.x - mPinkContainer2.tail.x)/2;
                    mPinkContainer1.setTailPoint(circleX, circleY, radius);
                    mPinkContainer2.setTailPoint(circleX, circleY, radius);
                }
                mPinkContainer1.tailStartDegree = 360 - degree;
                mPinkContainer2.tailStartDegree = 180;
                mPinkContainer1.showTailArc(degree);
                mPinkContainer2.showTailArc(degree);
                mPinkContainer1.tail.x = (float) (mPinkContainer1.tailCircleX + mPinkContainer1.tailRadius * Math.cos(degree * Math.PI / 180));
                mPinkContainer1.tail.y = (float) (mPinkContainer1.tailCircleY - mPinkContainer1.tailRadius * Math.sin(degree * Math.PI / 180));
                mPinkContainer2.tail.x = (float) (mPinkContainer2.tailCircleX - mPinkContainer2.tailRadius * Math.cos(degree * Math.PI / 180));
                mPinkContainer2.tail.y = (float) (mPinkContainer2.tailCircleY - mPinkContainer2.tailRadius * Math.sin(degree * Math.PI / 180));
                mIsSpotDrawing = false;
                mIsDrawingTail = true;
                invalidate();
            }
        });

        ValueAnimator arcTailHideAnimator = ValueAnimator.ofFloat(0, 90);
        arcTailHideAnimator.setDuration(1500);
        arcTailHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float degree = (float) animation.getAnimatedValue();
                mPinkContainer1.tailStartDegree = 270;
                mPinkContainer2.tailStartDegree = 180;
                mPinkContainer1.showTailArc(degree);
                mPinkContainer2.showTailArc(degree);
                mPinkContainer1.tail.x = (float) (mPinkContainer1.tailCircleX + mPinkContainer1.tailRadius * Math.cos((90 - degree) * Math.PI / 180));
                mPinkContainer1.tail.y = (float) (mPinkContainer1.tailCircleY - mPinkContainer1.tailRadius * Math.sin((90 - degree) * Math.PI / 180));
                mPinkContainer2.tail.x = (float) (mPinkContainer2.tailCircleX - mPinkContainer2.tailRadius * Math.cos((90 - degree) * Math.PI / 180));
                mPinkContainer2.tail.y = (float) (mPinkContainer2.tailCircleY - mPinkContainer2.tailRadius * Math.sin((90 - degree) * Math.PI / 180));
                mIsSpotDrawing = false;
                mIsDrawingTail = true;
                invalidate();
            }
        });

        mAnimatorSet.playSequentially(
                initShowLineAnimator(), initShowArcAnimator(), //arcTailAnimator,
                initHideLineAnimator(), initHideArcAnimator(), //arcTailHideAnimator,
                initSpotLineAnimator(), initSpotArcAnimator());
    }

    private Animator initShowLineAnimator() {
        ValueAnimator lineAnimator = ValueAnimator.ofFloat(0, 1);
        lineAnimator.setDuration(mLineSpeed);
        lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGreenContainer.showLine((float) animation.getAnimatedValue());
                mPinkContainer1.showLine((float) animation.getAnimatedValue());
                mPinkContainer2.showLine((float) animation.getAnimatedValue());
                mIsSpotDrawing = false;
                mIsDrawingLine = true;
                mIsShowing = true;
                mIsDrawingTail = false;
                invalidate();
            }
        });
        return lineAnimator;
    }

    private Animator initShowArcAnimator() {
        ValueAnimator arcAnimator = ValueAnimator.ofFloat(0, DEGREE_RANGE);
        arcAnimator.setDuration(mArcSpeed);
        arcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGreenContainer.showArc((float) animation.getAnimatedValue());
                mPinkContainer1.showArc((float) animation.getAnimatedValue());
                mPinkContainer2.showArc((float) animation.getAnimatedValue());
                mIsDrawingLine = false;
                mIsShowing = true;
                mIsDrawingTail = false;
                invalidate();
            }
        });
        return arcAnimator;
    }

    private Animator initHideLineAnimator() {
        ValueAnimator lineHideAnimator = ValueAnimator.ofFloat(0, 1);
        lineHideAnimator.setDuration(8000);
        lineHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGreenContainer.hideLine((float) animation.getAnimatedValue());
                mPinkContainer1.hideLine((float) animation.getAnimatedValue());
                mPinkContainer2.hideLine((float) animation.getAnimatedValue());
                mIsSpotDrawing = false;
                mIsDrawingLine = true;
                mIsShowing = false;
                mIsDrawingTail = true;
                invalidate();
            }
        });
        return lineHideAnimator;
    }
    private Animator initHideArcAnimator() {
        ValueAnimator arcHideAnimator = ValueAnimator.ofFloat(0, DEGREE_RANGE);
        arcHideAnimator.setDuration(mArcSpeed);
        arcHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGreenContainer.hideArc((float) animation.getAnimatedValue());
                mPinkContainer1.hideArc((float) animation.getAnimatedValue());
                mPinkContainer2.hideArc((float) animation.getAnimatedValue());
                mIsSpotDrawing = false;
                mIsDrawingLine = false;
                mIsShowing = false;
                mIsDrawingTail = true;
                invalidate();
            }
        });
        return arcHideAnimator;
    }

    private Animator initSpotLineAnimator() {
        ValueAnimator spotLineAnimator = ValueAnimator.ofFloat(0, 1);
        spotLineAnimator.setDuration(mLineSpeed);
        spotLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGreenContainer.showSpotLine((float) animation.getAnimatedValue());
                mPinkContainer1.showSpotLine((float) animation.getAnimatedValue());
                mPinkContainer2.showSpotLine((float) animation.getAnimatedValue());
                mIsSpotDrawing = true;
                mIsDrawingTail = false;
                invalidate();
            }
        });
        return spotLineAnimator;
    }
    private Animator initSpotArcAnimator() {
        ValueAnimator spotArcAnimator = ValueAnimator.ofFloat(0, DEGREE_RANGE);
        spotArcAnimator.setDuration(mArcSpeed);
        spotArcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGreenContainer.showSpotArc((float) animation.getAnimatedValue());
                mPinkContainer1.showSpotArc((float) animation.getAnimatedValue());
                mPinkContainer2.showSpotArc((float) animation.getAnimatedValue());
                mIsSpotDrawing = true;
                mIsDrawingTail = false;
                invalidate();
            }
        });
        return spotArcAnimator;
    }

    private void onDrawContainer(Canvas canvas, PointContainer container, int color) {
        mCirclePaint.setColor(color);
        mMainPaint.setColor(color);
        if (!mIsSpotDrawing) {
            //  起点圆点
            canvas.drawCircle(container.head.x, container.head.y, PointContainer.PAINT_WIDTH/2, mCirclePaint);
            if (mIsShowing) {// 显示图形
                //  画线
                canvas.drawLine(container.lineStart.x, container.lineStart.y,
                        container.currentLine.x, container.currentLine.y, mMainPaint);
                if (!mIsDrawingLine) {//  进入画弧阶段
                    //  画弧
                    canvas.drawArc(container.circleRectF, START_DEGREE, container.sweepDegree, false, mMainPaint);
                }
            } else {//  隐藏图形
                if (mIsDrawingLine) {
                    Log.d(TAG, "onDrawContainer: " + mGreenContainer.currentLine + mGreenContainer.lineEnd);
                    canvas.drawLine(container.currentLine.x, container.currentLine.y,
                            container.lineEnd.x, container.lineEnd.y, mMainPaint);
                    canvas.drawArc(container.circleRectF, START_DEGREE, container.sweepDegree, false, mMainPaint);
                } else {
                    canvas.drawArc(container.circleRectF, START_DEGREE + container.sweepDegree,
                            DEGREE_RANGE - container.sweepDegree, false, mMainPaint);
                }
            }
            if (mIsDrawingTail && container.hasTailArc) {
                canvas.drawArc(container.tailCircleRectF, container.tailStartDegree, container.tailSweepDegree, false, mMainPaint);
            }
            //  画末端的圆点
            canvas.drawCircle(container.tail.x, container.tail.y, PointContainer.PAINT_WIDTH/2, mCirclePaint);
        } else {
            canvas.drawCircle(container.head.x, container.head.y, PointContainer.PAINT_WIDTH/2, mCirclePaint);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawContainer(canvas, mGreenContainer, getContext().getResources().getColor(R.color.green));
        onDrawContainer(canvas, mPinkContainer1, getContext().getResources().getColor(R.color.pink));
        onDrawContainer(canvas, mPinkContainer2, getContext().getResources().getColor(R.color.pink));
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
