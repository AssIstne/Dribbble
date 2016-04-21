package com.assistne.dribbble.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
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

    private final long mLineSpeed = 700;
    private final long mArcSpeed = 850;
    //  直线长度
    private final float mLineLen = 100f;

    private PointContainer mGreenContainer;
    private PointContainer mPinkContainer1;
    private PointContainer mPinkContainer2;

    //  直线起始位置
    private final float mGreenLineStartX = 300f;
    private final float mGreenLineStartY = 300f;
    private final float mGreenR = mLineLen * PointContainer.SIN_ALPHA / PointContainer.COS_ALPHA;
    private final float mCircleCenterX = mGreenLineStartX;
    private final float mCircleCenterY = mLineLen / PointContainer.COS_ALPHA + mGreenLineStartY;

    public LogoLoaderAnimation(Context context) {
        this(context, null);
    }

    public LogoLoaderAnimation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogoLoaderAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        mGreenContainer = new PointContainer(mGreenLineStartX, mGreenLineStartY, mLineLen,
                mCircleCenterX, mCircleCenterY, mGreenR);
        final float mPinkLineStartX1 = mGreenLineStartX + (PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN) * PointContainer.COS_ALPHA;
        final float mPinkLineStartY1 = mGreenLineStartY - (PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN) * PointContainer.SIN_ALPHA;
        final float mPinkR1 = mGreenR + PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN;
        mPinkContainer1 = new PointContainer(mPinkLineStartX1, mPinkLineStartY1, mLineLen,
                mCircleCenterX, mCircleCenterY, mPinkR1);
        mPinkContainer1.hasTailArc = true;
        mPinkContainer1.tailStartDegree = 0;

        final float mPinkLineStartX2 = mPinkLineStartX1 + (PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN) * PointContainer.COS_ALPHA;
        final float mPinkLineStartY2 = mPinkLineStartY1 - (PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN) * PointContainer.SIN_ALPHA;
        final float mPinkR2 = mPinkR1 + PointContainer.PAINT_WIDTH + PointContainer.LINE_MARGIN;
        mPinkContainer2 = new PointContainer(mPinkLineStartX2, mPinkLineStartY2, mLineLen,
                mCircleCenterX, mCircleCenterY, mPinkR2);
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

        ValueAnimator arcAnimator = ValueAnimator.ofFloat(0, PointContainer.DEGREE_RANGE);
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

        ValueAnimator arcTailAnimator = ValueAnimator.ofFloat(0, 90);
        arcTailAnimator.setDuration(500);
        arcTailAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float degree = (float) animation.getAnimatedValue();
                if (!mIsDrawingTail) {
                    float circleX = (mPinkContainer1.currentTailX + mPinkContainer2.currentTailX)/2;
                    float circleY = mPinkContainer1.currentTailY;
                    float radius = (mPinkContainer1.currentTailX - mPinkContainer2.currentTailX)/2;
                    mPinkContainer1.setTailPoint(circleX, circleY, radius);
                    mPinkContainer2.setTailPoint(circleX, circleY, radius);
                }
                mPinkContainer1.tailStartDegree = 360 - degree;
                mPinkContainer2.tailStartDegree = 180;
                mPinkContainer1.showTailArc(degree);
                mPinkContainer2.showTailArc(degree);
                mPinkContainer1.currentTailX = (float) (mPinkContainer1.tailCircleX + mPinkContainer1.tailRadius * Math.cos(degree * Math.PI / 180));
                mPinkContainer1.currentTailY = (float) (mPinkContainer1.tailCircleY - mPinkContainer1.tailRadius * Math.sin(degree * Math.PI / 180));
                mPinkContainer2.currentTailX = (float) (mPinkContainer2.tailCircleX - mPinkContainer2.tailRadius * Math.cos(degree * Math.PI / 180));
                mPinkContainer2.currentTailY = (float) (mPinkContainer2.tailCircleY - mPinkContainer2.tailRadius * Math.sin(degree * Math.PI / 180));
                mIsSpotDrawing = false;
                mIsDrawingTail = true;
                invalidate();
            }
        });

        ValueAnimator lineHideAnimator = ValueAnimator.ofFloat(0, 1);
        lineHideAnimator.setDuration(mLineSpeed);
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
                mPinkContainer1.currentTailX = (float) (mPinkContainer1.tailCircleX + mPinkContainer1.tailRadius * Math.cos((90 - degree) * Math.PI / 180));
                mPinkContainer1.currentTailY = (float) (mPinkContainer1.tailCircleY - mPinkContainer1.tailRadius * Math.sin((90 - degree) * Math.PI / 180));
                mPinkContainer2.currentTailX = (float) (mPinkContainer2.tailCircleX - mPinkContainer2.tailRadius * Math.cos((90 - degree) * Math.PI / 180));
                mPinkContainer2.currentTailY = (float) (mPinkContainer2.tailCircleY - mPinkContainer2.tailRadius * Math.sin((90 - degree) * Math.PI / 180));
                mIsSpotDrawing = false;
                mIsDrawingTail = true;
                invalidate();
            }
        });

        ValueAnimator arcHideAnimator = ValueAnimator.ofFloat(0, PointContainer.DEGREE_RANGE);
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

        ValueAnimator spotArcAnimator = ValueAnimator.ofFloat(0, PointContainer.DEGREE_RANGE);
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
        mAnimatorSet.playSequentially(
                lineAnimator, arcAnimator, arcTailAnimator,
                lineHideAnimator, arcHideAnimator, //arcTailHideAnimator,
                spotLineAnimator, spotArcAnimator);
    }

    private void onDrawContainer(Canvas canvas, PointContainer container, int color) {
        mCirclePaint.setColor(color);
        mMainPaint.setColor(color);
        if (!mIsSpotDrawing) {
            //  起点圆点
            canvas.drawCircle(container.currentHeadX, container.currentHeadY, PointContainer.PAINT_WIDTH/2, mCirclePaint);
            if (mIsShowing) {// 显示图形
                //  画线
                canvas.drawLine(container.lineStartX, container.lineStartY,
                        container.currentLineX, container.currentLineY, mMainPaint);
                if (!mIsDrawingLine) {//  进入画弧阶段
                    //  画弧
                    canvas.drawArc(container.circleRectF, PointContainer.START_DEGREE, container.sweepDegree, false, mMainPaint);
                }
            } else {//  隐藏图形
                if (mIsDrawingLine) {
                    canvas.drawLine(container.currentLineX, container.currentLineY,
                            container.lineEndX, container.lineEndY, mMainPaint);
                    canvas.drawArc(container.circleRectF, PointContainer.START_DEGREE, container.sweepDegree, false, mMainPaint);
                } else {
                    canvas.drawArc(container.circleRectF, PointContainer.START_DEGREE + container.sweepDegree,
                            PointContainer.DEGREE_RANGE - container.sweepDegree, false, mMainPaint);
                }
            }
            if (mIsDrawingTail && container.hasTailArc) {
                canvas.drawArc(container.tailCircleRectF, container.tailStartDegree, container.tailSweepDegree, false, mMainPaint);
            }
            //  画末端的圆点
            canvas.drawCircle(container.currentTailX, container.currentTailY, PointContainer.PAINT_WIDTH/2, mCirclePaint);
        } else {
            canvas.drawCircle(container.currentHeadX, container.currentHeadY, PointContainer.PAINT_WIDTH/2, mCirclePaint);
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
