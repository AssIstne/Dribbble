package com.assistne.dribbble.dashboardnavigation;

import android.content.Context;
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
    private int[] mDegreeArr = new int[] {360};
    private float mTotal = 0;
    @ColorRes
    private int[] mColorArr = new int[] {R.color.dn_blue, R.color.dn_green, R.color.dn_red, R.color.dn_yellow, R.color.dn_purple};
    @ColorRes
    private int[] mColorDarkArr = new int[] {R.color.dn_blue_dark, R.color.dn_green_dark, R.color.dn_red_dark, R.color.dn_yellow_dark, R.color.dn_purple_dark};

    private Paint mPaint;
    private int mBaseDegree = 0;
    private int CIRCLE_MARGIN = 10;// 圆心位移
    private int RADIUS = 200;// 扇形半径
    private int mCurrentIndex = 0;

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
            mDegreeArr = new int[mData.length];
            for (int i = 0; i < mData.length; i++) {
                mDegreeArr[i] = (int) (getFraction(mData[i]) * 360);
            }
        }
    }

    private int total() {
        int total = 0;
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
    }

}
