package com.example.niyati.democustomview.FilpBoard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.niyati.democustomview.R;

public class FlipBoardLayout extends RelativeLayout {
    FlipBoardView mView;

    public FlipBoardLayout(Context context) {
        super(context);
    }

    public FlipBoardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipBoardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mView = findViewById(R.id.flip_view);

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mView,"degreeY",0,-45);
        animator1.setDuration(1000);
        animator1.setStartDelay(500);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mView,"degreeZ",0,270);
        animator2.setDuration(800);
        animator2.setStartDelay(500);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mView,"fixY",0,30);
        animator3.setDuration(500);
        animator3.setStartDelay(500);

        //貌似在布局中不能通过Handler实现延迟效果，所以用一个空白的animator来代替
        //只是为了最后有个延迟效果，强行添了个没什么意义的animator
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(mView,"fixY",30,30);
        animator4.setDuration(500);
        animator4.setStartDelay(500);

        final AnimatorSet animatorSet = new AnimatorSet();
        //循环播放
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                //将属性重置
                mView.reset();
                animatorSet.start();
            }
        });

        //顺序播放动画
        animatorSet.playSequentially(animator1,animator2,animator3,animator4);
        animatorSet.start();

    }
}
