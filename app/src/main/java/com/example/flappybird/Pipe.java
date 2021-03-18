package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

public class Pipe extends DrawablePart {
    private int mOriginBitmapHeight;
    private int mPipeUpHeight;
    private int mPipeDownHeight;
    private int mPipeHeightAll;
    private int mPipeWidth;
    private int mPipAreaHeight;
    private int x;
    private int yUp;
    private int yDown;
    private int count;

    private Bitmap mBitmapUp;
    private Bitmap mBitmapDown;
//    是否是上面的管子
//    private boolean isUp;

    private RectF mRectUp;
    private RectF mRectDown;


    public Pipe(Context context, int gameW, int gameH, Bitmap bitmap) {
        super(context, gameW, gameH, bitmap);
    }

    public Pipe(Context context, int gameW, int gameH, Bitmap bitmapUp, Bitmap bitmapDown) {
        this(context, gameW, gameH, bitmapUp);
//        this.isUp=isUp;
        mPipeWidth = bitmapUp.getWidth();
        mPipAreaHeight = gameH * 3 / 4;
        mPipeUpHeight = bitmapUp.getHeight() * 4 / 3;
        mPipeDownHeight = bitmapDown.getHeight() * 4 / 3;
        mOriginBitmapHeight=mPipeUpHeight;
        mPipeHeightAll = mPipeUpHeight + mPipeDownHeight;

        mRectUp = new RectF();
        mRectDown = new RectF();

        mBitmapUp = bitmapUp;
        mBitmapDown = bitmapDown;

        count = 1;
        generateDiffHeight();
        initLocation();
    }

    private void initLocation() {
        x = mGameW - mPipeWidth;
        yUp = 0;
        yDown = mPipAreaHeight - mPipeDownHeight;
    }

    @Override
    public void draw(Canvas canvas) {
        if (x < -mPipeWidth) {
            x = mGameW - mPipeWidth;
            count++;
            generateDiffHeight();
            yDown = mPipAreaHeight - mPipeDownHeight;

        }
        mRectUp.set(x, 0, x + mPipeWidth, mPipeUpHeight);
        canvas.drawBitmap(mBitmapUp, null, mRectUp, null);
        mRectDown.set(x, yDown, x + mPipeWidth, yDown + mPipeDownHeight);
        canvas.drawBitmap(mBitmapDown, null, mRectDown, null);
    }

    //随机生成上下管道高度
    private void generateDiffHeight() {
        if (count%3==0){
            int num=count%3;
            mOriginBitmapHeight += (10 * num);
            count++;
        }
        boolean match = new Random().nextBoolean();
        if (match) {
            float random = new Random().nextFloat();
            if (random < 0.5) {
                random = 0.5f;
            }
            mPipeUpHeight = (int) (random * mOriginBitmapHeight);
            mPipeDownHeight = mPipeHeightAll - mPipeUpHeight;
        } else {
            float random = new Random().nextFloat();
            if (random < 0.5) {
                random = 0.5f;
            }
            mPipeDownHeight = (int) (random * mOriginBitmapHeight);
            mPipeUpHeight = mPipeHeightAll - mPipeDownHeight;
        }

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void reset() {
        x = mGameW - mPipeWidth;
        mOriginBitmapHeight=mBitmap.getHeight();
    }


    public int getPipeWidth() {
        return mPipeWidth;
    }

    public int getyUp() {
        return yUp;
    }

    public int getyDown() {
        return yDown;
    }

    public int getPipeUpHeight() {
        return mPipeUpHeight;
    }

    public int getCount() {
        return count;
    }
}
