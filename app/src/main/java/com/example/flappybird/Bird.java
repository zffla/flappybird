package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.TypedValue;

public class Bird extends DrawablePart {
    private int x;
    private int y;

    private int mWidth;
    private int mHeight;

    private RectF mRectF;

    public Bird(Context context, int gameW, int gameH, Bitmap bitmap) {
        super(context, gameW, gameH, bitmap);
        y = gameH / 2;

        mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                30,
                context.getResources().getDisplayMetrics());
        mHeight = mWidth * bitmap.getHeight() / bitmap.getWidth();
        x = gameW / 2 - mWidth / 2;

        mRectF = new RectF();
    }

    @Override
    public void draw(Canvas canvas) {
        mRectF.set(x, y, x + mWidth, y + mHeight);
        canvas.drawBitmap(mBitmap, null, mRectF, null);
    }


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void reset() {
        y = mGameH / 2;
    }

    //判断是否在管道覆盖范围内
    public boolean isDead(int pipeUpY, int pipeDownY) {
        if ((y < pipeUpY) || (y > pipeDownY)) {
            return true;
        }

        return false;
    }

    public boolean isDead(int floorLocation){
        if (y>floorLocation){
            return true;
        }
        return false;
    }

    public int getX() {
        return x;
    }
}
