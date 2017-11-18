package com.example.learnupp.numbers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView tvNextButton;
    GIFView gvButter;
    int mode = 0;
    Animation animFadeIn,animFadeOut,animBlink,animZoomIn,animZoomOut,animRotate,
            animMove,animSlideUp,animSlideDown,animBounce,animSequential,animTogether,animCrossFadeIn,animCrossFadeOut;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gvButter = findViewById(R.id.ivButter);
        gvButter.setVisibility(View.INVISIBLE);
        // Example of a call to a native method
        tvNextButton = (TextView) findViewById(R.id.sample_text);
        tvNextButton.setText(stringFromJNI());

        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);

        animSlideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvNextButton.setText(stringFromJNI());
                tvNextButton.startAnimation(animSlideDown);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void onClickNextButton(View v) {
        gvButter.setVisibility(gvButter.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        if (mode == 0) {
            tvNextButton.startAnimation(animSlideUp);

        }
        else if (mode == 1) {
            tvNextButton.startAnimation(animRotate);
            tvNextButton.setText(stringFromJNI());
        }
        else {
            tvNextButton.setText(stringFromJNI());
            tvNextButton.startAnimation(animBounce);
        }
        if (++mode > 2) mode = 0;
    }

}
