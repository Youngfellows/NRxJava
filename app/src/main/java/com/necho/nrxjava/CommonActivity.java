package com.necho.nrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.necho.nrxjava.bean.Hero;
import com.necho.nrxjava.bean.Student;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CommonActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
    }

    /**
     * RxJava2基本使用
     *
     * @param view
     */
    public void click1(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {//1.创建一个被观察者

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("你好"); //4. 被观察者发送事件通知观察者
                emitter.onNext("深圳");
                emitter.onNext("休息一下");
                emitter.onComplete();
                emitter.onNext("么么哒");
                Log.d(TAG, "subscribe: send msg finish ...");
            }
        }).subscribe(new Observer<String>() {//3. 观察者订阅被观察者
            //2. 创建一个观察者
            private int i;
            private Disposable mDisposable;
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                mDisposable = d;
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
                i++;
                if (i == 2) {
                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                    Log.d(TAG, "onNext: " + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }

    /**
     * just创建事件队列
     * just(T...): 将传入的参数依次发送出来。
     *
     * @param view
     */
    public void click2(View view) {
        Observable.just("深圳", "欢迎你", "世界之窗")//1.创建被观察者
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }


    /**
     * from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
     *
     * @param view
     */
    public void click3(View view) {
        String[] address = new String[]{"深圳", "南山区", "科技园"};
        Observable.fromArray(address).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }

    /**
     * RxJava线程调度
     * <p>
     * 在不指定线程的情况下， RxJava 遵循的是线程不变的原则，
     * 即：在哪个线程调用 subscribe()，就在哪个线程生产事件；
     * 在哪个线程生产事件，就在哪个线程消费事件。如果需要切换线程，就需要用到 Scheduler （调度器）。
     *
     * @param view
     */
    public void click4(View view) {
        Log.d(TAG, "click4: " + Thread.currentThread().getName());
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "subscribe: " + Thread.currentThread().getName());
                emitter.onNext(7);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())//指定事件发生在IO线程
                .observeOn(AndroidSchedulers.mainThread())//指定事件回调在Main线程
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer + "," + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

    }


    /**
     * 线程切换
     *
     * @param view
     */
    public void click5(View view) {
        Observable.just(1, 2, 3, 4)// 指定事件发生在IO线程，由 subscribeOn() 指定
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(// 新线程，由 observeOn() 指定
                        new Function<Integer, String>() {
                            @Override
                            public String apply(Integer integer) throws Exception {
                                Log.d(TAG, "apply: " + integer + "," + Thread.currentThread().getName());
                                return "张无忌" + integer;
                            }
                        })
                .observeOn(Schedulers.io())
                .map( // IO 线程，由 observeOn() 指定
                        new Function<String, Hero>() {
                            @Override
                            public Hero apply(String s) throws Exception {
                                Log.d(TAG, "apply: " + s + "," + Thread.currentThread().getName());
                                int age = new Random().nextInt(100);
                                Hero hero = new Hero(s, age);
                                return hero;
                            }
                        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(// Android 主线程，由 observeOn() 指定
                        new Observer<Hero>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "onSubscribe: ");
                            }

                            @Override
                            public void onNext(Hero hero) {
                                Log.d(TAG, "onNext: " + hero + "," + Thread.currentThread().getName());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: ");
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");
                            }
                        });
    }
}
