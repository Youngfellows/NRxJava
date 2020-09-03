package com.necho.nrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AnnotationActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
    }

    /**
     * 转账注解
     *
     * @param view
     */
    public void onAnnotation(View view) {
        BankService bankService = new BankService();
        String transferResult = bankService.transferMoney(1500);
        Log.d(TAG, "onAnnotation: " + transferResult);
        transferResult = bankService.transferMoney(7800);
        Log.d(TAG, "onAnnotation: " + transferResult);
    }
}
