package com.assistne.dribbble.yahooweather;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 16/8/18.
 */
public class YHWeatherContainer extends FrameLayout {
    private static final String TAG = "#YHWeatherContainer";
    private ViewDragHelper mViewDragHelper;
    private int mOriginTop;
    View mDragView;
    private DragListener mListener;
    private int mHeight;

    public YHWeatherContainer(Context context) {
        this(context, null);
    }

    public YHWeatherContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YHWeatherContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewDragHelper = ViewDragHelper.create(this, 0.3f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (mOriginTop == 0) {
                    mDragView = findViewById(R.id.yahoo_span_content);
                    mOriginTop = mDragView.getTop();
                    mHeight = getHeight() - mOriginTop;
                }
                return child.getId() == R.id.yahoo_span_content || ((View)child.getParent()).getId() == R.id.yahoo_span_content;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return mHeight;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                Log.d(TAG, "onViewPositionChanged: " + top);
                if (mListener != null) {
                    mListener.onDrag(1 - (float) top/(float) mOriginTop);
                }
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int target = top + dy;
                Log.d(TAG, "clampViewPositionVertical: " + target);
                return Math.max(0, Math.min(target, mOriginTop));
            }
        });
    }

    public void setListener(DragListener listener) {
        mListener = listener;
    }

    public interface DragListener {
        void onDrag(float fraction);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
