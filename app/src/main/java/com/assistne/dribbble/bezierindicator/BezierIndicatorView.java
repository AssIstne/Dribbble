package com.assistne.dribbble.bezierindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * 仅水平位移
 * http://blog.csdn.net/sinat_27706697/article/details/49836627
 * https://dribbble.com/shots/2227686-Settings-fun-navigation
 * http://www.jianshu.com/p/791d3a791ec2
 * Created by assistne on 16/9/1.
 */
public class BezierIndicatorView extends View {
    private static final String TAG = "#BezierIndicatorView";
    private Paint mPaint;
    private Path mPath;
    private static final float MAGIC_FACTOR = 0.552284749831f;
    private float mDifference;
    private float mRadius;


    private PointF mOriginDataLeft;
    private PointF mOriginDataTop;
    private PointF mOriginDataRight;
    private PointF mOriginDataBottom;

    private PointF mDataLeft;
    private PointF mDataTop;
    private PointF mDataRight;
    private PointF mDataBottom;

    private float mMaxHorizontalDistance;
    private float mMaxVerticalDistance;

    public BezierIndicatorView(Context context) {
        this(context, null);
    }

    public BezierIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(8);
        mPath = new Path();

        mRadius = 100;
        mDifference = mRadius * MAGIC_FACTOR;
        mDataLeft = new PointF(0, 0);
        mDataTop = new PointF(mRadius, -mRadius);
        mDataRight = new PointF(2 * mRadius, 0);
        mDataBottom = new PointF(mRadius, mRadius);

        mOriginDataLeft = new PointF();
        mOriginDataLeft.set(mDataLeft);
        mOriginDataTop = new PointF();
        mOriginDataTop.set(mDataTop);
        mOriginDataRight = new PointF();
        mOriginDataRight.set(mDataRight);
        mOriginDataBottom = new PointF();
        mOriginDataBottom.set(mDataBottom);

        mMaxVerticalDistance =  mRadius / 6;
        mMaxHorizontalDistance = mMaxVerticalDistance * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(0, getHeight()/2);
        calculateBezierPath(mPath);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    private void getCirclePath(Path path, float centerX, float centerY, float radius) {
        float circleLeft = centerX - radius;
        float circleRight = centerX + radius;
        float circleTop = centerY - radius;
        float circleBottom = centerY + radius;
        float difference = radius * MAGIC_FACTOR;

        path.moveTo(circleLeft, centerY);
        path.cubicTo(circleLeft, centerY - difference, centerX - difference, circleTop, centerX, circleTop);
        path.cubicTo(centerX + difference, circleTop, circleRight, centerY - difference, circleRight, centerY);
        path.cubicTo(circleRight, centerY + difference, centerX + difference, circleBottom, centerX, circleBottom);
        path.cubicTo(centerX - difference, circleBottom, circleLeft, centerY + difference, circleLeft, centerY);
    }

    private void calculateBezierPath(Path path) {
        path.reset();
        path.moveTo(mDataLeft.x, mDataLeft.y);
        path.cubicTo(mDataLeft.x, mDataLeft.y - mDifference, mDataTop.x - mDifference, mDataTop.y, mDataTop.x, mDataTop.y);
        path.cubicTo(mDataTop.x + mDifference, mDataTop.y, mDataRight.x, mDataRight.y - mDifference, mDataRight.x, mDataRight.y);
        path.cubicTo(mDataRight.x, mDataRight.y + mDifference, mDataBottom.x + mDifference, mDataBottom.y, mDataBottom.x, mDataBottom.y);
        path.cubicTo(mDataBottom.x - mDifference, mDataBottom.y, mDataLeft.x, mDataLeft.y + mDifference, mDataLeft.x, mDataLeft.y);
    }

    public void moveBy(final float x) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, x);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value < mMaxHorizontalDistance) {
                    float fraction = value / mMaxHorizontalDistance;
                    float deltaY = mMaxVerticalDistance * fraction;
                    mDataRight.x = mOriginDataRight.x + value;
                    mDataTop.x = mOriginDataTop.x + value;
                    mDataTop.y = mOriginDataTop.y + deltaY;
                    mDataBottom.x = mOriginDataBottom.x + value;
                    mDataBottom.y = mOriginDataBottom.y - deltaY;
                } else {
                    mDataRight.x = mOriginDataRight.x + value;
                    mDataTop.x = mOriginDataTop.x + value;
                    mDataTop.y = mOriginDataTop.y + mMaxVerticalDistance;
                    mDataBottom.x = mOriginDataBottom.x + value;
                    mDataBottom.y = mOriginDataBottom.y - mMaxVerticalDistance;
                    mDataLeft.x = mOriginDataLeft.x + value - mMaxHorizontalDistance;
                }
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ValueAnimator anim = ValueAnimator.ofFloat(0, mDataRight.x - mDataLeft.x - 2 * mRadius);
                anim.setInterpolator(new BounceInterpolator());
                anim.setDuration(2500);
                final float origin = mDataLeft.x;
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation1) {
                        mDataLeft.x = origin + (float)animation1.getAnimatedValue();
                        invalidate();
                    }
                });
                anim.start();
            }
        });
        animator.start();
    }

    public void bounce() {
        final float originX = mDataLeft.x;
        final float originTopY = mDataTop.y;
        final float originBottomY = mDataBottom.y;
        ValueAnimator animator = ValueAnimator.ofFloat(0, 100);
        animator.setInterpolator(new BounceInterpolator());
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mDifference = mRadius * MAGIC_FACTOR * (1 + animation.getAnimatedFraction()/2);
                Log.d(TAG, "onAnimationUpdate: " + value);
                float delta = originX + value - mDataRight.x;
                mDataRight.x = originX + value;
                if (mDataRight.x - mDataTop.x > 1.5 * mRadius) {
                    mDataTop.x += delta;
                    mDataBottom.x += delta;
                }
                if (mDataTop.x - mDataLeft.x > 1.5 * mRadius) {
                    mDataLeft.x += delta;
                }
                invalidate();
            }
        });
        animator.start();
    }
}
