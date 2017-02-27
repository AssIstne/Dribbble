package com.assistne.dribbble.dashboardnavigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by assistne on 17/2/27.
 */

public class DotIndicatorView extends View {
    private static final String TAG = "#DotIndicatorView";
    private Paint mPaint;
    private float mOffset;
    private RectF mRectF;
    private int mSize = 5;
    private int mPivot;
    private int mFuturePivot;
    private static final int DOT_SIZE = 30;
    private static final int DOT_MARGIN = 10;
    public DotIndicatorView(Context context) {
        this(context, null);
    }

    public DotIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotIndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mRectF = new RectF(0, 0, DOT_SIZE, DOT_SIZE);
        setAlpha(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mSize > 0) {
            for (int i = 0; i < mSize; i ++) {
                mPaint.setColor(getColor(i));
                canvas.save();
                canvas.translate(getMeasuredWidth()/2 - 2.5f * DOT_SIZE - 2 * DOT_MARGIN + i * (DOT_SIZE + DOT_MARGIN), getMeasuredHeight()/2 - DOT_SIZE / 2);
                canvas.drawOval(mRectF, mPaint);
                canvas.restore();
            }
        }
    }

    private int getColor(int index) {
        // TODO: 17/2/27 假设了mSize是奇数
        int colorIndex;
        int middleIndex = mSize/2;
        int delta = Math.abs(middleIndex - index);
        if (index < middleIndex) {
            colorIndex = mPivot - delta < 0 ? mSize - delta + mPivot : mPivot - delta;
        } else if (index > middleIndex){
            colorIndex = mPivot + delta >= mSize ? mPivot +delta - mSize : mPivot + delta;
        } else {
            colorIndex = mPivot;
        }
        int num = (int) mOffset;
        float fraction = mOffset - num;
        if (num == colorIndex) {
            return (int) PieChartView.ARGB_EVALUATOR.evaluate(fraction,
                    getResources().getColor(PieChartView.COLOR_ARR[colorIndex]),
                    getResources().getColor(PieChartView.COLOR_DARK_ARR[colorIndex]));
        } else if (colorIndex == num + 1 || (num == mSize - 1 && colorIndex == 0)) {
            return (int) PieChartView.ARGB_EVALUATOR.evaluate(fraction,
                    getResources().getColor(PieChartView.COLOR_DARK_ARR[colorIndex]),
                    getResources().getColor(PieChartView.COLOR_ARR[colorIndex]));
        }
        return getResources().getColor(PieChartView.COLOR_DARK_ARR[colorIndex]);
    }

    public void setOffset(float offset) {
        if (mOffset != offset) {
            mOffset = offset;
            invalidate();
        }
    }

    public void setSize(int size) {
        mSize = size;
    }

    public void setPivot(int pivot) {
        if (!isShown() || getAlpha() == 0) {
            if (mPivot != pivot) {
                mPivot = pivot;
                mFuturePivot = pivot;
                invalidate();
            }
        } else {
            mFuturePivot = pivot;
        }
    }

    public void updatePivot() {
        if (mPivot != mFuturePivot) {
            mPivot = mFuturePivot;
            invalidate();
        }
    }
}
