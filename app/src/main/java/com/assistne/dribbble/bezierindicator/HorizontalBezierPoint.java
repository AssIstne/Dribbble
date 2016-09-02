package com.assistne.dribbble.bezierindicator;

import android.graphics.PointF;

/**
 * 水平方向上的点
 * Created by assistne on 16/9/2.
 */
public class HorizontalBezierPoint {
    public PointF dataPoint;
    public PointF topControlPoint;
    public PointF bottomControlPoint;

    public HorizontalBezierPoint(float dataPointX, float dataPointY, float distance) {
        dataPoint = new PointF(dataPointX, dataPointY);
        topControlPoint = new PointF(dataPointX, dataPointY - distance);
        bottomControlPoint = new PointF(dataPointX, dataPointY + distance);
    }

    public void setPoint(float dataPointX, float dataPointY, float distance) {
        dataPoint.set(dataPointX, dataPointY);
        topControlPoint.set(dataPointX, dataPointY - distance);
        bottomControlPoint.set(dataPointX, dataPointY + distance);
    }

    public void movePoint(float offset) {
        dataPoint.x += offset;
        topControlPoint.x += offset;
        bottomControlPoint.x += offset;
    }

    public void adjustControlPoint(float offset) {
        topControlPoint.y -= offset;
        bottomControlPoint.y += offset;
    }
}
