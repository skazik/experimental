package com.example.learnupp.numbers;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

public class GIFView extends View {
    public Movie mMovie;
    public long movieStart;
    final int res_idx_max = 4;
    int resIdIdx = 0;
    final int[] resIdlist = new int[] {
            R.drawable.butterfly, R.drawable.banny, R.drawable.dog, R.drawable.sonik
    };
    final int[][] resIdoffset = new int[][] {{20,270}, {200,300}, {100,0}, {0,10}};

    public GIFView(Context context) {
        super(context);
        this.gifId = resIdlist[resIdIdx];
        initializeView();
    }

    public GIFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.gifId = resIdlist[resIdIdx];
        initializeView();
    }

    public GIFView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.gifId = resIdlist[resIdIdx];
        initializeView();
    }

    private void initializeView() {
        //R.drawable.loader - our animated GIF
        @SuppressLint("ResourceType") InputStream is = getContext().getResources().openRawResource(this.gifId);
        mMovie = Movie.decodeStream(is);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = now;
        }
        if (mMovie != null) {
            int relTime = (int) ((now - movieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            int x = getWidth() - mMovie.width() - resIdoffset[resIdIdx][0];
            int y = getHeight() - mMovie.height() - resIdoffset[resIdIdx][1];
            mMovie.draw(canvas, x, y);//
            this.invalidate();
        }
    }

    private int gifId;

    public void setGIFResource(int resId) {
        this.gifId = resId;
        initializeView();
    }

    public int getGIFResource() {
        return this.gifId;
    }

    public void setNextGIFResource() {
        resIdIdx++;
        if (resIdIdx >= res_idx_max) {
            resIdIdx = 0;
        }
        setGIFResource(resIdlist[resIdIdx]);
    }
}