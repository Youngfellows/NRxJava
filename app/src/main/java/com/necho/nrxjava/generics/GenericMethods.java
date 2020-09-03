package com.necho.nrxjava.generics;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 泛型方法
 * 将泛型参数列表置于返回值之前
 */
public class GenericMethods {

    private String TAG = this.getClass().getSimpleName();

    /**
     * 将泛型参数列表置于返回值之前
     *
     * @param t
     * @param <T>
     */
    public <T> void f(T t) {
        Log.d(TAG, t.getClass().getSimpleName());
    }

    /**
     * 泛型方法与可变参数列表能很好地共存
     *
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> makeList(T... args) {
        ArrayList<T> result = new ArrayList<T>();
        for (T item : args) {
            result.add(item);
        }
        return result;
    }

    /**
     * 静态方法上的泛型：静态方法无法访问类上定义的泛型。如果静态方法操作的引用数据类型不确定的时候，必须要将泛型定义在方法上。
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String function(T t) {
        return t.getClass().getSimpleName();
    }
}
