package com.assistne.dribbble.bezierindicator;

import android.graphics.PointF;

/**
 * 竖直方向上的点
 * Created by assistne on 16/9/2.
 */
public class VerticalBezierPoint {
    public PointF dataPoint;
    public PointF leftControlPoint;
    public PointF rightControlPoint;

    public VerticalBezierPoint(float dataPointX, float dataPointY, float distance) {
        dataPoint = new PointF(dataPointX, dataPointY);
        leftControlPoint = new PointF(dataPointX - distance, dataPointY);
        rightControlPoint = new PointF(dataPointX + distance, dataPointY);
    }

    public void setPoint(float dataPointX, float dataPointY, float distance) {
        dataPoint.set(dataPointX, dataPointY);
        leftControlPoint.set(dataPointX - distance, dataPointY);
        rightControlPoint.set(dataPointX + distance, dataPointY);
    }

    public void movePoint(float offset) {
        dataPoint.x += offset;
        leftControlPoint.x += offset;
        rightControlPoint.x += offset;
    }

    public void adjustControlPoint(float offset) {
        leftControlPoint.y -= offset;
        rightControlPoint.y += offset;
    }
}
