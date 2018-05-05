package com.example.niyati.democustomview.CirclePercent;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.niyati.democustomview.R;

public class CirclePerLayout extends RelativeLayout {
    CirclePerView mView;
    Button btnAnimate;
    public CirclePerLayout(Context context) {
        super(context);
    }

    public CirclePerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CirclePerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mView = findViewById(R.id.circle_view);
        btnAnimate = findViewById(R.id.btn_animate);
        btnAnimate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator animator = ObjectAnimator.ofFloat(mView,"progress",0,100);
                animator.setDuration(5000);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.start();
            }
        });
    }
}
