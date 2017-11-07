package cn.techscan.gd.diy_view.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cloverss on 2017/11/29.
 * 带有loading效果的Button
 */

public class LoadingButton extends View {


    private Paint bgPaint, loadingPaint, resultPaint;

    int textWidth, textHeight;
    String buttonText = "";

    public LoadingButton(Context context) {
        this(context, null);
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#19CC95"));
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(5);
        bgPaint.setAntiAlias(true);

        loadingPaint = new Paint();
        loadingPaint.setColor(Color.parseColor("#19CC95"));
        loadingPaint.setStyle(Paint.Style.STROKE);
        loadingPaint.setStrokeWidth(9);
        loadingPaint.setAntiAlias(true);

        resultPaint = new Paint();
        resultPaint.setColor(Color.WHITE);
        resultPaint.setStyle(Paint.Style.STROKE);
        resultPaint.setStrokeWidth(9);
        resultPaint.setStrokeCap(Paint.Cap.ROUND);  //设置线帽
        resultPaint.setAntiAlias(true);

//        textPaint = new Paint();
//        textPaint.setColor(buttonColor);
//        textPaint.setStrokeWidth(textSize / 6);
//        textPaint.setTextSize(textSize);
//        textPaint.setAntiAlias(true);

//        textHeight = getTextHeight()

    }

    /**
     * 获取文本的宽度
     *
     * @param paint
     * @param string
     * @return
     */
    private int getTextWidth(Paint paint, String string) {
        int result = 0;
        if (string != null && string.length() > 0) {
            int len = string.length();
            float[] widths = new float[len];
            paint.getTextWidths(string, widths);
            for (int i = 0; i < len; i++) {
                result += Math.ceil(widths[i]);
            }
        }
        return result;
    }

    /**
     * 获取字体的高度
     *
     * @param paint
     * @param string
     * @return
     */
    private int getTextHeight(Paint paint, String string) {
        Rect rect = new Rect();
        paint.getTextBounds(string, 0, string.length(), rect);
        return rect.height();
    }

}
