package com.example.niyati.democustomview.lineChart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.niyati.democustomview.SizeUtil;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {

    private Paint mTextPaint,mLinePaint,mPointPaint;
    private Path mPath;//坐标轴
    private float mPointRadius;
    private int xyMargin = 50;
    private int lineLength = 10;

    private List<ChartPoint> mPointList;
    private List<ChartPoint> mLastPointList;

    private int maxX = 10;
    private int maxY = 100;

    public LineChartView(Context context) {
        this(context,null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPointRadius = SizeUtil.Dp2Px(context,3);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(40);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLUE);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(SizeUtil.Dp2Px(context,3));

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.YELLOW);
        mPointPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPointList == null || mPointList.size() == 0){
            return;
        }
        //坐标轴
        mPath.reset();
        mPath.moveTo(xyMargin,0);
        mPath.lineTo(xyMargin,getHeight() - xyMargin);
        mPath.lineTo(getWidth(),getHeight() - xyMargin);
        canvas.drawPath(mPath,mLinePaint);

        for (int i=0; i<mPointList.size(); i++){
            //x刻度线
            int x = xyMargin + (i + 1)*(getWidth() - 2*xyMargin)/mPointList.size();
            canvas.drawLine(x,getHeight()-xyMargin-lineLength,x,getHeight()-xyMargin,mLinePaint);

            //y刻度线
            int y = getHeight() -xyMargin -(i+1) * (getHeight() - 2*xyMargin)/mPointList.size();
            canvas.drawLine(xyMargin,y,xyMargin + lineLength,y,mLinePaint);

            canvas.drawText(String.valueOf(mPointList.get(i).getxData()), x, getHeight() - mTextPaint.getTextSize() / 4, mTextPaint);
            canvas.drawText(String.valueOf((i + 1) * 10), 0, y + mTextPaint.getTextSize() / 2, mTextPaint);
        }

        for (int i=0;i<mPointList.size();i++){
            if (i != mPointList.size()-1){
                ChartPoint lastP = mPointList.get(i);
                ChartPoint nextP = mPointList.get(i +1);
                canvas.drawLine(lastP.getX(),lastP.getY(),nextP.getX(),nextP.getY(),mLinePaint);
            }
        }

        for (int i=0; i<mPointList.size(); i++){
            ChartPoint point = mPointList.get(i);
            canvas.drawCircle(point.getX(),point.getY(),mPointRadius,mPointPaint);
        }
    }

    public void setDataList(List<Integer> xList, List<Integer> yList){
        setPointData(xList,yList);
        setPointAnimator();
    }

    private void setPointData(List<Integer> xList, List<Integer> yList) {
        mPointList = new ArrayList<>();
        for (int i=0; i<xList.size(); i++){
            ChartPoint point = new ChartPoint();
            point.setxData(xList.get(i));
            point.setyData(yList.get(i));

            point.setX(xyMargin + xList.get(i)*(getWidth() -2*xyMargin)/maxX);
            point.setY(getHeight() - xyMargin - (getHeight()-2*xyMargin)*yList.get(i)/maxY);
            mPointList.add(point);

        }
    }
    private void setPointAnimator(){
        for (int i=0; i<mPointList.size(); i++){
            final ChartPoint point = mPointList.get(i);
            ValueAnimator animator;
            if (mLastPointList != null && mLastPointList.size()>0){
                animator = ValueAnimator.ofInt(mLastPointList.get(i).getY(),point.getY());
            }else {
                animator = ValueAnimator.ofInt(getHeight() - xyMargin,point.getY());
            }
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    point.setY(value);
                    invalidate();
                }
            });
            animator.start();
        }
        mLastPointList = mPointList;
    }


    class ChartPoint {
        private int xData;
        private int yData;
        private int X;
        private int Y;

        public int getxData() {
            return xData;
        }

        public void setxData(int xData) {
            this.xData = xData;
        }

        public int getyData() {
            return yData;
        }

        public void setyData(int yData) {
            this.yData = yData;
        }

        public int getX() {
            return X;
        }

        public void setX(int x) {
            X = x;
        }

        public int getY() {
            return Y;
        }

        public void setY(int y) {
            Y = y;
        }
    }
}
