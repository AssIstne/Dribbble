package com.assistne.dribbble.bezierindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

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
    private static final float MARGIN_FACTOR = 2.3f;

    private float mDifference;
    private float mRadius;
    private float mDotMargin;

    private float mMaxStretchDistance;
    private float mMaxControlDistance;

    private HorizontalBezierPoint mLeftPoint;
    private VerticalBezierPoint mTopPoint;
    private HorizontalBezierPoint mRightPoint;
    private VerticalBezierPoint mBottomPoint;

    private int mPosition;
    private int[] mClolr;


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
        mDotMargin = mRadius * MARGIN_FACTOR;
        mMaxStretchDistance = mRadius;
        mDifference = mRadius * MAGIC_FACTOR;
        mMaxControlDistance = mDifference * 0.45f;

        mLeftPoint = new HorizontalBezierPoint(0, 0, mDifference);
        mTopPoint = new VerticalBezierPoint(mRadius, -mRadius, mDifference);
        mRightPoint = new HorizontalBezierPoint(2 * mRadius, 0, mDifference);
        mBottomPoint = new VerticalBezierPoint(mRadius, mRadius, mDifference);

        mClolr = new int[]{Color.parseColor("#fcc04d"),//黄
                 Color.parseColor("#00c3e2"),// 蓝
                 Color.parseColor("#fe626d")};// 红
}

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(0, getHeight()/2);
        setStrokePaint();
        canvas.drawCircle(mRadius, 0, mRadius, mPaint);
        canvas.translate(mDotMargin + 2 * mRadius, 0);
        canvas.drawCircle(mRadius, 0, mRadius, mPaint);
        canvas.translate(mDotMargin + 2 * mRadius, 0);
        canvas.drawCircle(mRadius, 0, mRadius, mPaint);
        canvas.restore();
        canvas.save();
        canvas.translate(0, getHeight()/2);
        drawBezierPath(canvas);
        setRectPaint();
        canvas.drawRect(mTopPoint.dataPoint.x - mRadius, mLeftPoint.dataPoint.y - mRadius,
                mTopPoint.dataPoint.x + mRadius, mLeftPoint.dataPoint.y + mRadius, mPaint);
        canvas.restore();
    }

    private void setStrokePaint() {
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(null);
        mPaint.setStrokeWidth(2);
    }

    private void setRectPaint() {
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{8f, 8f}, 8));
        mPaint.setStrokeWidth(2);
    }

    private void drawBezierPath(Canvas canvas) {
        calculateBezierPath(mPath);
        setFillPaint();
        canvas.drawPath(mPath, mPaint);
    }

    private void setFillPaint() {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private void calculateBezierPath(Path path) {
        path.reset();
        path.moveTo(mLeftPoint.dataPoint.x, mLeftPoint.dataPoint.y);
        path.cubicTo(mLeftPoint.topControlPoint.x, mLeftPoint.topControlPoint.y,
                mTopPoint.leftControlPoint.x, mTopPoint.leftControlPoint.y,
                mTopPoint.dataPoint.x, mTopPoint.dataPoint.y);
        path.cubicTo(mTopPoint.rightControlPoint.x, mTopPoint.rightControlPoint.y,
                mRightPoint.topControlPoint.x, mRightPoint.topControlPoint.y,
                mRightPoint.dataPoint.x, mRightPoint.dataPoint.y);
        path.cubicTo(mRightPoint.bottomControlPoint.x, mRightPoint.bottomControlPoint.y,
                mBottomPoint.rightControlPoint.x, mBottomPoint.rightControlPoint.y,
                mBottomPoint.dataPoint.x, mBottomPoint.dataPoint.y);
        path.cubicTo(mBottomPoint.leftControlPoint.x, mBottomPoint.leftControlPoint.y,
                mLeftPoint.bottomControlPoint.x, mLeftPoint.bottomControlPoint.y,
                mLeftPoint.dataPoint.x, mLeftPoint.dataPoint.y);
    }

    public void move(int position, float fraction) {
        mPosition = position;
        // 0 - 1变化
        if(fraction >= 0 && fraction <= 0.2){// 拉扯右点阶段
            setStage1Point(fraction * 5);
        }else if(fraction > 0.2 && fraction <= 0.5){
            setStage2Point((fraction - 0.2f) / 3f * 10f);
        }else if(fraction > 0.5 && fraction <= 0.8){
            setStage3Point((fraction - 0.5f) / 3f * 10f);
        }else if(fraction > 0.8 && fraction <= 0.9){
            setStage4Point((fraction - 0.8f) * 10f);
        }else if(fraction > 0.9 && fraction <=1 ){
            setStage5Point((fraction - 0.9f) * 10f);
        }

        if (fraction > 0.2) {
            // 位移
            float offset = (mDotMargin + 2 * mRadius - mMaxStretchDistance) * ((fraction - 0.2f) / 0.8f);
            offset = offset > 0 ? offset : 0;
            mLeftPoint.movePoint(offset);
            mTopPoint.movePoint(offset);
            mRightPoint.movePoint(offset);
            mBottomPoint.movePoint(offset);
        }

        if (fraction > 0.7) {
            float colorFraction = (fraction - 0.7f) / 0.3f;

        }
        invalidate();
    }

    private void setCirclePoint() {
        float startX = mPosition * (mDotMargin + 2 * mRadius);
        mLeftPoint.setPoint(startX, 0, mDifference);
        mTopPoint.setPoint(startX + mRadius, -mRadius, mDifference);
        mRightPoint.setPoint(startX + 2 * mRadius, 0, mDifference);
        mBottomPoint.setPoint(startX + mRadius, mRadius, mDifference);
    }

    private void setStage1Point(float fraction) {
        setCirclePoint();
        mRightPoint.movePoint(mMaxStretchDistance * fraction);
    }

    private void setStage2Point(float fraction) {
        setStage1Point(1);
        mTopPoint.movePoint(mMaxStretchDistance / 2 * fraction);
        mBottomPoint.movePoint(mMaxStretchDistance / 2 * fraction);

        mLeftPoint.adjustControlPoint(mMaxControlDistance * fraction);
        mRightPoint.adjustControlPoint(mMaxControlDistance * fraction);
    }

    private void setStage3Point(float fraction) {
        setStage2Point(1);
        mTopPoint.movePoint(mMaxStretchDistance / 2 * fraction);
        mBottomPoint.movePoint(mMaxStretchDistance / 2 * fraction);

        mLeftPoint.adjustControlPoint(-mMaxControlDistance * fraction);
        mRightPoint.adjustControlPoint(-mMaxControlDistance * fraction);

        mLeftPoint.movePoint(mMaxStretchDistance / 2 * fraction);
    }

    private void setStage4Point(float fraction) {
        setStage3Point(1);
        mLeftPoint.movePoint(mMaxStretchDistance / 2 * fraction);
    }

    private void setStage5Point(float fraction) {
        setStage4Point(1);
        mLeftPoint.movePoint((float) (Math.sin(Math.PI*fraction)*(2/10f*mRadius)));
    }
}
