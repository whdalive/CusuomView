package com.example.niyati.democustomview.Pie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PieView extends View {

    private int[] mColors =  {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    private float mStartAngle = 0;

    private ArrayList<PieData> mData;


    private int mWidth,mHeight;

    private Paint mPaint = new Paint();

    public PieView(Context context) {
        super(context);
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mData){
          return;
        }
        float currentStartAngle = mStartAngle;
        canvas.translate(mWidth/2, mHeight/2);
        float r = (float) (Math.min(mWidth,mHeight)/2 * 0.8);
        RectF rectF = new RectF(-r,-r,r,r);
        for (int i = 0; i < mData.size(); i++){
            PieData pieData = mData.get(i);
            mPaint.setColor(pieData.getColor());
            canvas.drawArc(rectF,currentStartAngle,pieData.getAngle(),true,mPaint);
            currentStartAngle += pieData.getAngle();
        }
    }

    public void setStartAngle(int mStartAngle){
        this.mStartAngle = mStartAngle;
        invalidate();
    }

    public void setData(ArrayList<PieData> mData){
        this.mData = mData;
        initData(mData);
        invalidate();
    }

    private void initData(ArrayList<PieData> mData){
        if (null == mData || mData.size() == 0){
            return;
        }
        float sumValue = 0;
        for (int i = 0; i < mData.size(); i++){
            PieData pieData = mData.get(i);

            sumValue += pieData.getValue();
            int j = i % mColors.length;
            pieData.setColor(mColors[j]);
        }

        for (int i =0;i<mData.size();i++){
            PieData pieData = mData.get(i);
            float per = pieData.getValue() / sumValue;
            float angle = per * 360;
            pieData.setPer(per);
            pieData.setAngle(angle);
        }
    }
    private void init() {
        ArrayList<PieData> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PieData pie = new PieData("id is" + i, i);
            list.add(pie);
        }
        setData(list);
    }
}
