package cn.techscan.gd.diy_view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Arrays;

public class Main2Act extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String str = "1|2||3";
        Log.d("TAG", "onCreate: " + Arrays.toString(str.split("\\|")));
    }
}
