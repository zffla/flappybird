package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.TypedValue;

import java.util.List;

public class Grade extends DrawablePart {

    private List<Bitmap> mBitmaps;
    private RectF rectF;
//    private int mFirstLeft;
    private int mTop;
    private int mLeft;

    private int mWidth;
    private int mHeight;

    public Grade(Context context, int gameW, int gameH, Bitmap bitmap) {
        super(context, gameW, gameH, bitmap);
        mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                30,
                context.getResources().getDisplayMetrics());
        mHeight = mWidth * bitmap.getHeight() / bitmap.getWidth();

        mLeft = gameW / 2 - mWidth / 2;
        mTop=gameH/4;

        rectF=new RectF();
    }


    @Override
    public void draw(Canvas canvas) {
        mLeft = mGameW / 2 - mWidth / 2;
        for (int i=0;i<mBitmaps.size();i++){
            mLeft=mLeft+i*mWidth;
            rectF.set(mLeft,mTop,mLeft+mWidth,mTop+mHeight);
            canvas.drawBitmap(mBitmaps.get(i),null,rectF,null);
        }
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        mBitmaps = bitmaps;
    }
}
