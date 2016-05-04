package com.assistne.dribbble.framestday004;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
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
                return child.getId() == mTargetId;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                mHelper.smoothSlideViewTo(releasedChild, 0, 0);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                Log.d(TAG, "clampViewPositionVertical: " + dy);
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - child.getHeight();
                return Math.min(Math.max(top, topBound), bottomBound);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                Log.d(TAG, "onViewPositionChanged: " + top);
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
}
