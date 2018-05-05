package com.example.niyati.democustomview.FilpBoard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.niyati.democustomview.R;

public class FlipBoardView extends View {
    //“右侧” 沿Y轴翻折的角度
    private float degreeY;

    //canvas 旋转的角度
    private float degreeZ;

    //“左侧” 最后翻折的角度
    private float fixY;

    private Paint mPaint;
    private Bitmap mBitmap;
    private Camera mCamera;

    public FlipBoardView(Context context) {
        this(context,null);
    }

    public FlipBoardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlipBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlipBoardView);
        BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(R.styleable.FlipBoardView_flip_background);
        a.recycle();
        if (drawable!=null){
            mBitmap = drawable.getBitmap();
        }else {
            mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.flip_board);
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCamera = new Camera();

        //沿Z轴平移，防止相机旋转后图片“糊”到脸上
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float newZ = - displayMetrics.density * 6;
        mCamera.setLocation(0,0,newZ);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        //绘制 翻折&旋转 部分
        canvas.save();
        mCamera.save();
        canvas.translate(centerX,centerY);//画布移动到屏幕中心
        //“折线及翻折部分的画布” 的旋转
        canvas.rotate(-degreeZ);//画布旋转
        mCamera.rotateY(degreeY);//相机绕Y周旋转
        mCamera.applyToCanvas(canvas);//相机应用到画布
        canvas.clipRect(0,-centerY,centerX,centerY);//画布裁切，只绘制翻折起来的部分
        //因为翻折部分相对于画布而言，坐标始终未改变

        canvas.rotate(degreeZ);//将画布旋转为原角度，否则绘制时绘制的图片也会旋转
        mCamera.restore();
        canvas.drawBitmap(mBitmap,-bitmapWidth/2,-bitmapHeight/2,mPaint);//绘制图片，注意坐标
        canvas.restore();

        //绘制 剩余 部分
        canvas.save();
        mCamera.save();
        canvas.translate(centerX,centerY);//移动到画布中心
        //“折线以及最后才翻折的画布”的旋转
        canvas.rotate(-degreeZ);//画布旋转
        canvas.clipRect(-centerX,-centerY,0,centerY);//裁切绘制范围
        mCamera.rotateY(fixY);//将旋转之后才需要翻折的部分 进行旋转
        mCamera.applyToCanvas(canvas);
        canvas.rotate(degreeZ);//画布还原回原本角度
        mCamera.restore();
        canvas.drawBitmap(mBitmap,-bitmapWidth/2,-bitmapHeight/2,mPaint);//绘制图片，注意坐标
        canvas.restore();


    }

    public void reset(){
        degreeY = 0;
        degreeZ = 0;
        fixY = 0;
    }
    public void setDegreeY(float degreeY) {
        this.degreeY = degreeY;
        invalidate();
    }

    public void setDegreeZ(float degreeZ) {
        this.degreeZ = degreeZ;
        invalidate();
    }

    public void setFixY(float fixY) {
        this.fixY = fixY;
        invalidate();
    }

}
