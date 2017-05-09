package cn.techscan.gd.diy_view.arcmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import cn.techscan.gd.diy_view.R;

/**
 * Created by cloverss on 2017/5/9.
 */

public class ArcMenu extends ViewGroup implements View.OnClickListener {
    public static final String TAG = "ArcMenu";

    public static final int POS_LEFT_TOP = 0;
    public static final int POS_LEFT_BOTTOM = 1;
    public static final int POS_RIGHT_TOP = 2;
    public static final int POS_RIGHT_BOTTOM = 3;

    private Position mPosition = Position.RIGHT_BOTTOM;
    private int radius;

    /**
     * 菜单默认状态
     */
    private Status mStatus = Status.CLOSE;

    @Override
    public void onClick(View v) {

    }

    public enum Status {
        OPEN, CLOSE
    }

    /**
     * 菜单的主按钮
     */
    View mCButton;

    /**
     * 点击菜单的回调接口
     */
    public interface OnMenuClickListener {
        void onClick(View view, int position);
    }

    private OnMenuClickListener mOnMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }


    /**
     * 菜单位置
     */
    public enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        //获取自定义属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);
        int pos = typedArray.getInt(R.styleable.ArcMenu_position, 3);
        Log.d(TAG, "ArcMenu: " + pos);
        switch (pos) {
            case POS_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case POS_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POS_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case POS_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        radius = (int) typedArray.getDimension(R.styleable.ArcMenu_radius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        Log.d(TAG, "ArcMenu: " + mPosition + "|" + radius);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutCButton();
        }
    }

    /**
     * 定位主菜单按钮
     */
    private void layoutCButton() {
        mCButton = getChildAt(0);
        mCButton.setOnClickListener(this);
        int left = 0;
        int top = 0;
        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();
        switch (mPosition) {
            case LEFT_TOP:
                left = 0;
                top = 0;
                break;
            case LEFT_BOTTOM:
                left = 0;
                top = mCButton.getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                left = mCButton.getMeasuredWidth() - width;
                top = 0;
                break;
            case RIGHT_BOTTOM:
                left = mCButton.getMeasuredWidth() - width;
                top = mCButton.getMeasuredHeight() - height;
                break;
            default:
                break;
        }
        mCButton.layout(left, top, left + width, top + width);
        Log.d(TAG, "layoutCButton: " + mPosition + "|" + left + "|" + top);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            // 测量子view
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }
}
