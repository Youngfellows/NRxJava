package com.necho.nrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.necho.nrxjava.bean.Course;
import com.necho.nrxjava.bean.Person;
import com.necho.nrxjava.bean.SimpleGen;
import com.necho.nrxjava.bean.Student;
import com.necho.nrxjava.generics.CoffeeGenerator;
import com.necho.nrxjava.generics.GenericMethods;
import com.necho.nrxjava.generics.ListOfGenerics;
import com.necho.nrxjava.generics.ThreeTuple;
import com.necho.nrxjava.generics.TupleList;

import java.io.Serializable;
import java.util.List;

public class GenericsActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generics);
    }


    /**
     * 类型转化
     *
     * @param view
     */
    public void onClick1(View view) {
        SimpleGen simpleGen1 = new SimpleGen(new Integer(99));
        simpleGen1.showType();
        int age = (Integer) simpleGen1.getOb();
        Log.d(TAG, "onClick1: " + age);

        SimpleGen simpleGen2 = new SimpleGen("世界这么大");
        simpleGen2.showType();
        String name = (String) simpleGen2.getOb();
        Log.d(TAG, "onClick1: " + name);
    }

    /**
     * 泛型接口、泛型类
     */
    public void onClick2(View view) {
        try {
            CoffeeGenerator coffeeGenerator = new CoffeeGenerator();
            for (int i = 0; i < 4; i++) {
                Log.d(TAG, coffeeGenerator.next().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 泛型方法
     */
    public void onClick3(View view) {
        GenericMethods gm = new GenericMethods();
        gm.f(99);
        gm.f("西门吹雪");
        gm.f(new Integer(98));
        gm.f(3.145);
        gm.f('a');
        gm.f(gm);

        Log.d(TAG, "==========================");
        List<Character> alist = gm.makeList('A');
        Log.d(TAG, "alist: " + alist);
        List<? extends Serializable> blist = gm.makeList("A", "B", 'C');
        Log.d(TAG, "blist: " + blist);
        List<String> clist = gm.makeList("saflsafdlosalfdkhweoqofelafsdsadf".split(""));
        Log.d(TAG, "clist: " + clist);


        Log.d(TAG, "==========================");
        String cs1 = GenericMethods.function(3.14);
        String cs2 = GenericMethods.function(8);
        String cs3 = GenericMethods.function('a');
        String cs4 = GenericMethods.function("西门吹雪");
        String cs5 = GenericMethods.function(new Person("小明", 22));
        Log.d(TAG, "cs1: " + cs1);
        Log.d(TAG, "cs2: " + cs2);
        Log.d(TAG, "cs3: " + cs3);
        Log.d(TAG, "cs4: " + cs4);
        Log.d(TAG, "cs5: " + cs5);

    }

    /**
     * 建复杂模型如list元组
     *
     * @param view
     */
    public void onClick4(View view) {
        TupleList<Integer, String, Character> ts = new TupleList<Integer, String, Character>();
        ThreeTuple<Integer, String, Character> tt1 = new ThreeTuple<Integer, String, Character>(99, "掌上洪城", 'a');
        ThreeTuple<Integer, String, Character> tt2 = new ThreeTuple<Integer, String, Character>(23, "西门吹雪", 'b');
        ThreeTuple<Integer, String, Character> tt3 = new ThreeTuple<Integer, String, Character>(18, "洪熙官", 'c');
        ts.add(tt1);
        ts.add(tt2);
        ts.add(tt3);
        int size = ts.size();
        Log.d(TAG, "size: " + size);
        for (int i = 0; i < size; i++) {
            ThreeTuple<Integer, String, Character> tuple = ts.get(i);
            Log.d(TAG, i + "," + tuple.toString());
        }

        Log.d(TAG, "=====================");
        ListOfGenerics<String> heros = new ListOfGenerics<>();
        heros.add("小李");
        heros.add("小花");
        heros.add("小翠");
        heros.add("小燕");
        heros.add("小红");
        int size1 = heros.size();
        for (int i = 0; i < size1; i++) {
            Log.d(TAG, i + "," + heros.get(i));
        }
    }
}
