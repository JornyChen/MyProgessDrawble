package hello.test.cn.dialogtest;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

/**
 * Created by JornyChen
 * Email: zhaolong_chen@kingdee.com
 * Date: 15-5-25.
 */
public class ProgreeDrawable extends Drawable {
    private static final int STATE_NORMAL = 1;
    private static final int STATE_ROUTE = 2;
    private static final int STATE_SCALEICON = 3;
    private static final int STATE_TICK = 4;
    private int state = STATE_NORMAL;
    private ValueAnimator routeAnimaror, scaleAnimator, tickAnimator, colorAnimator;
    private AnimatorSet animatorSet, scaleAnimatorSet;
    Paint mFlogPaint;
    Paint mBgPant, mBgGreenPant;
    Rect rect = new Rect();
    int radius = 0;
    int step_iconId = 0;
    private Context context;
    private float progress = 0f;
    Bitmap step_icon;
    private int START_LINELENGTH = 10;
    private int END_LINELENGTH = 16;
    private int mFlogLength = 0;
    private int centX, centY;
    private int startX;
    private int startY;
    private int endX, endY;
    private int mFlogStart;
    private int width = 40;
    private int height = 40;

    public ProgreeDrawable(Context cxt) {
        this.context = cxt;
        init();
    }

    public int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void init() {
        width=dip2px(20);
        height=dip2px(20);
        START_LINELENGTH=dip2px(5);
        END_LINELENGTH=dip2px(8);
        mFlogPaint = new Paint();
        mFlogPaint.setAntiAlias(true);
        mFlogPaint.setStyle(Paint.Style.STROKE);
        mFlogPaint.setStrokeWidth(5);
        mFlogPaint.setColor(Color.WHITE);
        mBgPant = new Paint();
        mBgPant.setColor(context.getResources().getColor(R.color.icon_blue));
        mBgPant.setStrokeWidth(1);
        mBgPant.setStyle(Paint.Style.FILL);
        mBgPant.setAntiAlias(true);

        mBgGreenPant = new Paint();
        mBgGreenPant.setColor(context.getResources().getColor(R.color.icon_green));
        mBgGreenPant.setStrokeWidth(1);
        mBgGreenPant.setStyle(Paint.Style.FILL);
        mBgGreenPant.setAntiAlias(true);

        rect.left = 0;
        rect.top = 0;
        rect.right = getIntrinsicWidth();
        rect.bottom = getIntrinsicHeight();
        centX = width / 2;
        centY = height / 2;
        radius = height / 2 - 2;

        step_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.step_icon_rotate);

//
//        Bitmap tmpicon = Bitmap.createBitmap(step_icon, 0, 0, 30, 30);
//        step_icon.recycle();
//        step_icon = tmpicon;
        routeAnimaror = ValueAnimator.ofFloat(0f, 1f);
        routeAnimaror.setDuration(1000);
        routeAnimaror.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                state = STATE_ROUTE;
                progress = (Float) animation.getAnimatedValue();
                invalidateSelf();
            }
        });
        scaleAnimator = ValueAnimator.ofFloat(0f, 1f);
        scaleAnimator.setDuration(500);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                state = STATE_SCALEICON;
                progress = (Float) animation.getAnimatedValue();
                invalidateSelf();
            }
        });

        tickAnimator = ValueAnimator.ofFloat(1f, 0f);
        tickAnimator.setDuration(500);
        tickAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                state = STATE_TICK;
                progress = (Float) animation.getAnimatedValue();
                invalidateSelf();

            }
        });
        colorAnimator = ValueAnimator.ofInt(0, 255);
        colorAnimator.setDuration(500);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (Integer) animation.getAnimatedValue();
                mBgPant.setAlpha(255 - alpha);
                mBgGreenPant.setAlpha(alpha);
            }
        });
    }

    public void startAnimator() {

        routeAnimaror.setRepeatCount(1);
        routeAnimaror.setInterpolator(new LinearInterpolator());
        animatorSet = new AnimatorSet();
        scaleAnimatorSet = new AnimatorSet();
        scaleAnimatorSet.playTogether(scaleAnimator, colorAnimator);
        animatorSet.playSequentially(routeAnimaror, scaleAnimatorSet, tickAnimator);
        animatorSet.start();
    }

    /**
     * 画线
     *
     * @param canvas
     * @param pos    坐标点
     */
    private void drawLine2Line(Canvas canvas, int... pos) {
        if (pos.length < 4 || pos.length % 2 != 0) {
            return;
        }
        Path path = new Path();
        for (int i = 0; i < pos.length; i = i + 2) {
            if (i == 0) {
                path.moveTo(pos[i], pos[i + 1]);
            } else {
                path.lineTo(pos[i], pos[i + 1]);
            }

        }
        canvas.drawPath(path, mFlogPaint);
    }


    Rect rectBg = new Rect(0, 0, 40, 40);

    @Override
    public void draw(Canvas canvas) {
        Matrix matrix;
        //draw bg;

        switch (state) {
            case STATE_NORMAL:
                //预留默认图画
                break;
            case STATE_ROUTE:
                mBgPant.setAlpha(255);
                canvas.drawCircle(centX, centY, radius, mBgPant);
//                canvas.drawCircle(centX, centY, radius, mBgPant);
                matrix = new Matrix();
//                matrix.setTranslate(4, 1); //处理图片间距问题
                matrix.setTranslate(centX - step_icon.getWidth() / 2, centY - step_icon.getHeight() / 2);
                matrix.preRotate(progress * 720, step_icon.getWidth() / 2, step_icon.getHeight() / 2);  //要旋转的角度

                canvas.drawBitmap(step_icon, matrix, null);
                break;
            case STATE_SCALEICON:

                canvas.drawCircle(centX, centY, radius, mBgPant);
                canvas.drawCircle(centX, centY, radius, mBgGreenPant);
//                canvas.drawBitmap(step_bg_blue, null, rectBg, mBgPant);
//                canvas.drawBitmap(step_bg_green, null, rectBg, mBgGreenPant);

                matrix = new Matrix();
                matrix.setTranslate(centX - step_icon.getWidth() / 2, centY - step_icon.getHeight() / 2);     //设置图片的旋转中心，即绕（X,Y）这点进行中心旋转
                matrix.preScale((1 - progress), (1 - progress), step_icon.getWidth() / 2, step_icon.getHeight() / 2);
                canvas.drawBitmap(step_icon, matrix, null);
                break;
            case STATE_TICK:
                canvas.drawCircle(centX, centY, radius, mBgGreenPant);
                mFlogLength = (int) ((1f - progress) * END_LINELENGTH);
                mFlogStart = (int) ((1f - progress) * START_LINELENGTH);
                startX = (int) (radius - mFlogStart * Math.sin(45));
                startY = (int) (radius + dip2px(2)- mFlogStart * Math.cos(45));
                endX = (int) (radius + mFlogLength * Math.sin(45));
                endY = (int) (radius + dip2px(2)- mFlogLength * Math.cos(45));
                drawLine2Line(canvas, startX, startY, centX - dip2px(1) , centY + dip2px(2), endX, endY);
                break;

        }

    }

    public void startCheck() {
        routeAnimaror.setRepeatCount(-1);
        routeAnimaror.setInterpolator(new LinearInterpolator());
        routeAnimaror.start();
    }

    public void finish() {
        routeAnimaror.cancel();
        animatorSet = new AnimatorSet();
        scaleAnimatorSet = new AnimatorSet();
        scaleAnimatorSet.playTogether(scaleAnimator, colorAnimator);
        animatorSet.playSequentially(scaleAnimatorSet, tickAnimator);
        animatorSet.start();
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
