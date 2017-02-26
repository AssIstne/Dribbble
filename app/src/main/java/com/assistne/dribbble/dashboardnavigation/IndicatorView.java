package com.assistne.dribbble.dashboardnavigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by assistne on 2017/2/26.
 */

public class IndicatorView extends View {
    private Paint mPaint;
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final float width = getMeasuredWidth() * 2 / 5;
        final float height = getMeasuredHeight();
        final float top = 0;
        mPaint.setColor(Color.RED);
        canvas.drawRect(0, top, width/2, height, mPaint);
        canvas.drawRect(width*0.75f, top, width*0.75f+width, height, mPaint);
        canvas.drawRect(getMeasuredWidth() - width/2, top, getMeasuredWidth(), height, mPaint);
    }
}
