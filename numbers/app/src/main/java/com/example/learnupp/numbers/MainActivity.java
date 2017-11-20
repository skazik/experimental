package com.example.learnupp.numbers;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView tvQuest, tvBad, tvGood;
    GIFView gvButter;
    Button btAns1;
    Button btAns2;
    Button btAns3;
    Button btAns4;
    Random mode;
    int bg_select = 1;
    Integer bad, good;
    final int test_level_switch = 5;
    Animation animFadeIn,animFadeOut,animBlink,animZoomIn,animZoomOut,animRotate,
            animMove,animSlideUp,animSlideDown,animBounce,animSequential,animSlideLeft,animSlideRight;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String resultFromJNI();
    public native String get1valFromJNI();
    public native String get2valFromJNI();
    public native String get3valFromJNI();
    public native String get4valFromJNI();
    public native String levelUpFromJNI();
    public native String getLevelFromJNI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mode = new Random();
        bad = good = 0;
        gvButter = findViewById(R.id.ivButter);

        setLevelOnTitle(false);
        // Example of a call to a native method
        tvQuest = (TextView) findViewById(R.id.sample_text);
        tvBad = (TextView) findViewById(R.id.res_bad);
        tvGood = (TextView) findViewById(R.id.res_good);

        btAns1 = (Button) findViewById(R.id.Asn1);
        btAns2 = (Button) findViewById(R.id.Asn2);
        btAns3 = (Button) findViewById(R.id.Asn3);
        btAns4 = (Button) findViewById(R.id.Asn4);

        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);

        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);

        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);

        animSlideLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
        animSlideRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);

        makeQuestAndAns();

        animSlideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                makeQuestAndAns();
                tvQuest.startAnimation(animSlideDown);
            }
        });
        animZoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                makeQuestAndAns();
                tvQuest.startAnimation(animZoomOut);
            }
        });
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                makeQuestAndAns();
                tvQuest.startAnimation(animFadeIn);
            }
        });
        animSlideLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                makeQuestAndAns();
                tvQuest.startAnimation(animSlideRight);
            }
        });
    }

    private void makeQuestAndAns() {
        tvQuest.setText(stringFromJNI());
        btAns1.setText(get1valFromJNI());
        btAns2.setText(get2valFromJNI());
        btAns3.setText(get3valFromJNI());
        btAns4.setText(get4valFromJNI());
    }

    public void generateNewQuest()
    {
        int n_case = mode.nextInt(6);
        switch (n_case)
        {
            case 0:
                tvQuest.startAnimation(animSlideUp);
                // text done in end of animation
                return;
            case 1:
                tvQuest.startAnimation(animRotate);
                break;
            case 2:
                tvQuest.startAnimation(animBounce);
                break;
            case 3:
                tvQuest.startAnimation(animZoomIn);
                // text done in end of animation
                return;
            case 4:
                tvQuest.startAnimation(animFadeOut);
                // text done in end of animation
                return;
            case 5:
                tvQuest.startAnimation(animSlideLeft);
                // text done in end of animation
                return;
        }
        makeQuestAndAns();
    }

    public void changeBackground()
    {
        if (++bg_select > 5) bg_select=1;
        int resin;
        switch(bg_select)
        {
            case 1: resin = R.drawable.bg1; break;
            case 2: resin = R.drawable.bg2; break;
            case 3: resin = R.drawable.bg3; break;
            case 4: resin = R.drawable.bg4; break;
            case 5: resin = R.drawable.bg5; break;
            default:resin = R.drawable.bg1; break;
        }
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.appLayout);
        layout.setBackgroundResource(resin);
    }

    public void validateAns(String txt)
    {
        String res = resultFromJNI();
        if (res.compareTo(txt) == 0)
        {
            gvButter.setVisibility(View.VISIBLE);
            good++;
            tvGood.setText(good.toString());

            if (good >0 && (good % test_level_switch == 0)) {
                setLevelOnTitle(true);
                changeBackground();
            }
            return;
        }
        bad++;
        tvBad.setText(bad.toString());
        gvButter.setVisibility(View.INVISIBLE);
    }

    public void onClickAns1(View v) {
//        if (gvButter.getVisibility() != View.VISIBLE)
//            gvButter.setVisibility(gvButter.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        validateAns(btAns1.getText().toString());
        generateNewQuest();
    }
    public void onClickAns2(View v) {
        validateAns(btAns2.getText().toString());
        generateNewQuest();
    }
    public void onClickAns3(View v) {
        validateAns(btAns3.getText().toString());
        generateNewQuest();
    }
    public void onClickAns4(View v) {
        validateAns(btAns4.getText().toString());
        generateNewQuest();
    }

    public void setLevelOnTitle(boolean levelOnTitle) {
        String level = levelOnTitle ? levelUpFromJNI() : getLevelFromJNI();
        setTitle(getString(R.string.app_name) + "... Level: " + level);
    }
}
