package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ProgressView extends View {
    private Paint progressPaint;  // 进度条画笔
    private Paint textPaint;     // 文字画笔
    private float progress = 0;  // 当前进度值 (0-100)
    private int totalPages = 0;  // 累计页数

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 进度条画笔设置
        progressPaint = new Paint();
        progressPaint.setColor(0xFF4CAF50);
        progressPaint.setStrokeWidth(20);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setAntiAlias(true);

        // 文字画笔设置
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(36);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        setContentDescription("读书进度环形图");
    }

    public void setProgress(float progress) {
        this.progress = Math.min(progress, 100);
        invalidate();
    }

    public void setTotalPages(int pages) {
        this.totalPages = pages;
        invalidate();
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 计算环形图区域
        float padding = getWidth() * 0.1f;
        float circleSize = getWidth() - 2 * padding;
        RectF rect = new RectF(padding, padding, padding + circleSize, padding + circleSize);

        // 绘制环形进度条
        canvas.drawArc(rect, -90, 360 * progress / 100, false, progressPaint);

        // 计算中心坐标
        float centerX = getWidth() / 2f;

        // 绘制环形进度百分比
        textPaint.setTextSize(42);
        canvas.drawText(String.format("%.0f%%", progress), centerX, rect.centerY(), textPaint);

        // 绘制累计页数
        textPaint.setTextSize(36);
        float pageTextY = rect.bottom + dpToPx(40);
        canvas.drawText("累积页数：" + totalPages + "页", centerX, pageTextY, textPaint);

    }

    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}