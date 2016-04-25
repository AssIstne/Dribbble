package com.assistne.dribbble.animation;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by assistne on 16/4/21.
 */
public class PointContainer {
    public boolean debug;
    public boolean isSpotDrawing;
    public boolean isDrawingLine;
    public boolean isShowing;
    public boolean isDrawingTail;

    private static final String TAG = "#PointContainer";
    //  线段粗细
    public static final float PAINT_WIDTH = 16;
    //  线段间隔
    public static final float LINE_MARGIN = 11;
    /**
     * 直线运动轨迹的角度, 在本图中为定值*/
    public static final float LINE_DEGREE = 51.5f;
    /**
     * 直线运动轨迹的斜率, 定值*/
    public static final float LINE_SLOPE = (float) Math.tan(Math.toRadians(LINE_DEGREE));
    /**
     * 直线起始的运动方向, 1或者-1, 正值表示开始向右运动, 负值表示向左*/
    public final float moveDirect;

    /**
     * Android角度, 顺时针为正, 0-360度*/
    public final float startDegree;
    public final float degreeRange;
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
    //  圆弧所属的矩形
    public RectF circleRectF;

    public PointF arcEnd;

    public boolean hasTailArc;
    public final float tailSweepRange = 90;
    public RectF tailCircleRectF;
    public float tailStartDegree;
    public float tailSweepDegree;
    private boolean mHasInitTailCircle;
    private float mTailCircleY;
    private float mTailCircleX;
    private float mTailRadius;
    private int tailDirect;// 1:270-360;2:180-270;3:90-180;4:0-90

    public PointContainer(float lineStartX, float lineStartY, float moveDirect,
                          float circleCenterX, float circleCenterY) {
        //  图形起点
        lineStart = new PointF(lineStartX, lineStartY);
        //  圆心坐标
        circleCenter = new PointF(circleCenterX, circleCenterY);
        //  直线的终点坐标为与过圆心垂直线的交点
        lineEnd = lineEnd(lineStartX, lineStartY, circleCenterX, circleCenterY);
        //  直线的长度
        float deltaX = Math.abs(lineEnd.x - lineStart.x);
        float deltaY = Math.abs(lineEnd.y - lineStart.y);
        this.lineLength = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        //  起始运动的方向
        this.moveDirect = moveDirect;
        //  X坐标变化的最大值
        lineXRange = (float) Math.abs(Math.cos(Math.toRadians(LINE_DEGREE)) * lineLength);
        //  过直线终点的半径的与水平轴的角度, 0-90度
        float deltaDegree = (float) Math.toDegrees(Math.atan(Math.abs(lineEnd.y - circleCenterY)/Math.abs(lineEnd.x - circleCenterX)));
        //  根据运动方向确定起始圆弧的起始角度
        if (moveDirect > 0) {
            startDegree = 360 - deltaDegree;
        } else {
            startDegree = 180 - deltaDegree;
        }
        //  圆弧的角度范围, 最终转到水平轴
        this.degreeRange = 180 + deltaDegree;
        //  圆弧的半径
        this.circleRadius = (float) (Math.abs(lineEnd.x - circleCenterX) / Math.cos(Math.toRadians(deltaDegree)));
        //  圆弧所属的矩形
        circleRectF = new RectF(circleCenterX - circleRadius, circleCenterY - circleRadius,
                circleCenterX + circleRadius, circleCenterY + circleRadius);
        tailCircleRectF = new RectF();
        arcEnd = new PointF();

        mHasInitTailCircle = false;
        isDrawingLine = true;
        isShowing = true;
        isSpotDrawing = false;
        isDrawingTail = false;

        currentLine.set(lineStart);
        head.set(lineStart);
        tail.set(lineStart);
    }

