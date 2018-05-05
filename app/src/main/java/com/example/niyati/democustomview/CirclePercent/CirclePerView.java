package com.example.niyati.democustomview.CirclePercent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Size;
import android.view.View;

import com.example.niyati.democustomview.SizeUtil;

public class CirclePerView extends View {

    final float radius = SizeUtil.Dp2Px(getContext(),80);

    RectF arcRecrF = new RectF();
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        mPaint.setTextSize(SizeUtil.Dp2Px(getContext(),40));
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    float progress = 0;

    public CirclePerView(Context context) {
        this(context,null);
    }

    public CirclePerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CirclePerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;

        mPaint.setColor(Color.YELLOW);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(SizeUtil.Dp2Px(getContext(),15));
        arcRecrF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        canvas.drawArc(arcRecrF,-90,progress * 3.6f, false,mPaint);

        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText((int)progress + "%",centerX,centerY-( mPaint.ascent() + mPaint.descent()) / 2, mPaint);
    }
}
