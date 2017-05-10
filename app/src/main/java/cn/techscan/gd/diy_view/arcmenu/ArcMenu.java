package cn.techscan.gd.diy_view.arcmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

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
            int count = getChildCount();        //包含主菜单
            for (int i = 0; i < count - 1; i++) {
                View childView = getChildAt(i + 1);
//                childView.setVisibility(GONE);
                int x = (int) (radius * Math.cos(Math.PI / 2 / (count - 2) * i));
                int y = (int) (radius * Math.sin(Math.PI / 2 / (count - 2) * i));
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();
                // 按钮位于右上 和 右下
                if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                    x = getMeasuredWidth() - childWidth - x;
                }
                // 按钮位于左下 和 右下
                if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {
                    y = getMeasuredHeight() - childHeight - y;
                }
                childView.layout(x, y, x + childWidth, y + childHeight);
            }
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
                top = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                left = getMeasuredWidth() - width;
                top = 0;
                break;
            case RIGHT_BOTTOM:
                left = getMeasuredWidth() - width;
                top = getMeasuredHeight() - height;
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

    @Override
    public void onClick(View v) {
        if (mCButton == null) {
            mCButton = getChildAt(0);
        }
        rotateCButton(v, 0f, 360f, 300);
        toggleMenu(300);
    }

    public void toggleMenu(int duration) {
        //为item添加动画
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            final View childView = getChildAt(i + 1);
            childView.setVisibility(View.VISIBLE);
            int x = (int) (radius * Math.cos(Math.PI / 2 / (count - 2) * i));
            int y = (int) (radius * Math.sin(Math.PI / 2 / (count - 2) * i));
            int xFlag = 1;
            int yFlag = 1;
            if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM) {
                xFlag = -1;
            }
            if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
                yFlag = -1;
            }

            AnimationSet animationSet = new AnimationSet(true);

            Animation tranAnim;

            if (mStatus == Status.CLOSE) {
                tranAnim = new TranslateAnimation(xFlag * x, 0, yFlag * y, 0);
                childView.setClickable(true);
                childView.setFocusable(true);
            } else {
                tranAnim = new TranslateAnimation(0, xFlag * x, 0, yFlag * y);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            tranAnim.setFillAfter(true);
            tranAnim.setDuration(duration);
            tranAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mStatus == Status.CLOSE) {
                        childView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            //旋转动画
            RotateAnimation rotateAnim = new RotateAnimation(0, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setDuration(duration);
            rotateAnim.setFillAfter(true);
            animationSet.addAnimation(rotateAnim);
            animationSet.addAnimation(tranAnim);
            childView.startAnimation(animationSet);

        }
        // 切换菜单状态
        changeStatus();
    }

    /**
     * 切换菜单状态
     */
    private void changeStatus() {
        mStatus = (mStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);
    }

    private void rotateCButton(View v, float start, float end, int duration) {
        RotateAnimation ra = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(duration);
        ra.setFillAfter(true);
        v.startAnimation(ra);
    }


}