    /**
     * @param fraction 正值表示向右移动, 负值向左*/
    public void showLine(float fraction) {
        float deltaX = moveDirect * fraction * lineXRange;
        currentLine.x = lineStart.x + deltaX;
        currentLine.y = line(deltaX, lineStart.y);
        tail.set(currentLine);
        head.set(lineStart);
        isDrawingLine = true;
        isShowing = true;
        isSpotDrawing = false;
        isDrawingTail = false;
    }

    public void showArc(float fraction) {
        sweepDegree = degreeRange * fraction;
        //  转换角度
        float deltaDegree = 360f - startDegree - sweepDegree;
        tail.set(circle(deltaDegree, circleCenter.x, circleCenter.y, circleRadius));
        isDrawingLine = false;
        arcEnd.set(tail);
    }

    public void showTailArc(float fraction) {
        if (!hasTailArc || tailDirect < 1 || tailDirect > 4) {
            return;
        }
        tailSweepDegree = fraction * tailSweepRange;//  0-90
        //  当前点的角度
        float deltaDegree;
        //  注释的角度以逆时针, 竖直向上为90度
        if (tailDirect == 1) { //  逆时针从0-90
            tailStartDegree = 360 - tailSweepDegree;
            deltaDegree = tailSweepDegree;
        } else if (tailDirect == 2) { //  顺时针从180-90
            tailStartDegree = 180;
            deltaDegree = 180 - tailSweepDegree;
        } else if (tailDirect == 3) { //  逆时针从180-270
            tailStartDegree = 180 - tailSweepDegree;
            deltaDegree = 180 + tailSweepDegree;
        } else { //  顺时针从0-270
            tailStartDegree = 0;
            deltaDegree = 360 - tailSweepDegree;
        }
        if (!mHasInitTailCircle) {
            mTailCircleY = tail.y;
            mTailRadius = (PAINT_WIDTH + LINE_MARGIN) / 2;
            switch (tailDirect) {
                case 1:
                    mTailCircleX = tail.x - mTailRadius;
                    break;
                case 2:
                    mTailCircleX = tail.x + mTailRadius;
                    break;
                case 3:
                    mTailCircleX = tail.x + mTailRadius;
                    break;
                default:
                    mTailCircleX = tail.x - mTailRadius;
                    break;
            }
            tailCircleRectF.set(mTailCircleX - mTailRadius, mTailCircleY - mTailRadius, mTailCircleX + mTailRadius, mTailCircleY + mTailRadius);
            mHasInitTailCircle = true;
        }
        tail.set(circle(deltaDegree, mTailCircleX, mTailCircleY, mTailRadius));
        isDrawingTail = true;
    }

    public void hideLine(float fraction) {
        float deltaX = fraction * lineXRange * moveDirect;
        currentLine.x = lineStart.x + deltaX;
        currentLine.y = line(deltaX, lineStart.y);
        head.set(currentLine);
        isDrawingLine = true;
        isShowing = false;
    }

    public void hideArc(float fraction) {
        sweepDegree = degreeRange * fraction;
        //  转换角度
        float deltaDegree = 360f - startDegree - sweepDegree;
        head.set(circle(deltaDegree, circleCenter.x, circleCenter.y, circleRadius));
        isDrawingLine = false;
    }

    public void hideTailArc(float fraction) {
        if (!hasTailArc || tailDirect < 1 || tailDirect > 4) {
            return;
        }
        tailSweepDegree = 90 - fraction * tailSweepRange;
        //  当前点的角度
        float deltaDegree;
        //  注释的角度以逆时针, 竖直向上为90度
        if (tailDirect == 1) { //  顺时针270-360
            tailStartDegree = 360 - tailSweepDegree;
            deltaDegree = tailSweepDegree;
        } else if (tailDirect == 2) { //  逆时针从90-180
            tailStartDegree = 180;
            deltaDegree = 180 - tailSweepDegree;
        } else if (tailDirect == 3) { //  顺时针从270-180
            tailStartDegree = 180 - tailSweepDegree;
            deltaDegree = 180 + tailSweepDegree;
        } else { //  逆时针从270-0
            tailStartDegree = 0;
            deltaDegree = 360 - tailSweepDegree;
        }
        tail.set(circle(deltaDegree, mTailCircleX, mTailCircleY, mTailRadius));
        isSpotDrawing = false;
    }

