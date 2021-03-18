package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

public class Floor extends DrawablePart {
    private int x;
    private int y;

    private Paint mPaint;
    private BitmapShader mBitmapShader;

    public Floor(Context context, int gameW, int gameH, Bitmap bitmap) {
        super(context, gameW, gameH, bitmap);
        y = gameH * 3 / 4;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(x, y);
        mPaint.setShader(mBitmapShader);
        canvas.drawRect(x, 0, -x + mGameW, mGameH - y, mPaint);
        canvas.restore();
        mPaint.setShader(null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        if (-x > mGameW) {
            this.x = x % mGameW;
        }
    }

    public int getY() {
        return y;
    }
}
