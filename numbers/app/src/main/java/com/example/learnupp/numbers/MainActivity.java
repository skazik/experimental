package com.example.learnupp.numbers;

import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
    int bg_select = 0;
    Integer bad, good;
    ImageView imgCard1,imgCard2,imgCard3;

    final int test_level_switch = 5;
    final int[][] cdres = new int[][]
    {
        {
            R.drawable.cred_joker, R.drawable.cred_joker, R.drawable.cblack_joker, R.drawable.cblack_joker
        },
        {
            R.drawable.cace_of_clubs, R.drawable.cace_of_diamonds, R.drawable.cace_of_hearts, R.drawable.cace_of_spades
        },
        {
            R.drawable.cjack_of_clubs2, R.drawable.cjack_of_diamonds2, R.drawable.cjack_of_hearts2, R.drawable.cjack_of_spades2
        },
        {
            R.drawable.cqueen_of_clubs2, R.drawable.cqueen_of_diamonds2, R.drawable.cqueen_of_hearts2, R.drawable.cqueen_of_spades2
        },
        {
            R.drawable.cking_of_clubs2, R.drawable.cking_of_diamonds2, R.drawable.cking_of_hearts2, R.drawable.cking_of_spades2
        },
        {
            R.drawable.c5_of_clubs, R.drawable.c5_of_diamonds, R.drawable.c5_of_hearts, R.drawable.c5_of_spades
        },
        {
            R.drawable.c6_of_clubs, R.drawable.c6_of_diamonds, R.drawable.c6_of_hearts, R.drawable.c6_of_spades
        },
        {
            R.drawable.c7_of_clubs, R.drawable.c7_of_diamonds, R.drawable.c7_of_hearts, R.drawable.c7_of_spades
        },
        {
            R.drawable.c8_of_clubs, R.drawable.c8_of_diamonds, R.drawable.c8_of_hearts, R.drawable.c8_of_spades
        },
        {
            R.drawable.c9_of_clubs, R.drawable.c9_of_diamonds, R.drawable.c9_of_hearts, R.drawable.c9_of_spades
        },

        //{R.drawable.c10_of_clubs,	R.drawable.c10_of_diamonds,	R.drawable.c10_of_hearts,	R.drawable.c10_of_spades},
        //{R.drawable.c2_of_clubs,	R.drawable.c2_of_diamonds,	R.drawable.c2_of_hearts,	R.drawable.c2_of_spades},
        //{R.drawable.c3_of_clubs,	R.drawable.c3_of_diamonds,	R.drawable.c3_of_hearts,	R.drawable.c3_of_spades},
        //{R.drawable.c4_of_clubs,	R.drawable.c4_of_diamonds,	R.drawable.c4_of_hearts,	R.drawable.c4_of_spades},
    };

    Animation animFadeIn,animFadeOut,animZoomIn,animZoomOut,animRotate,
            animFadeOutOnce, animFadeInOnce,
            animMove,animMove1,animMove2,
            animSlideUp,animSlideDown,animBounce,animSlideLeft,animSlideRight;
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
    public native int getNum1FromJNI();
    public native int getNum2FromJNI();
    public native int getNum3FromJNI();

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

        imgCard1 = (ImageView) findViewById(R.id.imageCard1);
        imgCard2 = (ImageView) findViewById(R.id.imageCard2);
        imgCard3 = (ImageView) findViewById(R.id.imageCard3);

        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);

        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);

        animFadeOutOnce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animFadeInOnce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);

        animSlideLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
        animSlideRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);

        animMove = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        animMove1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        animMove2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);

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
        animFadeOutOnce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                gvButter.setVisibility(View.INVISIBLE);
            }
        });

        animFadeInOnce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                gvButter.setVisibility(View.VISIBLE);
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
        animMove.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                imgCard2.startAnimation(animMove1);
                imgCard2.setVisibility(View.VISIBLE);
            }
        });
        animMove1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                imgCard1.startAnimation(animMove2);
                imgCard1.setVisibility(View.VISIBLE);
            }
        });
    }

    private void makeQuestAndAns() {
        tvQuest.setText(stringFromJNI());
        btAns1.setText(get1valFromJNI());
        btAns2.setText(get2valFromJNI());
        btAns3.setText(get3valFromJNI());
        btAns4.setText(get4valFromJNI());

        imgCard1.setImageResource(cdres[getNum1FromJNI()][mode.nextInt(4)]);
        imgCard2.setImageResource(cdres[getNum2FromJNI()][mode.nextInt(4)]);
        imgCard3.setImageResource(cdres[getNum3FromJNI()][mode.nextInt(4)]);

        imgCard1.setVisibility(View.INVISIBLE);
        imgCard2.setVisibility(View.INVISIBLE);
        imgCard3.startAnimation(animMove);
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
        if (++bg_select > 6) bg_select = 0;
        final int[] resin = new int[]{R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5, R.drawable.bg6, R.drawable.bg7};
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.appLayout);
        layout.setBackgroundResource(resin[bg_select]);
    }

    public void validateAns(String txt)
    {
        String res = resultFromJNI();
        if (res.compareTo(txt) == 0)
        {
            if (gvButter.getVisibility() == View.INVISIBLE)
                gvButter.startAnimation(animFadeInOnce);

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
        if (gvButter.getVisibility() == View.VISIBLE)
            gvButter.startAnimation(animFadeOutOnce);
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
