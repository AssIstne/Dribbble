package com.assistne.dribbble.dashboardnavigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 17/2/22.
 */

public class CollapsingLayout extends RelativeLayout {
    private static final String TAG = "#CollapsingLayout";
    private WindowInsetsCompat mLastInsets;
    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
    private Paint mPaint;
    private RectF mCoverRectF;
    private float mCoverScale;
    private boolean mHasCover;
    private float mTranslateY;
    private float mLine;

    public CollapsingLayout(Context context) {
        this(context, null);
    }

    public CollapsingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ViewCompat.setOnApplyWindowInsetsListener(this,
                new android.support.v4.view.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v,
                                                                  WindowInsetsCompat insets) {
                        return onWindowInsetChanged(insets);
                    }
                });
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GRAY);
    }

    private WindowInsetsCompat onWindowInsetChanged(final WindowInsetsCompat insets) {
        WindowInsetsCompat newInsets = null;

        if (ViewCompat.getFitsSystemWindows(this)) {
            // If we're set to fit system windows, keep the insets
            newInsets = insets;
        }

        // If our insets have changed, keep them and invalidate the scroll ranges...
        if (mLastInsets != newInsets && (mLastInsets != null && mLastInsets.equals(newInsets))) {
            mLastInsets = newInsets;
            requestLayout();
        }
        return insets;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Add an OnOffsetChangedListener if possible
        final ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            // Copy over from the ABL whether we should fit system windows
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View) parent));

            if (mOnOffsetChangedListener == null) {
                mOnOffsetChangedListener = new OffsetUpdateListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(mOnOffsetChangedListener);

            // We're attached, so lets request an inset dispatch
            ViewCompat.requestApplyInsets(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // Remove our OnOffsetChangedListener if possible and it exists
        final ViewParent parent = getParent();
        if (mOnOffsetChangedListener != null && parent instanceof AppBarLayout) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(mOnOffsetChangedListener);
        }

        super.onDetachedFromWindow();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // Update our child view offset helpers
        for (int i = 0, z = getChildCount(); i < z; i++) {
            final View child = getChildAt(i);

            if (mLastInsets != null && !ViewCompat.getFitsSystemWindows(child)) {
                final int insetTop = mLastInsets.getSystemWindowInsetTop();
                if (child.getTop() < insetTop) {
                    // If the child isn't set to fit system windows but is drawing within the inset
                    // offset it down
                    ViewCompat.offsetTopAndBottom(child, insetTop);
                }
            }

            getViewOffsetHelper(child).onViewLayout();
        }
        mLine = (getHeight() - getMinimumHeight())*0.6f;
    }

    private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
        @Override
        public void onOffsetChanged(AppBarLayout layout, int verticalOffset) {
            for (int i = 0, z = getChildCount(); i < z; i++) {
                final View child = getChildAt(i);
                final ViewOffsetHelper offsetHelper = getViewOffsetHelper(child);
                int offset;
                if ("pin".equals(child.getTag())) {
                    offset = constrain(-verticalOffset, 0, getMaxOffsetForPinChild(child));
                } else {
                    offset = Math.round(-verticalOffset * 0.5f);
                }
                offsetHelper.setTopAndBottomOffset(offset);
                float fraction = Math.abs((float) verticalOffset/mLine);
                fraction = Math.min(fraction, 1f);
                if (child instanceof PieChartView) {
                    child.setAlpha(1- fraction);
                    float scale = (1 - fraction) * 0.7f + 0.3f;
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                }
                boolean oldCover= mHasCover;
                mHasCover = fraction > 0.05f;
                if (child instanceof IndicatorView && verticalOffset != 0) {
                    child.setAlpha(1- fraction);
                    float scaleIndicator = 1 - fraction * 0.5f;
                    mCoverScale = scaleIndicator;
                    ((IndicatorView) child).setScale(scaleIndicator);
                    mPaint.setColor(((IndicatorView) child).getCurrentColor());
                    mCoverRectF = ((IndicatorView) child).getCurrentIndicatorRect();
                    mCoverRectF.offset(0, child.getTop());
                    if (Math.abs(verticalOffset) >= mLine) {
                        float tFraction = (Math.abs(verticalOffset) - mLine) / (getHeight()-getMinimumHeight()-mLine);
                        mTranslateY = getResources().getDimensionPixelSize(R.dimen.dn_total_translate) * tFraction;
                    }
                }
                if (mHasCover || oldCover != mHasCover) {
                    invalidate(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                }

                if (child.getId() == R.id.title) {
                    float tFraction = (Math.abs(verticalOffset) - mLine) / (getHeight()-getMinimumHeight()-mLine);
                    if (Math.abs(verticalOffset) >= mLine) {
                        child.setScaleY(1- 0.5f * tFraction);
                        child.setScaleX(1 - tFraction * 0.27f);
                    }
                    if (tFraction > 0.5f) {
                        child.setAlpha(2 - 2 * tFraction);
                    }
                }
            }
        }
    }

    private static ViewOffsetHelper getViewOffsetHelper(View view) {
        ViewOffsetHelper offsetHelper = (ViewOffsetHelper) view.getTag(android.support.design.R.id.view_offset_helper);
        if (offsetHelper == null) {
            offsetHelper = new ViewOffsetHelper(view);
            view.setTag(android.support.design.R.id.view_offset_helper, offsetHelper);
        }
        return offsetHelper;
    }


    final int getMaxOffsetForPinChild(View child) {
        final ViewOffsetHelper offsetHelper = getViewOffsetHelper(child);
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return getHeight()
                - offsetHelper.getLayoutTop()
                - child.getHeight()
                - lp.bottomMargin;
    }

    static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHasCover && mCoverRectF != null) {
            canvas.save();
            canvas.translate(0, mTranslateY);
            canvas.scale(mCoverScale, mCoverScale, mCoverRectF.centerX(), mCoverRectF.centerY());
            canvas.drawRoundRect(mCoverRectF, mCoverRectF.width(), mCoverRectF.width(), mPaint);
            canvas.restore();
        }
    }
}
