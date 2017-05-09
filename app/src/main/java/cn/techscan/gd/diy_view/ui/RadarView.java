package cn.techscan.gd.diy_view.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cloverss on 2017/4/17.
 * 模仿max的雷达图
 */

public class RadarView extends View {


    private int count = 6;
    private float radius;
    private int centerX;
    private int centerY;
    private double angle = Math.PI * 2 / count;
    float fontHeight;

    String[] text = {"综合", "推进", "生存", "输出", "发育", "KDA"};
    int[] value = {60, 40, 70, 80, 50, 90};

    public void setValue(int[] value) {
        this.value = value;
    }

    public void setText(String[] text) {
        this.text = text;
    }

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

    }


    /**
     * 视图大小发生改变的时候调用
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 2 * 0.75f;
        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);
        drawPolygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawValueFill(canvas);
        drawValueText(canvas);

    }

    /**
     * 绘制填充 雷达图
     *
     * @param canvas
     */
    private void drawValueFill(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3f);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xa0878686);
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            float x = (float) (radius * value[i] / 100 * Math.cos(angle * i));
            float y = (float) (radius * value[i] / 100 * Math.sin(angle * i));
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setStrokeWidth(2f);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(26);
        textPaint.setColor(0xff687681);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        fontHeight = fontMetrics.descent - fontMetrics.ascent;   //文字高度
        for (int i = 0; i < count; i++) {
            float x = (float) (radius * Math.cos(angle * i));
            float y = (float) (radius * Math.sin(angle * i));
            float totalAngle = (float) (angle * i);
            if (totalAngle > 0 && totalAngle < (Math.PI / 2)) { // 第一象限
                canvas.drawText(text[i], x, y + fontHeight, textPaint);
            } else if (totalAngle > (Math.PI / 2) && totalAngle < (Math.PI)) {  // 第二象限
                float fontLength = textPaint.measureText(text[i]);
                canvas.drawText(text[i], x - fontLength, y + fontHeight, textPaint);
            } else if (totalAngle >= Math.PI && totalAngle <= (Math.PI * 3 / 2)) {// 第三象限
                float fontLength = textPaint.measureText(text[i]);
                canvas.drawText(text[i], x - fontLength, y, textPaint);
            } else {
                canvas.drawText(text[i], x, y, textPaint);
            }
        }
    }

    /**
     * 绘制数值
     * ps：该坐标系是手机屏幕坐标系，右下 为第一象限
     *
     * @param canvas
     */
    private void drawValueText(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setStrokeWidth(8f);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(30);
        textPaint.setColor(0xff6A7883);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        fontHeight = fontMetrics.descent - fontMetrics.ascent;   //文字高度
        for (int i = 0; i < count; i++) {
            float x = (float) (radius * Math.cos(angle * i));
            float y = (float) (radius * Math.sin(angle * i));
            float totalAngle = (float) (angle * i);
            if (totalAngle > 0 && totalAngle < (Math.PI / 2)) { // 第一象限
                float fontLength = textPaint.measureText(String.valueOf(value[i]));
                canvas.drawText(String.valueOf(value[i]), x + fontLength / 2, y + fontHeight * 2, textPaint);
            } else if (totalAngle > (Math.PI / 2) && totalAngle < (Math.PI)) {  // 第二象限
                float fontLength = textPaint.measureText(text[i]);
                canvas.drawText(String.valueOf(value[i]), x - fontLength, y + fontHeight * 2, textPaint);
            } else if (totalAngle >= Math.PI && totalAngle <= (Math.PI * 3 / 2)) {// 第三象限
                float fontLength = textPaint.measureText(text[i]);
                canvas.drawText(String.valueOf(value[i]), x - fontLength, y - fontHeight, textPaint);
            } else if (totalAngle >= Math.PI * 3 / 2 && totalAngle <= (Math.PI * 2)) {  //第四象限
                float fontLength = textPaint.measureText(text[i]);
                canvas.drawText(String.valueOf(value[i]), (float) (x + fontLength * 0.2), y - fontHeight, textPaint);
            } else {
                canvas.drawText(String.valueOf(value[i]), x, y + fontHeight, textPaint);
            }
        }
    }

    /**
     * 绘制网格直线
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);
        paint.setAntiAlias(true);
        paint.setColor(0xffe1e1e1);
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            path.reset();
            float x = (float) (radius * Math.cos(angle * i));
            float y = (float) (radius * Math.sin(angle * i));
            path.lineTo(x, y);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * 绘制正多边形
     *
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(4f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(0xffe1e1e1);
        Path path = new Path();
        for (int i = 1; i < count; i++) {
            path.reset();
            float r = radius / (count - 1);
            for (int j = 0; j < count; j++) {
                if (j == 0) {
                    path.moveTo(r * i, 0);
                } else {
                    float x = (float) (r * i * Math.cos(angle * j));
                    float y = (float) (r * i * Math.sin(angle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, paint);
        }
    }
}
