package com.necho.nrxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * RxJava的基本使用
     *
     * @param view
     */
    public void onCommonUse(View view) {
        Intent intent = new Intent(this, CommonActivity.class);
        startActivity(intent);
    }

    /**
     * RxJava的变换
     *
     * @param view
     */
    public void onTransformation(View view) {
        Intent intent = new Intent(this, TransformationActivity.class);
        startActivity(intent);
    }
}
