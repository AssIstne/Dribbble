package com.assistne.dribbble.animation;

import android.graphics.RectF;

/**
 * Created by assistne on 16/4/21.
 */
public class PointContainer {

    //  线段粗细
    public static final float PAINT_WIDTH = 16;
    //  线段间隔
    public static final float LINE_MARGIN = 11;
    //  直线距竖直方向的夹角值
    public static final float SIN_ALPHA = 0.62f;
    public static final float COS_ALPHA = 0.78f;
    //  圆弧的角度范围(单位度)
    public static final float DEGREE_RANGE = 220f;
    //  圆弧的起始角度(单位度), 水平位置为0度
    public static final float START_DEGREE = 320f;
    //  直线长度
    public final float lineLength;
    //  弧度的圆心
    public final float circleCenterX;
    public final float circleCenterY;
    //  弧度的半径
    public final float circleRadius;
    //  直线起始位置
    public final float lineStartX;
    public final float lineStartY;
    //  直线的结束位置
    public final float lineEndX;
    public final float lineEndY;

    //  当前图形起点, 用作画起点圆点
    public float currentHeadX;
    public float currentHeadY;
    //  当前图形末端, 用作画末端圆点
    public float currentTailX;
    public float currentTailY;
    //  当前直线的动态位置
    public float currentLineX;
    public float currentLineY;
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

    public PointContainer(float lineStartX, float lineStartY, float lineLength,
                          float circleCenterX, float circleCenterY, float circleRadius) {
        this.lineStartX = lineStartX;
        this.lineStartY = lineStartY;
        this.lineLength = lineLength;
        //  直线的结束位置
        lineEndX = SIN_ALPHA * lineLength + lineStartX;
        lineEndY = COS_ALPHA * lineLength + lineStartY;

        this.circleCenterX = circleCenterX;
        this.circleCenterY = circleCenterY;
        this.circleRadius = circleRadius;
        circleRectF = new RectF(circleCenterX - circleRadius, circleCenterY - circleRadius,
                circleCenterX + circleRadius, circleCenterY + circleRadius);
        tailCircleRectF = new RectF();
    }

    public void showLine(float fraction) {
        float value = fraction * lineLength;
        currentTailX = currentLineX = SIN_ALPHA * value + lineStartX;
        currentTailY = currentLineY = COS_ALPHA * value + lineStartY;
        currentHeadX = lineStartX;
        currentHeadY = lineStartY;
    }

    public void showArc(float degree) {
        sweepDegree = degree;
        //  计算弧的动点位置, 用作图形末端
        float deltaDegree = (360f - START_DEGREE) - sweepDegree;// 40 -> 0 -> -180
        currentTailX = (float) (circleRadius * Math.cos(deltaDegree * Math.PI / 180)) + circleCenterX;
        currentTailY = circleCenterY - (float) (circleRadius * Math.sin(deltaDegree * Math.PI / 180));
    }

    public void showTailArc(float degree) {
        tailSweepDegree = degree;
    }

    public void hideLine(float fraction) {
        float value = fraction * lineLength;
        currentHeadX = currentLineX = SIN_ALPHA * value + lineStartX;
        currentHeadY = currentLineY = COS_ALPHA * value + lineStartY;
    }

    public void hideArc(float degree) {
        sweepDegree = degree;
        //  计算弧的动点位置, 用作图形起点
        float deltaDegree = (360f - START_DEGREE) - degree;// 40 -> 0 -> -180
        currentHeadX = (float) (circleRadius * Math.cos(deltaDegree * Math.PI / 180)) + circleCenterX;
        currentHeadY = circleCenterY - (float) (circleRadius * Math.sin(deltaDegree * Math.PI / 180));
    }

    public void showSpotLine(float fraction) {
        float value = fraction * lineLength;
        currentHeadX = currentTailX - SIN_ALPHA * value;
        currentHeadY = currentTailY - COS_ALPHA * value;
    }

    public void showSpotArc(float degree) {
        final float mirrorCenterX = lineStartX - circleRadius;
        final float mirrorCenterY = lineStartY;
        //  计算弧的动点位置, 用作图形末端
        final float deltaDegree = DEGREE_RANGE - degree;// 220 -> 0
        currentHeadX = (float) (circleRadius * Math.cos(deltaDegree * Math.PI / 180)) + mirrorCenterX;
        currentHeadY = mirrorCenterY - (float) (circleRadius * Math.sin(deltaDegree * Math.PI / 180));
    }

    public void setTailPoint(float circleX, float circleY, float radius) {
        tailCircleX = circleX;
        tailCircleY = circleY;
        tailRadius = radius;
        tailCircleRectF = new RectF(circleX - radius, circleY - radius, circleX + radius, circleY + radius);
    }
}
