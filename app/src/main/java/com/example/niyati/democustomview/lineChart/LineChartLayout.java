package com.example.niyati.democustomview.lineChart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.niyati.democustomview.R;

import java.util.ArrayList;
import java.util.List;

public class LineChartLayout extends RelativeLayout {
    LineChartView mView;
    public LineChartLayout(Context context) {
        super(context);
    }

    public LineChartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mView = findViewById(R.id.linechartview);
        findViewById(R.id.btn_data).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> xList = new ArrayList<>();
                List<Integer> yList = new ArrayList<>();
                for (int i=0; i<10 ;i++){
                    xList.add(i+1);
                    int y = (int) (Math.random() * 100 + 1);
                    yList.add(y);
                }
                mView.setDataList(xList,yList);
            }
        });
    }
}
