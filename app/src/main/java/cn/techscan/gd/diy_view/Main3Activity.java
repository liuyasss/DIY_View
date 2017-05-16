package cn.techscan.gd.diy_view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.techscan.gd.diy_view.ui.DiyView;

public class Main3Activity extends AppCompatActivity {

    Button mButton;
    DiyView mDiyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mButton = (Button) findViewById(R.id.btn);
        mDiyView = (DiyView) findViewById(R.id.diy);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiyView.start();
            }
        });
    }
}
