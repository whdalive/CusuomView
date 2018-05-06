package com.example.niyati.democustomview.MiuiClock;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.niyati.democustomview.SizeUtil;

import java.util.Calendar;
import java.util.TimerTask;

public class MiClock extends View {

    //文字的画笔和测量文本大小的矩形
    private Paint mTextPaint;
    private Rect mTextRect;

    //小时圆圈
    private Paint mCirclePaint;
    private float mCircleWidth = 2;
    private RectF mCircleRectF;

    //刻度相关
    private Paint mScaleArcPaint;
    private RectF mScaleArcRectF;
    private Paint mScaleLinePaint;

    //针的画笔
    private Paint mHourPaint;
    private Paint mMinPaint;
    private Paint mSecPaint;

    //针的路径
    private Path mHourPath;
    private Path mMinPath;
    private Path mSecPath;


    private int mLightColor,mDarkColor;
    private int mBgColor;

    private float mTextSize;
    private float mRadius;
    //刻度线长度
    private float mScaleLength = 0.12f * mRadius;

    private float mHourDegree;
    private float mMinDegree;
    private float mSecDegree;

    private SweepGradient mSweepGradient;
    private Matrix mGradientMatrix;

    private float centerX;
    private float centerY;
    private float mPadding;

    public MiClock(Context context) {
        this(context,null);
    }

    public MiClock(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MiClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBgColor = Color.parseColor("#237EAD");
        setBackgroundColor(mBgColor);

        mLightColor = Color.parseColor("#FFFFFF");
        mDarkColor = Color.parseColor("#80FFFFFF");
        mTextSize = SizeUtil.Dp2Px(context,14);

        mHourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourPaint.setStyle(Paint.Style.FILL);
        mHourPaint.setColor(mDarkColor);

        mMinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinPaint.setColor(mLightColor);
        mMinPaint.setStyle(Paint.Style.FILL);

        mSecPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecPaint.setColor(mLightColor);
        mSecPaint.setStyle(Paint.Style.FILL);

        mScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleLinePaint.setStyle(Paint.Style.STROKE);
        mScaleLinePaint.setColor(mBgColor);

        mScaleArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleArcPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mDarkColor);
        mTextPaint.setTextSize(mTextSize);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleWidth);
        mCirclePaint.setColor(mDarkColor);

        mTextRect = new Rect();
        mCircleRectF = new RectF();
        mScaleArcRectF = new RectF();
        mHourPath = new Path();
        mMinPath = new Path();
        mSecPath = new Path();


        mGradientMatrix = new Matrix();
    }
