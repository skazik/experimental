package com.example.learnupp.numbers;

import android.graphics.Point;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView tvQuest, tvBad, tvGood, tvCardsRes, tvCount, tvAverage;
    GIFView gvButter;
    Button btAns1;
    Button btAns2;
    Button btAns3;
    Button btAns4;
    Random mode;
    boolean winningAnimateResChange = false;
    int bg_select = 0;
    Integer bad, good;
    ImageView imgCard1,imgCard2,imgCard3;
    CountDownTimer countDownTimer = null;
    long mStartTime, mElapsedTime;

    int [][] mCardPos = new int[3][2];
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

    Animation animBlink,animFadeIn,animFadeOut,animZoomIn,animZoomOut,animRotate,
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
    public native void setAddOnly();
    public native void unsetAddOnly();
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

        tvCardsRes = (TextView) findViewById(R.id.tvCardResult);
        tvCount = (TextView) findViewById(R.id.count);
        tvAverage = (TextView) findViewById(R.id.average);

        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);

        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);

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
                if (tvQuest != null) tvQuest.startAnimation(animSlideDown);
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
                if (tvQuest != null) tvQuest.startAnimation(animZoomOut);
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
                if (tvQuest != null) tvQuest.startAnimation(animFadeIn);
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
                if (tvQuest != null) tvQuest.startAnimation(animSlideRight);
            }
        });
        animMove.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                imgCard3.setVisibility(View.VISIBLE);
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
                tvCardsRes.setVisibility(View.VISIBLE);
            }
        });
    }

    private void makeQuestAndAns() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        String new_quest = stringFromJNI();
        if (tvQuest != null) tvQuest.setText(new_quest);

        btAns1.setText(get1valFromJNI());
        btAns2.setText(get2valFromJNI());
        btAns3.setText(get3valFromJNI());
        btAns4.setText(get4valFromJNI());

        int i1 = getNum1FromJNI();
        int i2 = getNum2FromJNI();
        int i3 = getNum3FromJNI();

        imgCard1.setImageResource(cdres[i1][mode.nextInt(4)]);
        imgCard2.setImageResource(cdres[i2][mode.nextInt(4)]);
        imgCard3.setImageResource(cdres[i3][mode.nextInt(4)]);
        tvCardsRes.setVisibility(View.INVISIBLE);
        tvCardsRes.setText(new Integer(i1+i2+i3).toString());

        imgCard1.setVisibility(View.INVISIBLE);
        imgCard2.setVisibility(View.INVISIBLE);
        imgCard3.startAnimation(animMove);

        countDownTimer = new CountDownTimer(11000, 1000) {

            public void onTick(long millisUntilFinished) {
                long disp = (long)(millisUntilFinished / 1000)-1;
                tvCount.setText("00:0" + disp);
            }

            public void onFinish() {
                tvCount.setText("--:--");
                hideWinningAnimation();
            }
        };
        countDownTimer.start();
        mStartTime = System.currentTimeMillis();
    }

    private void showWinningAnimation() {
        if (gvButter.getVisibility() == View.INVISIBLE)
            gvButter.startAnimation(animFadeInOnce);
    }

    private void hideWinningAnimation() {
        if (gvButter.getVisibility() == View.VISIBLE)
            gvButter.startAnimation(animFadeOutOnce);
    }

    public void generateNewQuest()
    {
        mElapsedTime = System.currentTimeMillis() - mStartTime;
        float ela = ((float)((int)mElapsedTime/100))/10;
        tvAverage.setText(Float.toString(ela) + "s");
        int n_case = mode.nextInt(6);
        switch (n_case)
        {
            case 0:
                if (tvQuest != null) {
                    tvQuest.startAnimation(animSlideUp);
                    return;
                }
                break;
            case 1:
                if (tvQuest != null) tvQuest.startAnimation(animRotate);
                break;
            case 2:
                if (tvQuest != null) tvQuest.startAnimation(animBounce);
                break;
            case 3:
                if (tvQuest != null) {
                    tvQuest.startAnimation(animZoomIn);
                    return;
                }
                break;
            case 4:
                if (tvQuest != null) {
                    tvQuest.startAnimation(animFadeOut);
                    return;
                }
                break;
            case 5:
                if (tvQuest != null) {
                    tvQuest.startAnimation(animSlideLeft);
                    return;
                }
                break;
        }
        makeQuestAndAns();
    }

    public void changeBackground()
    {
        if (tvQuest != null) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            int x = (int) tvQuest.getX();
            tvQuest.setVisibility(View.INVISIBLE);
            tvQuest.setX(width);

            mCardPos[0][0] = (int) imgCard1.getX();
            mCardPos[0][1] = (int) imgCard1.getY();
            mCardPos[1][0] = (int) imgCard2.getX();
            mCardPos[1][1] = (int) imgCard2.getY();
            mCardPos[2][0] = (int) imgCard3.getX();
            mCardPos[2][1] = (int) imgCard3.getY();

            imgCard1.setVisibility(View.INVISIBLE);
            imgCard2.setVisibility(View.INVISIBLE);
            imgCard3.setVisibility(View.INVISIBLE);

            imgCard1.setX(x);
            imgCard1.setY(tvQuest.getY());

            imgCard2.setX(imgCard1.getX() + imgCard1.getWidth() + 80);
            imgCard2.setY(imgCard1.getY());

            imgCard3.setX(imgCard2.getX() + imgCard2.getWidth() + 80);
            imgCard3.setY(imgCard2.getY());

            imgCard1.setScaleX((float) 1.3);
            imgCard1.setScaleY((float) 1.3);
            imgCard2.setScaleX((float) 1.3);
            imgCard2.setScaleY((float) 1.3);
            imgCard3.setScaleX((float) 1.3);
            imgCard3.setScaleY((float) 1.3);

            setAddOnly();
            tvCardsRes.setVisibility(View.INVISIBLE);
            tvQuest = null;
        }
        else
        {
            imgCard1 = (ImageView) findViewById(R.id.imageCard1);
            imgCard2 = (ImageView) findViewById(R.id.imageCard2);
            imgCard3 = (ImageView) findViewById(R.id.imageCard3);

            tvQuest = (TextView) findViewById(R.id.sample_text);
            tvQuest.setX(imgCard1.getX());

            imgCard1.setVisibility(View.INVISIBLE);
            imgCard2.setVisibility(View.INVISIBLE);
            imgCard3.setVisibility(View.INVISIBLE);

            imgCard1.setScaleX((float) 1.0);
            imgCard1.setScaleY((float) 1.0);
            imgCard2.setScaleX((float) 1.0);
            imgCard2.setScaleY((float) 1.0);
            imgCard3.setScaleX((float) 1.0);
            imgCard3.setScaleY((float) 1.0);

            imgCard1.setX(mCardPos[0][0]);
            imgCard1.setY(mCardPos[0][1]);
            imgCard2.setX(mCardPos[1][0]);
            imgCard2.setY(mCardPos[1][1]);
            imgCard3.setX(mCardPos[2][0]);
            imgCard3.setY(mCardPos[2][1]);

            unsetAddOnly();
            tvQuest.setVisibility(View.VISIBLE);
        }

        winningAnimateResChange = true;
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
            good++;
            tvGood.setText(good.toString());
            tvGood.startAnimation(animBlink);

            if (good >0 && (good % test_level_switch == 0)) {
                setLevelOnTitle(true);
                hideWinningAnimation();
                changeBackground();
            }
            else {
                if (winningAnimateResChange) {
                    gvButter.setNextGIFResource();
                    winningAnimateResChange = false;
                }
                showWinningAnimation();
            }
            generateNewQuest();
            return;
        }
        bad++;
        tvBad.setText(bad.toString());
        tvBad.startAnimation(animBlink);
        hideWinningAnimation();
        generateNewQuest();
    }

    public void onClickAns1(View v) {
        validateAns(btAns1.getText().toString());
    }
    public void onClickAns2(View v) {
        validateAns(btAns2.getText().toString());
    }
    public void onClickAns3(View v) {
        validateAns(btAns3.getText().toString());
    }
    public void onClickAns4(View v) {
        validateAns(btAns4.getText().toString());
    }

    public void setLevelOnTitle(boolean levelOnTitle) {
        String level = levelOnTitle ? levelUpFromJNI() : getLevelFromJNI();
        setTitle(getString(R.string.app_name) + "... Level: " + level);
    }
}
