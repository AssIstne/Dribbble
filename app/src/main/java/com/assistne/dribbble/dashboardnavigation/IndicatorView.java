package com.assistne.dribbble.dashboardnavigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by assistne on 2017/2/26.
 */

public class IndicatorView extends View {
    private Paint mPaint;
    private float mOffset;
    private int mOffsetPixel;
    private RectF mRectF;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHeadOrTail(canvas);
        drawFirst(canvas);
        drawSecond(canvas);
    }

    private void drawHeadOrTail(Canvas canvas) {
        final float width = getMeasuredWidth() * 2 / 5;
        final float height = getMeasuredHeight();
        final float top = 0;
        if (mOffsetPixel < 0.5f * width) {
            mPaint.setColor(getHeadColor());
            mRectF.set(-width/2, top, width/2, height);
            mRectF.offset(-mOffsetPixel, 0);
            canvas.drawRoundRect(mRectF, width/2, width/2, mPaint);
        } else if (mOffsetPixel > 0.75f * width){
            mPaint.setColor(getTailColor());
            mRectF.set(getMeasuredWidth()+0.75f*width, top, getMeasuredWidth()+0.75f*width+width, height);
            mRectF.offset(-mOffsetPixel, 0);
            canvas.drawRoundRect(mRectF, width/2, width/2, mPaint);
        }
    }

    private void drawFirst(Canvas canvas) {
        final float width = getMeasuredWidth() * 2 / 5;
        final float height = getMeasuredHeight();
        final float top = 0;
        mPaint.setColor(getFirstColor());
        mRectF.set(0.75f*width, top, 0.75f*width+width, height);
        mRectF.offset(-mOffsetPixel, 0);
        canvas.drawRoundRect(mRectF, width/2, width/2, mPaint);
    }

    private void drawSecond(Canvas canvas) {
        final float width = getMeasuredWidth() * 2 / 5;
        final float height = getMeasuredHeight();
        final float top = 0;
        mPaint.setColor(getSecondColor());
        mRectF.set(getMeasuredWidth()-0.5f*width, top, getMeasuredWidth()+0.5f*width, height);
        mRectF.offset(-mOffsetPixel, 0);
        canvas.drawRoundRect(mRectF, width/2, width/2, mPaint);
    }

    private int getHeadColor() {
        int currentIndex = (int) mOffset;
        return getResources().getColor(PieChartView.COLOR_DARK_ARR[currentIndex-1<0?PieChartView.COLOR_DARK_ARR.length:currentIndex-1]);
    }

    private int getTailColor() {
        int currentIndex = (int) mOffset;
        return getResources().getColor(PieChartView.COLOR_DARK_ARR[currentIndex+2<PieChartView.COLOR_DARK_ARR.length?currentIndex+2:0]);
    }

    private int getFirstColor() {
        int num = (int) mOffset;
        float fraction = mOffset - num;
        return (int) PieChartView.ARGB_EVALUATOR.evaluate(fraction,
                getResources().getColor(PieChartView.COLOR_ARR[num]),
                getResources().getColor(PieChartView.COLOR_DARK_ARR[num]));
    }

    private int getSecondColor() {
        int num = (int) mOffset;
        float fraction = mOffset - num;
        int index = num == PieChartView.COLOR_ARR.length - 1 ? 0 : num + 1;
        return (int) PieChartView.ARGB_EVALUATOR.evaluate(fraction,
                getResources().getColor(PieChartView.COLOR_DARK_ARR[index]),
                getResources().getColor(PieChartView.COLOR_ARR[index]));
    }

    public void setOffset(float offset) {
        if (mOffset != offset) {
            mOffset = offset;
            int num = (int) offset;
            float fraction = offset - num;
            mOffsetPixel = (int) (getMeasuredWidth() * fraction / 2);
            invalidate();
        }
    }
}
