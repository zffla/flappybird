package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.List;


public class FlappyBird extends SurfaceView implements Runnable {
    private Thread mThread;
    private boolean isRunning;

    private Bitmap mBitmapBg;
    private Bitmap mBitmapFloor;
    private Bitmap mBitmapBird;
    private Bitmap mBitmapUp;
    private Bitmap mBitmapDown;
    private Bitmap mBitmapOver;
    private List<Bitmap> mBitmapNumList;
    private int[] mBitmapRes = {
            R.drawable.n0,
            R.drawable.n1,
            R.drawable.n2,
            R.drawable.n3,
            R.drawable.n4,
            R.drawable.n5,
            R.drawable.n6,
            R.drawable.n7,
            R.drawable.n8,
            R.drawable.n9
    };

    private int mGrade;
    private boolean isPassPipe;

    //    private RectF mRectF;
//    private Background mBackground;
    private Floor mFloor;
    private Bird mBird;
    private Pipe mPipe;
    private Grade mGradeObj;

    private final int SPEED = 2;
    private final int BIRD_AUTO_DOWN = 2;
    private final int BIRD_TOUCH_UP = -10;

    private int mSpeed;
    private int mBirdAutoDown;
    private int mBirdTouchUp;

    private int mBirdTempDis;
    private RectF mRectF;
    private RectF mRectFOver;


    private int mPreCount;
    private GameOverListener mListener;



    public FlappyBird(Context context) {
        super(context);
        init();
    }

    public FlappyBird(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlappyBird(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public interface GameOverListener {
        void onGameOver();
    }

    public void setListener(GameOverListener listener) {
        mListener = listener;
    }

    /**
     * 初始化方法
     */
    private void init() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //监听surface创建成功，开启异步线程
                isRunning = true;
                mThread = new Thread(FlappyBird.this);
                mThread.start();
                mBirdTempDis = 0;
                reset();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isRunning = false;
            }
        });

        mSpeed = dpToPx(SPEED);
        mBirdAutoDown = dpToPx(BIRD_AUTO_DOWN);
        mBirdTouchUp = dpToPx(BIRD_TOUCH_UP);

        isPassPipe = false;
        mPreCount = 0;
        initResource();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(0, 0, w, h);
        mRectFOver = new RectF();

