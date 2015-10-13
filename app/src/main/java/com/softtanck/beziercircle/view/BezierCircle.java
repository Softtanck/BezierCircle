package com.softtanck.beziercircle.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.softtanck.beziercircle.bean.HPoint;
import com.softtanck.beziercircle.bean.VPoint;

/**
 * @author : Tanck
 * @Description : TODO
 * @date 10/12/2015
 */
public class BezierCircle extends View {
    /** 路径 */
    private Path mPath;
    /** 画笔*/
    private Paint mFillCirclePaint;
    /** 四个点*/
    private VPoint p2;
    private VPoint p4;
    private HPoint p1;
    private HPoint p3;
    /** 半径*/
    private int radius;
    private float c;
    private float blackMagic = 0.551915024494f;
    private float mInterpolatedTime;
    private float stretchDistance;

    public BezierCircle(Context context) {
        this(context, null);
    }

    public BezierCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化操作
     */
    private void init() {
        mFillCirclePaint = new Paint();
        mFillCirclePaint.setColor(0xFFFFFFFF);//fe626d);
        mFillCirclePaint.setStyle(Paint.Style.FILL);
        mFillCirclePaint.setStrokeWidth(1);
        mFillCirclePaint.setAntiAlias(true);
        mPath = new Path();
        p2 = new VPoint();
        p4 = new VPoint();

        p1 = new HPoint();
        p3 = new HPoint();
        radius = 20;
        c = radius * blackMagic;
        stretchDistance = radius;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        canvas.translate(radius + 10, getHeight() / 2);
        if (mInterpolatedTime >= 0 && mInterpolatedTime <= 0.2) {
            CircleSpecLast(mInterpolatedTime);//圆 -> 右边尖圆
        } else {
            updateRadius(10);
            CircleModel();
            movePosition();
            drawCircle(canvas);
            updateRadius(20);
            CircleModel();
        }
        drawCircle(canvas);
    }

    /**
     * 更新半径
     *
     * @param r
     */
    private void updateRadius(int r) {
        radius = r;
        c = radius * blackMagic;
    }

    /**
     * 移动位置
     */
    private void movePosition() {
        float offset = (getWidth() - 3 * radius - 10) * (mInterpolatedTime - 0.2f);
        offset = offset > 0 ? offset : 0;
        offset = offset + 2 * radius + 10;
        p1.adjustAllX(offset);
        p2.adjustAllX(offset);
        p3.adjustAllX(offset);
        p4.adjustAllX(offset);
    }

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPath.moveTo(p1.x, p1.y);
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);
        canvas.drawPath(mPath, mFillCirclePaint);
        mPath.reset();
    }

    private void CircleSpecLast(float time) {//0~0.2
        CircleModel();
        p2.setX(radius + stretchDistance * time * 5); // 改变最右边的点x
    }

    private void CircleModel() {
        // p2.p4属于圆左右两点

        p1.setY(radius);//右边
        p3.setY(-radius);// 左边
        p3.x = p1.x = 0;//圆心
        p3.left.x = p1.left.x = -c;
        p3.right.x = p1.right.x = c;

        //p1.p3属于圆的上下两点
        p2.setX(radius); // 下边
        p4.setX(-radius);// 上边
        p2.y = p4.y = 0;//圆心
        p2.top.y = p4.top.y = -c;
        p2.bottom.y = p4.bottom.y = c;
    }


    /**
     * 移动动画类
     */
    private class MoveAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
        }

    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        mPath.reset();
        mInterpolatedTime = 0;
        MoveAnimation move = new MoveAnimation();
        move.setDuration(3000);
        move.setInterpolator(new AccelerateDecelerateInterpolator());
        startAnimation(move);
    }
}