    public void showSpotLine(float fraction) {
        float deltaX = -1 * moveDirect * fraction * lineXRange;
        head.x = tail.x + deltaX;
        head.y = line(deltaX, tail.y);
        isSpotDrawing = true;
        isDrawingTail = false;
    }

    public void showSpotArc(float fraction) {
        final float mirrorCenterX = lineStart.x - circleRadius * moveDirect;
        final float mirrorCenterY = lineStart.y;
        //  计算弧的动点位置, 用作图形末端
        final float deltaDegree;
        if (moveDirect > 0) {
           deltaDegree = degreeRange - degreeRange * fraction;// 220 -> 0
        } else {
            deltaDegree = degreeRange - 180 - degreeRange * fraction;
        }
        head.set(circle(deltaDegree, mirrorCenterX, mirrorCenterY, circleRadius));
    }

    public void showSpotCircle(float fraction) {
        float deltaDegree = fraction * 180;
        float cX = (arcEnd.x + lineStart.x)/2;
        float cY = (arcEnd.y + lineStart.y)/2;
        float deltaX = Math.abs(arcEnd.x - lineStart.x);
        float deltaY = Math.abs(arcEnd.y - lineStart.y);
        float radius = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY) / 2;
        float degree = (float) Math.toDegrees(Math.atan(deltaX / deltaY));
        if (moveDirect > 0) {
            head.set(circle(270 - deltaDegree - degree, cX, cY, radius));
        } else {
            head.set(circle(90 - degree - deltaDegree, cX, cY, radius));
        }
        isSpotDrawing = true;
        isDrawingTail = false;
    }

    public void setTailDirect(int direct) {
        hasTailArc = true;
        tailDirect = direct;
    }

    /**
     * 直线方程的斜率是固定的, 根据起点坐标得出方程, 根据位移得出当前Y坐标
     * 计算在直线范围内, 点从起始坐标水平移动deltaX距离后y的屏幕坐标系坐标
     * @param deltaX 距起始点的位移量, 区分正负
     * @param y 起点Y坐标
     * @return 直线上对应的Y坐标*/
    private float line(float deltaX, float y) {
        return y + LINE_SLOPE * deltaX;
    }

    private float line(float x1, float y1, float x) {
        return y1 + LINE_SLOPE * (x - x1);
    }

    private PointF lineEnd(float x1, float y1, float x2, float y2) {
        float x = (LINE_SLOPE * LINE_SLOPE * x1 - LINE_SLOPE * y1 + x2 + LINE_SLOPE * y2) / (LINE_SLOPE * LINE_SLOPE + 1);
        float y = line(x1, y1, x);
        return new PointF(x, y);
    }

    /**
     * 根据过坐标的半径与X轴正方向的夹角计算屏幕坐标值
     * @param degree 逆时针为正方向, 0-360度*/
    private PointF circle(float degree, float circleX, float circleY, float radius) {
        float x = (float) (circleX + radius * Math.cos(Math.toRadians(degree)));
        float y = (float) (circleY - radius * Math.sin(Math.toRadians(degree)));
        return new PointF(x, y);
    }

    public float arcLength() {
        return (float) (2 * Math.PI * circleRadius * (degreeRange / 360));
    }

    public void reset() {
        mHasInitTailCircle = false;
        isDrawingLine = true;
        isShowing = true;
        isSpotDrawing = false;
        isDrawingTail = false;

        sweepDegree = 0;
        head.set(lineStart);
        tail.set(lineStart);
        currentLine.set(lineStart);
        tailSweepDegree = 0;
    }
}
