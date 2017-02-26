package com.assistne.dribbble.dashboardnavigation;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 17/2/23.
 */

public class PieChartView extends View {
    private static final String TAG = "#PieChartView";
    private float[] mData = new float[] {1};
    private float[] mDegreeArr = new float[] {360};
    private float mTotal = 0;
    public static final ArgbEvaluator ARGB_EVALUATOR = new ArgbEvaluator();
    @ColorRes
    public static final int[] COLOR_ARR = new int[] {R.color.dn_blue, R.color.dn_green, R.color.dn_red, R.color.dn_yellow, R.color.dn_purple};
    @ColorRes
    private static final int[] mColorDarkArr = new int[] {R.color.dn_blue_dark, R.color.dn_green_dark, R.color.dn_red_dark, R.color.dn_yellow_dark, R.color.dn_purple_dark};

    private Paint mPaint;
    private int mCircleMargin;// 小同心圆半径
    private int mOffset;// 圆心位移
    private float mRotateOffset;
    private float mRotateDegree;

    private Path mUnderPath;
    private Path mOuterPath;
    private Path mInnerPath;
    private RectF mRect;
    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mTotal = total();
        mUnderPath = new Path();
        mOuterPath = new Path();
        mInnerPath = new Path();

        mRect = new RectF();
    }

    public void setData(@NonNull float[] data) {
        if (data.length > 5) {
            throw new IllegalArgumentException("can not bigger than 5.");
        }
        if (data.length >= 1) {
            mData = data;
            mTotal = total();
            calculateDegree();
            invalidate();
        }
    }

    private void calculateDegree() {
        if (mTotal > 0) {
            mDegreeArr = new float[mData.length];
            for (int i = 0; i < mData.length; i++) {
                mDegreeArr[i] = getFraction(mData[i]) * 360;
            }
        }
    }

    private float total() {
        float total = 0;
        if (mData != null && mData.length > 0) {
            for (float i : mData) {
                if (i > 0) {
                    total += i;
                }
            }
        }
        return total;
    }

    private float getFraction(float value) {
        return value <= 0 ? -1 : value/mTotal;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mCircleMargin = getMeasuredWidth()/9;
        mOffset = getMeasuredWidth()/80;
        mUnderPath.reset();
        mInnerPath.reset();
        mUnderPath.addCircle(getWidth()/2, getHeight()/2, getWidth()/2, Path.Direction.CW);
        mInnerPath.addCircle(getWidth()/2, getHeight()/2, mCircleMargin, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getAlpha() == 0) {
            return;
        }
        int sum = 0;
        for (int i = 0; i < mDegreeArr.length; i++) {
            canvas.save();
            float partRotate = i > 0 ? -(sum + mDegreeArr[i]/2) : 0;
            canvas.rotate(partRotate + mRotateDegree, getMeasuredWidth()/2, getMeasuredHeight()/2);
            drawPieAt(i, canvas);
            canvas.restore();
            if (i == 0) {
                sum += mDegreeArr[0]/2;
            } else {
                sum += mDegreeArr[i];
            }
        }
    }

    private void drawPieAt(int index, Canvas canvas) {
        final float degree = mDegreeArr[index];
        mPaint.setColor(getColorAccordingTo(index));
        mOuterPath.moveTo(canvas.getWidth()/2, canvas.getHeight()/2);
        float beginDegree = 90 - degree/2;
        mOuterPath.arcTo(mRect, beginDegree, degree, false);
        mOuterPath.offset(0, (float) (mOffset /Math.sin(degree/360 * Math.PI)));
        mOuterPath.close();
        mOuterPath.op(mUnderPath, Path.Op.INTERSECT);
        mOuterPath.op(mInnerPath, Path.Op.DIFFERENCE);
        canvas.save();
        canvas.drawPath(mOuterPath, mPaint);
        canvas.restore();
        mOuterPath.reset();
    }

    private int getColorAccordingTo(int index) {
        int num = (int) mRotateOffset;
        float fraction = mRotateOffset - num;
        if (index != num && index != num + 1) {
            return getResources().getColor(mColorDarkArr[index]);
        } else {
            return (int) ARGB_EVALUATOR.evaluate(index == num ? fraction : 1 - fraction, getResources().getColor(COLOR_ARR[index]),
                    getResources().getColor(mColorDarkArr[index]));
        }
    }

    public void rotateChart(float offset) {
        mRotateOffset = offset;
        int num = (int) offset;
        float fraction = offset - num;
        if (num >= 0 && fraction > 0 && num < mData.length - 1) {
            float part = 0;// 整数部分对应的度数
            if (num > 0) {
                part += (mDegreeArr[0]/2 + mDegreeArr[num]/2);// 头尾总是只有一半
                for (int i = 1; i < num; i++) {// 中间部分包含全部
                    part += mDegreeArr[i];
                }
            }
            // 小数部分对应的度数总数
            final float degreeRange = mDegreeArr[num]/2 + mDegreeArr[num+1]/2;
            // 小数*总度数 + 整数总度数
            mRotateDegree = part + degreeRange * fraction;
            if (isShown() && getAlpha() != 0f) {
                invalidate();
            }
        }
    }
}
