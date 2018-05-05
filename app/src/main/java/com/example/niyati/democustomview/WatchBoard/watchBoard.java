package com.example.niyati.democustomview.WatchBoard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.niyati.democustomview.R;
import com.example.niyati.democustomview.SizeUtil;

import java.util.Calendar;


public class watchBoard extends View {

    //钟表数字
    private String[] mTimes = {"XII","Ⅰ","Ⅱ","Ⅲ","Ⅳ","Ⅴ","Ⅵ","Ⅶ","Ⅷ","Ⅸ","Ⅹ","XI"};

    //外圆半径
    private float mRadius;
    //边距
    private float mPadding;
    //数字大小
    private float mTextSize;
    //时针宽度
    private float mHourPointWidth;
    //分针宽度
    private float mMinutePointWidth;
    //秒针宽度
    private float mSecondPointWidth;
    //指针圆角
    private int mPointRadius;
    //指针末尾长度
    private float mPointEndLength;

    //长线颜色
    private int mColorLong;
    //短线颜色
    private int mColorShort;
    //指针颜色
    private int mHourPointColor;
    //分针颜色
    private int mMinutePointColor;
    //秒针颜色
    private int mSecondPointColor;

    //画笔
    private Paint mPaint;

    public watchBoard(Context context) {
        super(context);
    }