/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureDimension(widthMeasureSpec),measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY){
            result = size;
        }else {
            result = 800;
            if (mode == MeasureSpec.AT_MOST){
                result = Math.min(result,size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mRadius = Math.min(w - getPaddingLeft() - getPaddingRight(),
                h - getPaddingTop() - getPaddingBottom())/2;
        mDefaultPadding = 0.12f * mRadius;
        mPaddingLeft = mDefaultPadding + w/2 - mRadius + getPaddingLeft();
        mPaddingTop = mDefaultPadding + h / 2 - mRadius + getPaddingTop();
        mPaddingRight = mPaddingLeft;
        mPaddingBottom = mPaddingTop;

        mScaleLength = 0.12f * mRadius;
        mScaleArcPaint.setStrokeWidth(mScaleLength);
    }
*/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        mRadius = SizeUtil.Dp2Px(getContext(),150);
        mPadding = 1.2f * mRadius;
        mScaleLength = 0.12f * mRadius;
        mScaleArcPaint.setStrokeWidth(mScaleLength);
        mScaleLinePaint.setStrokeWidth(0.012f * mRadius);
        getTimeDegree();
        drawTimeText(canvas);
        drawScaleLine(canvas);
        drawSecHand(canvas);
        drawHourHand(canvas);
        drawMinHand(canvas);
        invalidate();
    }

    private void getTimeDegree() {
        Calendar calendar = Calendar.getInstance();
        float milliSec = calendar.get(Calendar.MILLISECOND);
        float sec = calendar.get(Calendar.SECOND) + milliSec/1000;
        float min = calendar.get(Calendar.MINUTE) + sec / 60;
        float hour = calendar.get(Calendar.HOUR) + min / 60;
        mSecDegree = sec / 60 * 360;
        mMinDegree = min  / 60 * 360;
        mHourDegree = hour  / 12 * 360;
    }

    private void drawTimeText(Canvas canvas) {

        //绘制外圈圆弧
        mCircleRectF.set(centerX - mPadding,
                centerY - mPadding,
                centerX + mPadding,
                centerY + mPadding);
        for (int i=0; i<4; i++){
            canvas.drawArc(mCircleRectF,5 + 90*i,80,false,mCirclePaint);
        }

        //绘制数字
        String[] timeText = {"12","3","6","9"};
        int[] textWidth = new int[4];
        int[] textHeight = new int[4];
        for (int i=0; i<4;i++){
            mTextPaint.getTextBounds(timeText[i],0,timeText[i].length(),mTextRect);
            textWidth[i] = mTextRect.width();
            textHeight[i] = mTextRect.height();
        }

        canvas.drawText(timeText[0],centerX - textWidth[0]/2,centerY - mPadding + textHeight[0]/2,mTextPaint);
        canvas.drawText(timeText[1],centerX + mPadding - textWidth[1]/2,centerY + textHeight[1]/2,mTextPaint);
        canvas.drawText(timeText[2],centerX - textWidth[2]/2,centerY + mPadding + textHeight[2]/2,mTextPaint);
        canvas.drawText(timeText[3],centerX - mPadding - textWidth[3]/2, centerY + textHeight[3]/2, mTextPaint);

    }

    private void drawScaleLine(Canvas canvas) {

        mScaleArcRectF.set(centerX - mPadding + 1.5f * mScaleLength,centerY - mPadding + 1.5f *mScaleLength
        ,centerX + mPadding - 1.5f *mScaleLength,centerY + mPadding - 1.5f *mScaleLength);

        mGradientMatrix = new Matrix();
        mGradientMatrix.setRotate(mSecDegree - 90,centerX,centerY);
        mSweepGradient = new SweepGradient(centerX,centerY,new int[]{mDarkColor,mLightColor},new float[]{0.75f,1});
        mSweepGradient.setLocalMatrix(mGradientMatrix);
        mScaleArcPaint.setShader(mSweepGradient);
        canvas.drawArc(mScaleArcRectF,0,360,false,mScaleArcPaint);

        canvas.save();
        for (int i=0; i<200; i++){
            canvas.drawLine(centerX,centerY -  mPadding + 1f*mScaleLength,
                    centerX, centerY - mPadding + 2f* mScaleLength,mScaleLinePaint);
            canvas.rotate(1.8f,centerX,centerY);
        }
        canvas.restore();
    }

    private void drawSecHand(Canvas canvas) {
        canvas.save();
        canvas.rotate(mSecDegree,centerX,centerY);

        if (mSecPath.isEmpty()){
            mSecPath.reset();
            float offset = centerY - mPadding + mTextRect.height()/2;
            mSecPath.moveTo(centerX,offset + 0.26f * mRadius);
            mSecPath.lineTo(centerX - 0.05f * mRadius,offset + 0.34f * mRadius);
            mSecPath.lineTo(centerX + 0.05f * mRadius,offset + 0.34f * mRadius);
            mSecPath.close();
            mSecPaint.setColor(mLightColor);
        }
        canvas.drawPath(mSecPath,mSecPaint);
        canvas.restore();
    }

    private void drawHourHand(Canvas canvas) {
        canvas.save();
        canvas.rotate(mHourDegree, getWidth() / 2, getHeight() / 2);
        if (mHourPath.isEmpty()) {
            mHourPath.reset();
            float offset = centerY - mPadding + mTextRect.height() / 2;
            mHourPath.moveTo(getWidth() / 2 - 0.018f * mRadius, getHeight() / 2 - 0.03f * mRadius);
            mHourPath.lineTo(getWidth() / 2 - 0.009f * mRadius, offset + 0.48f * mRadius);
            mHourPath.quadTo(getWidth() / 2, offset + 0.46f * mRadius,
                    getWidth() / 2 + 0.009f * mRadius, offset + 0.48f * mRadius);
            mHourPath.lineTo(getWidth() / 2 + 0.018f * mRadius, getHeight() / 2 - 0.03f * mRadius);
            mHourPath.close();
        }
        mHourPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mHourPath, mHourPaint);

        mCircleRectF.set(getWidth() / 2 - 0.03f * mRadius, getHeight() / 2 - 0.03f * mRadius,
                getWidth() / 2 + 0.03f * mRadius, getHeight() / 2 + 0.03f * mRadius);
        mHourPaint.setStyle(Paint.Style.STROKE);
        mHourPaint.setStrokeWidth(0.01f * mRadius);
        canvas.drawArc(mCircleRectF, 0, 360, false, mHourPaint);
        canvas.restore();
    }

    private void drawMinHand(Canvas canvas) {
        canvas.save();
        canvas.rotate(mMinDegree, getWidth() / 2, getHeight() / 2);
        if (mMinPath.isEmpty()) {
            mMinPath.reset();
            float offset = centerY - mPadding + mTextRect.height() / 2;
            mMinPath.moveTo(getWidth() / 2 - 0.01f * mRadius, getHeight() / 2 - 0.03f * mRadius);
            mMinPath.lineTo(getWidth() / 2 - 0.008f * mRadius, offset + 0.365f * mRadius);
            mMinPath.quadTo(getWidth() / 2, offset + 0.345f * mRadius,
                    getWidth() / 2 + 0.008f * mRadius, offset + 0.365f * mRadius);
            mMinPath.lineTo(getWidth() / 2 + 0.01f * mRadius, getHeight() / 2 - 0.03f * mRadius);
            mMinPath.close();
        }
        mMinPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mMinPath, mMinPaint);

        mCircleRectF.set(getWidth() / 2 - 0.03f * mRadius, getHeight() / 2 - 0.03f * mRadius,
                getWidth() / 2 + 0.03f * mRadius, getHeight() / 2 + 0.03f * mRadius);
        mMinPaint.setStyle(Paint.Style.STROKE);
        mMinPaint.setStrokeWidth(0.02f * mRadius);
        canvas.drawArc(mCircleRectF, 0, 360, false, mMinPaint);
        canvas.restore();
    }
}

