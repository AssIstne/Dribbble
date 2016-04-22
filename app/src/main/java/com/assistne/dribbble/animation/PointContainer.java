package com.assistne.dribbble.animation;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by assistne on 16/4/21.
 */
public class PointContainer {
    private static final String TAG = "#PointContainer";
    //  线段粗细
    public static final float PAINT_WIDTH = 16;
    //  线段间隔
    public static final float LINE_MARGIN = 11;

    /**
     * Android角度, 顺时针为正, 0-360度*/
    public final float startDegree;
    public final float degreeRange;
    public final float sinAlpha;
    public final float cosAlpha;
    //  直线长度
    public final float lineLength;
    public final float lineXRange;
    //  弧度的圆心
    public final PointF circleCenter;
    //  弧度的半径
    public final float circleRadius;
    //  直线起始位置
    public final PointF lineStart;
    //  直线的结束位置
    public final PointF lineEnd;
    //  当前图形起点, 用作画起点圆点
    public PointF head = new PointF();
    //  当前图形末端, 用作画末端圆点
    public PointF tail = new PointF();
    //  当前直线的动态位置
    public PointF currentLine = new PointF();
    //  弧度扫过的角度(单位度)
    public float sweepDegree;

    public RectF circleRectF;

    public boolean hasTailArc;
    public RectF tailCircleRectF;
    public float tailStartDegree;
    public float tailSweepDegree;
    public float tailCircleX;
    public float tailCircleY;
    public float tailRadius;

    /**
     * @param lineDegree 直线与x轴正方向的夹角 0-180度*/
    public PointContainer(float lineStartX, float lineStartY, float lineLength, float lineDegree,
                          float circleCenterX, float circleCenterY, float circleRadius, float circleStartDegree,
                          float circleDegreeRange) {
        lineStart = new PointF(lineStartX, lineStartY);
        this.lineLength = lineLength;
        this.sinAlpha = (float) Math.sin(Math.toRadians(lineDegree));
        this.cosAlpha = (float) Math.cos(Math.toRadians(lineDegree));
        lineXRange = Math.abs(cosAlpha * lineLength);
        lineEnd = new PointF(sinAlpha * lineLength + lineStartX, cosAlpha * lineLength + lineStartY);
        this.startDegree = circleStartDegree;
        this.degreeRange = circleDegreeRange;
        circleCenter = new PointF(circleCenterX, circleCenterY);
        this.circleRadius = circleRadius;
        circleRectF = new RectF(circleCenterX - circleRadius, circleCenterY - circleRadius,
                circleCenterX + circleRadius, circleCenterY + circleRadius);
        tailCircleRectF = new RectF();
    }

    /**
     * @param fraction 正值表示向右移动, 负值向左*/
    public void showLine(float fraction) {
        float deltaX = fraction * lineXRange;
        currentLine.x = lineStart.x + deltaX;
        currentLine.y = line(deltaX, lineStart.y);
        tail.set(currentLine);
        head.set(lineStart);
    }

    public void showArc(float degree) {
        sweepDegree = degree;
        //  转换角度
        float deltaDegree = 360f - startDegree - sweepDegree;
        tail.set(circle(deltaDegree, circleCenter.x, circleCenter.y));
    }

    public void showTailArc(float degree) {
        tailSweepDegree = degree;
    }

    public void hideLine(float fraction) {
        float deltaX = fraction * lineXRange;
        currentLine.x = lineStart.x + deltaX;
        currentLine.y = line(deltaX, lineStart.y);
        head.set(currentLine);
    }

    public void hideArc(float degree) {
        sweepDegree = degree;
        //  转换角度
        float deltaDegree = 360f - startDegree - sweepDegree;
        head.set(circle(deltaDegree, circleCenter.x, circleCenter.y));
    }

    public void showSpotLine(float fraction) {
        float deltaX = fraction * lineXRange;
        head.x = tail.x + deltaX;
        head.y = line(deltaX, tail.y);
    }

    public void showSpotArc(float degree) {
        final float mirrorCenterX = lineStart.x - circleRadius;
        final float mirrorCenterY = lineStart.y;
        //  计算弧的动点位置, 用作图形末端
        final float deltaDegree = degreeRange - degree;// 220 -> 0
        head.set(circle(deltaDegree, mirrorCenterX, mirrorCenterY));
    }

    public void setTailPoint(float circleX, float circleY, float radius) {
        tailCircleX = circleX;
        tailCircleY = circleY;
        tailRadius = radius;
        tailCircleRectF = new RectF(circleX - radius, circleY - radius, circleX + radius, circleY + radius);
    }

    /**
     * 计算在直线范围内, 点从起始坐标水平移动deltaX距离后y的屏幕坐标系坐标*/
    private float line(float deltaX, float y) {
        return y - (sinAlpha / cosAlpha) * deltaX;
    }

    /**
     * 根据过坐标的半径与X轴正方向的夹角计算屏幕坐标值
     * @param degree 逆时针为正方向, 0-360度*/
    private PointF circle(float degree, float circleX, float circleY) {
        float x = (float) (circleX + circleRadius * Math.cos(Math.toRadians(degree)));
        float y = (float) (circleY - circleRadius * Math.sin(Math.toRadians(degree)));
        return new PointF(x, y);
    }
}