    public watchBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //初始化属性
        initAttrs(attrs);
        //初始化画笔
        initPaint();
    }


    /**
     * 获取自定义的属性
     * @param attrs XML文件中的属性
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray array = null;
        array = getContext().obtainStyledAttributes(attrs, R.styleable.WatchBoard);
        mPadding = array.getDimension(R.styleable.WatchBoard_wb_padding,DptoPx(10));
        mTextSize = array.getDimension(R.styleable.WatchBoard_wb_text_size,SptoPx(16));
        mHourPointWidth = array.getDimension(R.styleable.WatchBoard_wb_hour_pointer_width,DptoPx(5));
        mMinutePointWidth = array.getDimension(R.styleable.WatchBoard_wb_minute_pointer_width,DptoPx(3));
        mSecondPointWidth = array.getDimension(R.styleable.WatchBoard_wb_second_pointer_wider,DptoPx(2));
        mPointRadius = (int) array.getDimension(R.styleable.WatchBoard_wb_point_corner_radius,DptoPx(10));
        mPointEndLength = array.getDimension(R.styleable.WatchBoard_wb_pointer_end_length,DptoPx(10));

        mColorLong = array.getColor(R.styleable.WatchBoard_wb_scale_long_color,Color.argb(225,0,0,0));
        mColorShort = array.getColor(R.styleable.WatchBoard_wb_scale_short_color,Color.argb(125,0,0,0));
        mHourPointColor = array.getColor(R.styleable.WatchBoard_wb_hour_pointer_color,Color.BLACK);
        mMinutePointColor = array.getColor(R.styleable.WatchBoard_wb_minute_pointer_color,Color.BLACK);
        mSecondPointColor = array.getColor(R.styleable.WatchBoard_wb_second_pointer_color,Color.RED);

        //回收
        array.recycle();
    }
    //初始化画笔
    private void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*
        int width = 1000;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED
                || heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED){

        }
        else {
            if (widthMode == MeasureSpec.EXACTLY){
                width = Math.min(widthSize,width);
            }
            if (heightMode == MeasureSpec.EXACTLY){
                width = Math.min(heightSize,width);
            }
        }
        setMeasuredDimension(width,width);
        */
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = (Math.min(w,h) - getPaddingLeft() -getPaddingRight())/2;
        //指针头是尾的6倍长
        mPointEndLength = mRadius /6;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //移动到画布中心
        canvas.translate(getWidth()/2, getHeight()/2);
        //绘制外圆
        paintCircle(canvas);
        //绘制刻度表盘
        paintScale(canvas);
        //绘制指针
        paintPointer(canvas);
        canvas.restore();
        //每秒刷新
        postInvalidateDelayed(1000);

    }

    //绘制外圆
    private void paintCircle(Canvas canvas){
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0,0,mRadius,mPaint);
    }
    //绘制刻度表盘
    private void paintScale(Canvas canvas) {
        mPaint.setStrokeWidth(DptoPx(1));
        int lineWidth;
        for (int i =0;i<60;i++){
            //整点时
            if (i%5==0){
                mPaint.setStrokeWidth(SizeUtil.Dp2Px(getContext(),1.5f));
                mPaint.setColor(mColorLong);
                lineWidth = 40;//整点刻度线的长度
                mPaint.setTextSize(mTextSize);//数字大小
                String text = mTimes[i/5];//获取整点对应数字
                Rect textBound = new Rect();
                mPaint.getTextBounds(text,0,text.length(),textBound);//获取包含数字的最小矩形
                mPaint.setColor(Color.BLACK);
                //绘制数字
                canvas.save();
                canvas.translate(0,-mRadius + DptoPx(10) + lineWidth + mPadding + (textBound.bottom - textBound.top));//移动到数字对应位置
                canvas.rotate(-6*i);//逆时针旋转相应角度，使数字竖直摆放
                canvas.drawText(text,-(textBound.right-textBound.left)/2,-(textBound.bottom + textBound.top)/2,mPaint);//绘制数字
                canvas.restore();
            }else { // 非整点时
                lineWidth = 30;
                mPaint.setColor(mColorShort);
                mPaint.setStrokeWidth(DptoPx(1));
            }
            canvas.drawLine(0,-mRadius + mPadding,0,-mRadius + DptoPx(10) + lineWidth + mPadding,mPaint);//绘制刻度线
            canvas.rotate(6);//顺时针旋转6度，绘制下一刻度线
        }
    }

    //绘制指针
    private void paintPointer(Canvas canvas){
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        //获取当前时间对应角度
        int angleSecond = second*360/60;
        int angleMinute = minute*360/60 + angleSecond/60;
        int angleHour = (hour%12)*360/12 + angleMinute/12;

        //绘制时针
        canvas.save();
        canvas.rotate(angleHour);//旋转时针的角度
        RectF rectHour = new RectF(-mHourPointWidth/2,-mRadius*3/5,mHourPointWidth/2,mPointEndLength);//时针形状
        mPaint.setColor(mHourPointColor);//指针颜色
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHourPointWidth);//边界宽度
        canvas.drawRoundRect(rectHour,mPointRadius,mPointRadius,mPaint);//绘制时针
        canvas.restore();

        //绘制分针
        canvas.save();
        canvas.rotate(angleMinute);
        RectF rectMinute = new RectF(-mMinutePointWidth/2,-mRadius * 3.5f / 5,mMinutePointWidth/2,mPointEndLength);
        mPaint.setColor(mMinutePointColor);
        mPaint.setStrokeWidth(mMinutePointWidth);
        canvas.drawRoundRect(rectMinute,mPointRadius,mPointRadius,mPaint);
        canvas.restore();

        //绘制秒针
        canvas.save();
        canvas.rotate(angleSecond);
        RectF rectSecond = new RectF(-mSecondPointWidth/2,-mRadius+15,mSecondPointWidth/2,mPointEndLength);
        mPaint.setColor(mSecondPointColor);
        mPaint.setStrokeWidth(mSecondPointWidth);
        canvas.drawRoundRect(rectSecond,mPointRadius,mPointRadius,mPaint);
        canvas.restore();

        //绘制中心圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mSecondPointColor);
        canvas.drawCircle(0,0,mSecondPointWidth*4,mPaint);
    }

    //Dp转Px
    private float DptoPx(int value){
        return SizeUtil.Dp2Px(getContext(),value);
    }
    //Sp转Px
    private float SptoPx(int value){
        return SizeUtil.Sp2Px(getContext(),value);
    }
}
