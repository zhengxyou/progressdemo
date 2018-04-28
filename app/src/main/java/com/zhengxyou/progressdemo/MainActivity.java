package com.zhengxyou.progressdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private FractionalNumberProgressBarView progressBarView;
    private FractionalNumberProgressBarView progressBarView1;
    private FractionalNumberProgressBarView progressBarView2;
//    private ValueAnimator valueAnimator;

    private EditText et_progressValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_progressValue = findViewById(R.id.et_progressValue);
        progressBarView = findViewById(R.id.progressbar);
        progressBarView1 = findViewById(R.id.progressbar1);
        progressBarView2 = findViewById(R.id.progressbar2);
//
//        if (valueAnimator == null) {
//            valueAnimator = ValueAnimator.ofInt(1, 1000);
//            valueAnimator.setInterpolator(new DecelerateInterpolator());
//            valueAnimator.setDuration(2000);
//        }

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                valueAnimator.setIntValues(0, Integer.parseInt(et_progressValue.getText().toString()));
//                valueAnimator.start();
                ObjectAnimator.ofInt(progressBarView, "progress", 0, Integer.parseInt(et_progressValue.getText().toString())).setDuration(3000).start();
                ObjectAnimator.ofInt(progressBarView1, "progress", 0, Integer.parseInt(et_progressValue.getText().toString())).setDuration(3000).start();
                ObjectAnimator.ofInt(progressBarView2, "progress", 0, Integer.parseInt(et_progressValue.getText().toString())).setDuration(3000).start();
            }
        });
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int currentValue = (Integer) animation.getAnimatedValue();
//                float fraction = currentValue / 1000.0f;
//                progressBarView.setProgress((int) (fraction * 4000));
//
//            }
//
//        });
    }
}
