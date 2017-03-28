package com.assistne.dribbble.stepper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by assistne on 17/3/28.
 */

public class StepperView extends View {

    private int mBgColor = Color.argb(51, 255, 255, 255);
    private Paint mPaint;
    private Path mBgPath;

    public StepperView(Context context) {
        this(context, null);
    }

    public StepperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepperView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
    }

    private void drawBackground(Canvas canvas) {
        mPaint.setColor(mBgColor);
        if (mBgPath == null) {
            int r = getMeasuredHeight() / 2;
            int lineLen = getMeasuredWidth() - getMeasuredHeight();
            mBgPath = new Path();
            mBgPath.addRect(r, 0, getMeasuredWidth() - r, getMeasuredHeight(), Path.Direction.CW);
            RectF rectF = new RectF(lineLen, 0, getMeasuredWidth(), getMeasuredHeight());
            mBgPath.addArc(rectF, -90, 180);
            rectF.offsetTo(0, 0);
            mBgPath.addArc(rectF, 90, 180);
        }
        canvas.drawPath(mBgPath, mPaint);
    }
}
