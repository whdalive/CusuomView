package com.example.niyati.democustomview.Rodar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class RadarView extends View {
    //圈数
    private int count = 6;

    private float angle = (float) (Math.PI * 2/count);
    //网格最大半径
    private float radius;
    //原点坐标
    private int centerX;
    private int centerY;

    private float maxValue = 100;
    private Paint mainPaint;
    private Paint valuePaint;
    private Paint textPaint;
    //文本集合
    private String[] titles = {"a","b","c","d","e","f"};
    private double[] data = {100,60,60,60,100,50,10,20};

    public RadarView(Context context) {
        super(context);
        initPaint(context);

    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    private void initPaint(Context context){
        mainPaint = new Paint();
        mainPaint.setColor(Color.GRAY);
        mainPaint.setStrokeWidth(2);
        mainPaint.setStyle(Paint.Style.STROKE);

        valuePaint = new Paint();
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        valuePaint.setStrokeWidth(2);
        valuePaint.setColor(Color.BLUE);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(2);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
        textPaint.setAntiAlias(true);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = w/2;
        centerY = h/2;

        radius = Math.min(h,w)/2*0.8f;
        postInvalidate();
        super.onSizeChanged(w,h,oldw,oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPolygon(canvas);

        drawGridLine(canvas);

        drawText(canvas);

        drawRegion(canvas);
    }

    private void drawPolygon(Canvas canvas){
        Path path = new Path();

        float distance = radius/(count-1);
        for (int i=0; i<count; i++){
            float curR = distance*i;
            path.reset();
            for (int j=0;j<count;j++){
                if (j==0){
                    path.moveTo(centerX+curR,centerY);
                }else {
                    float x = (float) (centerX+curR*Math.cos(angle*j));
                    float y = (float) (centerY+curR*Math.sin(angle*j));
                    path.lineTo(x,y);
                }
            }
            path.close();
            canvas.drawPath(path,mainPaint);
        }
    }
    private void drawGridLine(Canvas canvas) {
        Path path = new Path();
        for (int i=0; i<count; i++ ){
            path.reset();
            path.moveTo(centerX,centerY);
            float x = (float) (centerX + radius*Math.cos(angle*i));
            float y = (float) (centerY + radius*Math.sin(angle*i));
            path.lineTo(x,y);
            canvas.drawPath(path,mainPaint);
        }
    }
    private void drawText(Canvas canvas){
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i=0; i<count; i++){
            float x = (float) (centerX + (radius+fontHeight/2)*Math.cos(angle*i));
            float y = (float) (centerY + (radius+fontHeight/2)*Math.sin(angle*i));
            if (angle*i>=0&&angle*i<=Math.PI/2){//4象限
                canvas.drawText(titles[i],x,y,textPaint);
            }else if (angle*i>Math.PI/2&&angle*i<=Math.PI){//3象限
                float dis = textPaint.measureText(titles[i]);
                canvas.drawText(titles[i],x-dis,y,textPaint);
            }else if (angle*i>Math.PI&&angle*i<=Math.PI*3/2){//2象限
                float dis = textPaint.measureText(titles[i]);
                canvas.drawText(titles[i],x-dis,y,textPaint);
            }else if (angle*i>Math.PI*3/2&&angle*i<=Math.PI*2){//1象限
                canvas.drawText(titles[i],x,y,textPaint);
            }
        }
    }
    private void drawRegion(Canvas canvas){
        Path path = new Path();
        valuePaint.setAlpha(255);
        for (int i=0; i<count;i++){
            double percent = data[i]/maxValue;
            float x = (float) (centerX+radius*Math.cos(angle*i)*percent);
            float y = (float) (centerY+radius*Math.sin(angle*i)*percent);
            if (i==0){
                path.moveTo(x,centerY);
            }else {
                path.lineTo(x,y);
            }
            canvas.drawCircle(x,y,8,valuePaint);
        }
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path,valuePaint);
        valuePaint.setAlpha(127);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path,valuePaint);
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public void setMainPaintColor(int color){
        mainPaint.setColor(color);
    }
    public void setTextPaintColor(int color){
        textPaint.setColor(color);
    }
    public void setValuePaintColor(int color){
        valuePaint.setColor(color);
    }
}
