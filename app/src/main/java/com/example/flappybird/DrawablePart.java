package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class DrawablePart {

    public Context mContext;
    public int mGameW;
    public int mGameH;
    public Bitmap mBitmap;

    public DrawablePart(Context context, int gameW, int gameH, Bitmap bitmap) {
        mContext = context;
        mGameW = gameW;
        mGameH = gameH;
        mBitmap = bitmap;
    }

    public abstract void draw(Canvas canvas);
}
