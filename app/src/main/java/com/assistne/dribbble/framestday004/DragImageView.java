package com.assistne.dribbble.framestday004;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 16/5/4.
 */
public class DragImageView extends FrameLayout {
    private static final String TAG = "#DragImageView";

    @IdRes
    private int mTargetId;
    private ViewDragHelper mHelper;
    private DragCallback mListener;

    private boolean mIsOrigin;
    private int mOriginLeft;
    private int mOriginTop;
    private int mCurrentBottom;

    private boolean mDragging;
    private int mSpecifiedLine;

    public DragImageView(Context context) {
        this(context, null);
    }

    public DragImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DragImageView, defStyleAttr, 0);

        mTargetId = a.getResourceId(R.styleable.DragImageView_target, -1);
        a.recycle();

        mHelper = ViewDragHelper.create(this, initCallback());
    }

    private ViewDragHelper.Callback initCallback() {
        return new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (child.getId() == mTargetId) {
                    mDragging = false;
                    if (!mIsOrigin) {
                        mIsOrigin = true;
                        mOriginLeft = child.getLeft();
                        mOriginTop = child.getTop();
                        mSpecifiedLine = (int) (0.84 * getHeight());
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                mCurrentBottom = changedView.getBottom();
                if (mListener != null) {
                    if (Math.abs(dy) > 2 && !mDragging) {
                        mDragging = true;
                        mListener.onStartDragging();
                    }
                    mListener.onAboveSpecifiedPositionOrNOt(top + changedView.getHeight() > mSpecifiedLine);
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (mCurrentBottom > mSpecifiedLine) {
                    if (mListener != null) {
                        mListener.onReleaseSpecifiedPosition();
                    }
                } else {
                    if (mHelper.settleCapturedViewAt(mOriginLeft, mOriginTop)) {
                        ViewCompat.postInvalidateOnAnimation(DragImageView.this);
                    }
                    if (mListener != null) {
                        mListener.onReleaseOtherPosition();
                    }
                }
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - child.getHeight();
                return Math.min(Math.max(top, topBound), bottomBound);
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(DragImageView.this);
        }
    }

    public void setListener(DragCallback listener) {
        mListener = listener;
    }

    public interface DragCallback {
        void onReleaseOtherPosition();
        void onStartDragging();
        void onReleaseSpecifiedPosition();
        void onAboveSpecifiedPositionOrNOt(boolean isEnter);
    }
}
