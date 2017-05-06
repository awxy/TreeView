package com.wxy.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class TwoActivity extends AppCompatActivity {
    int width = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        final ProgressBar prb = (ProgressBar) findViewById(R.id.pro);
        final TextView tv = (TextView) findViewById(R.id.tv);
        //server
        tv.setText("500万");
        double max = 30000000;
        double now = 5000000;
        double result = now/max;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        final String filesize = df.format(result);//返回的是String类型的
        prb.setProgress((int) (100*Double.valueOf(filesize)));
        final ViewTreeObserver vto = prb.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                prb.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                width = prb.getWidth();
                //获取当前进度值px
                int yellow = (int) (width*Double.valueOf(filesize));
                Log.e("re",yellow+"");
                //-  2/3
                int fin  = yellow  - 66;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv.getLayoutParams();
                layoutParams.setMargins(fin,0,0,0);
                tv.setLayoutParams(layoutParams);
            }
        });


    }
}
