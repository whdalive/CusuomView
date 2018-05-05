package com.example.niyati.democustomview.CirclePerEnhanced;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.niyati.democustomview.R;

public class EnhancedCircleLayout extends RelativeLayout {
    EnhancedCircleView mView;
    Button mButton;
    private float mLastProgress;
    private float mCurProgress;
    public EnhancedCircleLayout(Context context) {
        super(context);
    }

    public EnhancedCircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnhancedCircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mView = findViewById(R.id.enhanced_circle_view);
        mButton = findViewById(R.id.btn_change);
        mLastProgress = 0;
        mCurProgress = 0;

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setEnabled(false);
                long duration;
                mCurProgress = (float) (Math.random() * 100 +1);
                duration = (long) (Math.abs(mCurProgress - mLastProgress)*50);
                Animator animator = ObjectAnimator.ofFloat(mView,"progress",mLastProgress,mCurProgress);
                animator.setDuration(duration);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.start();

                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mButton.setEnabled(true);
                    }
                });
                mLastProgress = mCurProgress;
            }
        });

    }
}
