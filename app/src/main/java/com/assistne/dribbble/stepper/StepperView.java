package com.assistne.dribbble.stepper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by assistne on 17/3/28.
 */

public class StepperView extends View {
    private static final String TAG = "#StepperView";
    private static final int BG_COLOR = Color.argb(51, 255, 255, 255);
    private static final int CIRCLE_COLOR = Color.WHITE;
    private static final int MARK_COLOR = Color.WHITE;
    private static final int CIRCLE_SHADOW_COLOR = Color.argb(51, 15, 46, 81);
    private static final int TEXT_COLOR = Color.rgb(109, 114, 255);
    private int mCircleCenterX;
    private Paint mBgPaint;
    private Paint mCirclePaint;
    private Paint mMarkPaint;
    private Path mBgPath;
    private Layout mNumberLayout;
    private int mNumber;
    private float mMinusScale = 1f;
    private float mPlusScale = 1f;
    private float mLastX;
    private boolean mPlusScaling;
    private ValueAnimator mPlusRunningAnimator;
    private boolean mMinusScaling;
    private ValueAnimator mMinusRunningAnimator;

    public StepperView(Context context) {
        this(context, null);
    }

    public StepperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepperView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(BG_COLOR);
        mMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarkPaint.setColor(MARK_COLOR);
        mMarkPaint.setStyle(Paint.Style.STROKE);
        mCirclePaint = new Paint(mBgPaint);
        mCirclePaint.setColor(CIRCLE_COLOR);
        mCirclePaint.setShadowLayer(24, 0, 0, CIRCLE_SHADOW_COLOR);
        // 为了绘制阴影
        setLayerType(LAYER_TYPE_SOFTWARE, mCirclePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                if (isInCircle(event)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInCircle(event)) {
                    final int threshold = getMeasuredHeight() / 6;
                    final int middle = getMeasuredWidth() / 2;
                    mCircleCenterX += (event.getX() - mLastX);
                    mLastX = event.getX();
                    invalidate();
                    int delta = (int) (event.getX() - middle);
                    if (delta >= 0) {// 在右侧
                        if (event.getX() - middle > threshold) {
                            scalePlusMark();
                        } else {
                            restorePlusMark();
                        }
                    } else {
                        if (-delta > threshold) {
                            scaleMinusMark();
                        } else {
                            restoreMinusMark();
                        }
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void scalePlusMark() {
        if (!mPlusScaling) {
            if (mPlusRunningAnimator != null && mPlusRunningAnimator.isRunning()) {
                mPlusRunningAnimator.cancel();
            }
            mPlusRunningAnimator = ValueAnimator.ofFloat(mPlusScale, 1.2f);
            mPlusRunningAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mPlusScale = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mPlusRunningAnimator.start();
            mPlusScaling = true;
        }
    }

    private void restorePlusMark() {
        if (mPlusScaling) {
            if (mPlusRunningAnimator != null && mPlusRunningAnimator.isRunning()) {
                mPlusRunningAnimator.cancel();
            }
            mPlusRunningAnimator = ValueAnimator.ofFloat(mPlusScale, 1f);
            mPlusRunningAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mPlusScale = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mPlusRunningAnimator.start();
            mPlusScaling = false;
        }
    }

    private void scaleMinusMark() {
        if (!mMinusScaling) {
            if (mMinusRunningAnimator != null && mMinusRunningAnimator.isRunning()) {
                mMinusRunningAnimator.cancel();
            }
            mMinusRunningAnimator = ValueAnimator.ofFloat(mMinusScale, 1.2f);
            mMinusRunningAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mMinusScale = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mMinusRunningAnimator.start();
            mMinusScaling = true;
        }
    }

    private void restoreMinusMark() {
        if (mMinusScaling) {
            if (mMinusRunningAnimator != null && mMinusRunningAnimator.isRunning()) {
                mMinusRunningAnimator.cancel();
            }
            mMinusRunningAnimator = ValueAnimator.ofFloat(mMinusScale, 1f);
            mMinusRunningAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mMinusScale = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mMinusRunningAnimator.start();
            mMinusScaling = false;
        }
    }

    private boolean isInCircle(MotionEvent event) {
        int circleCenterY = getMeasuredHeight() / 2;
        return circleCenterY * circleCenterY >= Math.pow(event.getX() - mCircleCenterX, 2) + Math.pow(event.getY() - circleCenterY, 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCircleCenterX = getMeasuredWidth() / 2;
        if (mNumberLayout == null) {
            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(TEXT_COLOR);
            textPaint.setTextSize(getMeasuredHeight() * 0.45f);
            mNumberLayout = new StaticLayout(String.valueOf(mNumber), textPaint, getMeasuredHeight(), Layout.Alignment.ALIGN_CENTER, 1, 0, true);
        }
        mMarkPaint.setStrokeWidth(getMeasuredHeight() / 30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawMinus(canvas);
        drawPlus(canvas);
        drawCircle(canvas);
    }

    private void drawBackground(Canvas canvas) {
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
        canvas.drawPath(mBgPath, mBgPaint);
    }

    private void drawCircle(Canvas canvas) {
        int r = getMeasuredHeight() / 2;
        canvas.save();
        canvas.clipPath(mBgPath);
        canvas.translate(mCircleCenterX - r, 0);
        canvas.drawCircle(r, r , r, mCirclePaint);
        canvas.translate(0, (getMeasuredHeight() - mNumberLayout.getHeight()) / 2);
        mNumberLayout.draw(canvas);
        canvas.restore();
    }

    private void drawPlus(Canvas canvas) {
        mMarkPaint.setColor(MARK_COLOR);
        int container = getMeasuredHeight() / 3;
        int padding = container / 5;
        int lineLen = container - 2 * padding;
        canvas.save();
        canvas.translate(getMeasuredWidth() - getMeasuredHeight() / 2, (getMeasuredHeight() - container) / 2);
        canvas.scale(mPlusScale, mPlusScale, container / 2, container / 2);
        canvas.drawLine(padding, container / 2, padding + lineLen, container / 2, mMarkPaint);
        canvas.drawLine(container / 2, padding, container / 2, padding + lineLen, mMarkPaint);
        canvas.restore();
    }

    private void drawMinus(Canvas canvas) {
        if (mNumber == 0) {
            mMarkPaint.setAlpha(51);
        } else {
            mMarkPaint.setColor(MARK_COLOR);
        }
        int padding = getMeasuredHeight() / 15;
        int startX = getMeasuredHeight() / 6 + padding;
        int startY = getMeasuredHeight() / 2;
        int lineLen = getMeasuredHeight() / 5;
        canvas.save();
        canvas.scale(mMinusScale, mMinusScale, startX + lineLen / 2, startY);
        canvas.drawLine(startX, startY, startX + lineLen, startY, mMarkPaint);
        canvas.restore();
    }
}
