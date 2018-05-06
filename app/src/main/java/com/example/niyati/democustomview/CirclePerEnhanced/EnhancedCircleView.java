package com.example.niyati.democustomview.CirclePerEnhanced;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.niyati.democustomview.SizeUtil;

public class EnhancedCircleView extends View {

    //半径
    final float radius = SizeUtil.Dp2Px(getContext(),150);

    private Paint mArcBackPaint,mArcForePaint,mLinePaint,mProgressTextPaint,mLabelTextPaint;
    private Rect mTextRect;
    private RectF mArcRectF;

    private float mArcWidth = SizeUtil.Dp2Px(getContext(),8);
    private int mScaleCount = 24;
    private int mStartColor = Color.parseColor("#3FC199");
    private int mEndColor = Color.parseColor("#3294C1");
    private int[] mColorArray = new int[]{mStartColor,mEndColor};
    private String mLabelText = "进度";
    private int mTextColor = Color.parseColor("#4F5F6F");
    private float mProgressTextSize = 160;
    private float mLabelTextSize = 64;

    private float mSweepAngle;
    private float mProgress;
    public EnhancedCircleView(Context context) {
        this(context,null);
    }

    public EnhancedCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EnhancedCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mArcBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcBackPaint.setStyle(Paint.Style.STROKE);
        mArcBackPaint.setStrokeWidth(mArcWidth);
        mArcBackPaint.setColor(Color.LTGRAY);

        mArcForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcForePaint.setStyle(Paint.Style.STROKE);
        mArcForePaint.setStrokeWidth(mArcWidth);

        mArcRectF = new RectF();

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(SizeUtil.Dp2Px(context,2));

        mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressTextPaint.setStyle(Paint.Style.FILL);
        mProgressTextPaint.setColor(Color.GRAY);
        mProgressTextPaint.setTextSize(mProgressTextSize);

        mLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelTextPaint.setStyle(Paint.Style.FILL);
        mLabelTextPaint.setColor(Color.RED);
        mLabelTextPaint.setTextSize(mLabelTextSize);

        mTextRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;


        //北京弧线
        mArcRectF.set(centerX - radius, centerY - radius,centerX + radius, centerY +radius);
        canvas.drawArc(mArcRectF,-90,360,false,mArcBackPaint);

        //渐变渲染
        LinearGradient linearGradient = new LinearGradient(getWidth()/2,0,getWidth()/2,getHeight(),mColorArray,null, Shader.TileMode.CLAMP);
        mArcForePaint.setShader(linearGradient);

        //百分比弧线
        canvas.drawArc(mArcRectF,-90,mSweepAngle,false,mArcForePaint);

        for (int i=0; i<mScaleCount; i++){
            canvas.drawLine(centerX, centerY - radius - mArcWidth/2, centerX, centerY - radius + mArcWidth/2, mLinePaint);
            canvas.rotate(360/mScaleCount,centerX,centerY);
        }

        String progressText = mProgress + "%";
        mProgressTextPaint.getTextBounds(progressText,0,progressText.length(),mTextRect);
        float progressTextWidth = mTextRect.width();
        float progressTextHeight= mTextRect.height();
        canvas.drawText(progressText,centerX - progressTextWidth/2,
                getHeight()/2 + progressTextHeight/2,mProgressTextPaint);

        mLabelTextPaint.getTextBounds(mLabelText,0,mLabelText.length(),mTextRect);
        canvas.drawText(mLabelText,centerX - mTextRect.width()/2,centerY - progressTextHeight/2 - mTextRect.height(),mLabelTextPaint);

    }
    public void setProgress(float progress){
        mSweepAngle = mProgress * 360/100;
        mProgress = (float) (Math.round(progress * 10))/10;
        invalidate();
    }

    public float getProgress() {
        return mProgress;
    }
}
