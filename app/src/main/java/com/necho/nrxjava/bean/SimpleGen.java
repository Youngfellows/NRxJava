package com.necho.nrxjava.bean;


import android.util.Log;

public class SimpleGen {

    private String TAG = this.getClass().getSimpleName();

    private Object ob;

    public SimpleGen(Object ob) {
        this.ob = ob;
    }

    public Object getOb() {
        return ob;
    }

    public void setOb(Object ob) {
        this.ob = ob;
    }

    public void showType() {
        Log.d(TAG, "showType: " + ob.getClass().getSimpleName());
    }
}
