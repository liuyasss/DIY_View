package cn.techscan.gd.diy_view.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by cloverss on 2017/5/16.
 */

public class DiyView extends View implements View.OnClickListener {

    public final String TAG = this.getClass().getSimpleName();

    int mWidth, mHeight;
    Paint mPaint;
    int radiusButton = 10;  //圆角矩形的圆角半径
    int l = -200;
    int t = -40;
    int r = 200;
    int b = 40;
    AnimatorSet animatorSet = new AnimatorSet();
    ValueAnimator animToOval, animToCircle;
    ObjectAnimator animTranslation;
    Canvas mCanvas;

    private OnClick mOnClick = null;

    public void setOnClick(OnClick onClick) {
        mOnClick = onClick;
    }

    public DiyView(Context context) {
        this(context, null);
    }

    public DiyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(3f);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = (int) (w * 0.5);
        mHeight = (int) (h * 0.5);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        canvas.translate(mWidth, mHeight);
        RectF rectF = new RectF(l, t, r, b);
        canvas.drawRoundRect(rectF, radiusButton, radiusButton, mPaint);
    }

    public void start() {
        toOval();
        toCircle();
        translation();
        animatorSet.playSequentially(animToOval, animToCircle, animTranslation);
        animatorSet.setDuration(1200);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd: ");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void toOval() {
        animToOval = ValueAnimator.ofInt(10, 40);
        animToOval.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radiusButton = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    private void toCircle() {
        animToCircle = ValueAnimator.ofInt(200, 40);
        animToCircle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                l = -(int) animation.getAnimatedValue();
                r = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    private void translation() {
        animTranslation = ObjectAnimator.ofFloat(this, "translationY", 0f, -120f);
    }

    private void drawOK() {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setStrokeWidth(8f);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCanvas.drawText("OK", 0, -120, textPaint);

    }

    @Override
    public void onClick(View v) {
        if (mOnClick != null) {
            mOnClick.startAnim();
        }
    }

    interface OnClick {
        void startAnim();
    }
}
