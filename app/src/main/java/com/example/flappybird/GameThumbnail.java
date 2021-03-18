package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.DrawableRes;

import java.util.ArrayList;

public class GameThumbnail extends SurfaceView implements Runnable{

    private Thread mThread;

    private Bitmap mBitmapBg;
    private Bitmap mBitmapBird;
    private Bitmap mBitmapPipe;

    private int mSpeed;
    private boolean isRunning;

    private RectF mRectF;
    private Bird mBird;
//    private Pipe mPipe;

    public GameThumbnail(Context context) {
        super(context);
        init();
    }

    public GameThumbnail(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //监听surface创建成功，开启异步线程
                isRunning = true;
                mThread = new Thread(GameThumbnail.this);
                mThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isRunning = false;
            }
        });

        mSpeed = dpToPx(-2);
        initResource();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF=new RectF(0,0,w,h);
        mBird=new Bird(getContext(),w,h,mBitmapBird);
//        mPipe=new Pipe(getContext(),w,h,mBitmapPipe,mBitmapPipe);
//        mPipe.setX(w/2-mPipe.getPipeWidth()/2);
    }

    /**
     * 初始化bitmap资源
     */
    private void initResource() {
        mBitmapBg = decodeBitmapResource(R.drawable.bg1);
        mBitmapBird = decodeBitmapResource(R.drawable.b1);
        mBitmapPipe=decodeBitmapResource(R.drawable.g1);
    }

    @Override
    public void run() {
        while (isRunning){
            long start = System.currentTimeMillis();
            drawSelf();
            mBird.setY(mBird.getY()+mSpeed);
            mSpeed=-mSpeed;
            long end = System.currentTimeMillis();
            if (end - start < 50) {
                try {
                    Thread.sleep(50 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawSelf() {
        Canvas canvas=null;
        try {
            canvas=getHolder().lockCanvas();
            if (canvas!=null){
                drawBg(canvas);
                drawBird(canvas);
//                drawPipe(canvas);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (canvas!=null){
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawPipe(Canvas canvas) {
//        mPipe.draw(canvas);
    }

    private void drawBird(Canvas canvas) {
        mBird.draw(canvas);
    }

    private void drawBg(Canvas canvas) {
        canvas.drawBitmap(mBitmapBg,null,mRectF,null);
    }


    /**
     * 解析bitmap资源
     *
     * @param id resId
     * @return bitmap
     */
    private Bitmap decodeBitmapResource(@DrawableRes int id) {
        return BitmapFactory.decodeResource(getResources(), id);
    }

    /**
     * dp与px之间的转换
     *
     * @param val dp值
     * @return 转化的像素值
     */
    private int dpToPx(int val) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                val,
                getResources().getDisplayMetrics());
    }
}
