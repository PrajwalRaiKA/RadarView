package com.example.searchcircularbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class CustomView extends View {


    private int width;
    private int height;
    private int cy;
    private int cx;
    private int outerRadius;
    private int centerRadius;
    private int innerRadius;
    private Paint circleOuterRing, circleCenterRing, circleInnerRing, handPaint;
    private RectF rectF;
    private int degCount = 0;
    private static final int STROKE_WIDTH = 20;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet abs) {
        TypedArray arr = context.obtainStyledAttributes(abs, R.styleable.CustomView);
        arr.recycle();

        circleOuterRing = new Paint();
        circleCenterRing = new Paint();
        circleInnerRing = new Paint();
        handPaint = new Paint();

        circleOuterRing.setColor(Color.parseColor("#D8D8D8"));
        circleOuterRing.setStrokeWidth(2);
        circleOuterRing.setStyle(Paint.Style.STROKE);

        circleCenterRing.setColor(Color.parseColor("#D800D8"));
        circleCenterRing.setStrokeWidth(2);
        circleCenterRing.setStyle(Paint.Style.STROKE);

        circleInnerRing.setColor(Color.parseColor("#D8D800"));
        circleInnerRing.setStrokeWidth(2);
        circleInnerRing.setStyle(Paint.Style.STROKE);

        handPaint.setColor(Color.parseColor("#80D8D800"));
        handPaint.setStrokeWidth(50);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
        height = getHeight();
        int measuredHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int measuredWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        calculateCenterAndRadius();
    }

    private void calculateCenterAndRadius() {
        cx = width / 2;
        cy = height / 2;

        int size = (width > height) ? height : width;

        outerRadius = size / 2;
        centerRadius = size / 2 - size / 8;
        innerRadius = size / 2 - size / 4;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        calculateCenterAndRadius();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (rectF == null) {
            // take the minimum of width and height here to be on he safe side:


            // mRect will define the drawing space for drawArc()
            // We have to take into account the STROKE_WIDTH with drawArc() as well as drawCircle():
            // circles as well as arcs are drawn 50% outside of the bounds defined by the radius (radius for arcs is calculated from the rectangle mRect).
            // So if mRect is too large, the lines will not fit into the View
            int startTop = 20 / 2;
            int startLeft = startTop;

            int endBottom = 2 * outerRadius - startTop;
            int endRight = endBottom;

            rectF = new RectF(startLeft, startTop, endRight, endBottom);
        }
        canvas.drawCircle(cx, cy, outerRadius, circleOuterRing);
        canvas.drawCircle(cx, cy, centerRadius, circleCenterRing);
        canvas.drawCircle(cx, cy, innerRadius, circleInnerRing);
        if (degCount >= 115*2) {
            degCount = 0;
        }
        drawHand(canvas, degCount++);
        postInvalidateDelayed(30);
//        invalidate();
    }

    private void drawHand(Canvas canvas, double loc) {
        double angle = Math.PI * (loc/2)  - Math.PI / 2;
        /*canvas.drawLine(width / 2, height / 2,
                (float) (width / 2 + Math.cos(angle) * outerRadius),
                (float) (height / 2 + Math.sin(angle) * outerRadius),
                handPaint);*/

        canvas.drawArc(rectF, (float) angle, 45, true, handPaint);
    }
}