//        mBackground=new Background(getContext(),w,h,mBitmapBg);
        mFloor = new Floor(getContext(), w, h, mBitmapFloor);
        mBird = new Bird(getContext(), w, h, mBitmapBird);
        mPipe = new Pipe(getContext(), w, h, mBitmapUp, mBitmapDown);
        mGradeObj = new Grade(getContext(), w, h, mBitmapNumList.get(0));
    }

    /**
     * 初始化bitmap资源
     */
    private void initResource() {
        mBitmapBg = decodeBitmapResource(R.drawable.bg1);
        mBitmapFloor = decodeBitmapResource(R.drawable.floor_bg);
        mBitmapBird = decodeBitmapResource(R.drawable.b1);
        mBitmapUp = decodeBitmapResource(R.drawable.g2);
        mBitmapDown = decodeBitmapResource(R.drawable.g1);
        mBitmapOver = decodeBitmapResource(R.drawable.over);

        mBitmapNumList = new ArrayList<>();
        for (int resId : mBitmapRes) {
            mBitmapNumList.add(decodeBitmapResource(resId));
        }
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

    @Override
    public void run() {
        while (isRunning) {
            long start = System.currentTimeMillis();
            drawSelf();
            mPipe.setX(mPipe.getX() - mSpeed);
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

    private void reset() {
        mBird.reset();
        mPipe.reset();
    }

    /**
     * 绘制画面
     */
    private void drawSelf() {
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            if (canvas != null) {
                drawBg(canvas);
                logic(canvas);
                drawGrade(canvas);
                drawBird(canvas);
                drawPipe(canvas);
                drawFloor(canvas);


//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        ObjectAnimator.ofInt(mPipe,"x",getWidth(),0).setDuration(2000).start();
//                    }
//                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }

    }

    /**
     * 绘制管道
     *
     * @param canvas 画布
     */
    private void drawPipe(Canvas canvas) {
        mPipe.draw(canvas);
    }

    /**
     * 绘制逻辑
     * 1.floor移动，x变换
     * 2.bird下降，y变换
     * 3.判断bird是否撞击pipe
     *
     * @param canvas
     */
    private void logic(Canvas canvas) {
//        mBackground.setX(mBackground.getX()-mSpeed);
        mFloor.setX(mFloor.getX() - mSpeed);
        mBirdTempDis += mBirdAutoDown;
        mBird.setY(mBird.getY() + mBirdTempDis);
        if (mBird.getX() >= mPipe.getX() & mBird.getX() <= (mPipe.getX() + mPipe.getPipeWidth())) {
            if (!mBird.isDead(mPipe.getPipeUpHeight(), mPipe.getyDown())) {
                isPassPipe = true;
            } else {
                //游戏已结束，提示玩家
                showRemind(canvas);
            }
        }


        if (mPipe.getCount() == mPreCount) {
            isPassPipe = false;
        }
        if (isPassPipe & (mPipe.getCount() != mPreCount)) {
            mGrade++;
            mPreCount = mPipe.getCount();
        }
        if (mBird.isDead(mFloor.getY())) {
            showRemind(canvas);
        }


    }

    /**
     * 提示游戏已结束
     * 1.显示已死亡提示框
     * 2.点击重新开始，重置游戏；否则退出
     *
     * @param canvas
     */
    private void showRemind(Canvas canvas) {
        int top = getHeight() / 4;
        int left = getWidth() / 2 - mBitmapOver.getWidth() / 2;
        mRectFOver.set(left, top
                , left + mBitmapOver.getWidth(), top + mBitmapOver.getHeight());
        canvas.drawBitmap(mBitmapOver, null, mRectFOver, null);
        isRunning = false;

    }

    /**
     * 展示当前成绩
     *
     * @param canvas
     */
    private void drawGrade(Canvas canvas) {
        char[] grade = numToChar(mGrade);

        List<Bitmap> bitmaps = generateGradeBitmaps(grade);
        mGradeObj.setBitmaps(bitmaps);
        mGradeObj.draw(canvas);
    }

    private List<Bitmap> generateGradeBitmaps(char[] grade) {
        List<Bitmap> bitmaps = new ArrayList<>();
        for (char g : grade) {
            switch (g) {
                case '0':
                    bitmaps.add(mBitmapNumList.get(0));
                    break;
                case '1':
                    bitmaps.add(mBitmapNumList.get(1));
                    break;
                case '2':
                    bitmaps.add(mBitmapNumList.get(2));
                    break;
                case '3':
                    bitmaps.add(mBitmapNumList.get(3));
                    break;
                case '4':
                    bitmaps.add(mBitmapNumList.get(4));
                    break;
                case '5':
                    bitmaps.add(mBitmapNumList.get(5));
                    break;
                case '6':
                    bitmaps.add(mBitmapNumList.get(6));
                    break;
                case '7':
                    bitmaps.add(mBitmapNumList.get(7));
                    break;
                case '8':
                    bitmaps.add(mBitmapNumList.get(8));
                    break;
                case '9':
                    bitmaps.add(mBitmapNumList.get(9));
                    break;

            }
        }
        return bitmaps;
    }

    /**
     * 绘制floor
     *
     * @param canvas 画布
     */
    private void drawFloor(Canvas canvas) {
        mFloor.draw(canvas);
    }

    /**
     * 绘制鸟
     *
     * @param canvas 画布
     */
    private void drawBird(Canvas canvas) {
        mBird.draw(canvas);
    }

    /**
     * 绘制背景
     *
     * @param canvas 画布
     */
    private void drawBg(Canvas canvas) {
        canvas.drawBitmap(mBitmapBg, null, mRectF, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isRunning){
            mListener.onGameOver();
        }
            mBirdTempDis = mBirdTouchUp;
        return true;
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


    //数字转字符
    private char[] numToChar(int num) {
        String numStr = String.valueOf(num);
        return numStr.toCharArray();
    }
}
