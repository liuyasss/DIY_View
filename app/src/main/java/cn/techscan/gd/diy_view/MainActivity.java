package cn.techscan.gd.diy_view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    Button mButton, btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
//                btnAnim(v);
//                valueAnim(v);
                rotationAnim(v);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void btnAnim(View view) {

        RectF rectF = new RectF(0, 0, 200, 300);

        Path path = new Path();

        path.addRect(rectF, Path.Direction.CW);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", "translationY", path);
        animator.setDuration(5000);
        animator.start();
    }

    private void valueAnim(final View view) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 200);
        animator.setTarget(view);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                view.setTranslationY(currentValue);
                view.setTranslationX(currentValue - 50);
                Log.d(TAG, "onAnimationUpdate: " + currentValue);

            }
        });
        animator.setRepeatCount(2);
        animator.start();
    }

    private void rotationAnim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationX", 0f, 360f);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });
        animator.start();

    }

}
