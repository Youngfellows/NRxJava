package com.necho.nrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;

import com.necho.nrxjava.bean.Course;
import com.necho.nrxjava.bean.Person;
import com.necho.nrxjava.bean.Student;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class TransformationActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformation);
    }

    /**
     * map参数对象变换
     * map 基本作用就是将一个 Observable 通过某种函数关系，转换为另一种 Observable
     *
     * @param view
     */
    public void onMap(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(7);
                emitter.onComplete();
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                Log.d(TAG, "apply: " + integer);
                return "这是变换后的字符串" + integer;
            }
        }).subscribe(new Observer<String>() {
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
     * flatMap平铺
     * <p>
     * 它可以把一个发射器 Observable 通过某种方法转换为多个 Observables，然后再把这些分散的 Observables装进一个单一的发射器 Observable。
     * 但有个需要注意的是，flatMap 并不能保证事件的顺序，如果需要保证，需要用到我们下面要讲的 ConcatMap。*
     * 需求: 获取每个学生的每一门课程名称
     *
     * @param view
     */
    public void onFlatMap(View view) {
        //List<Student> students = new ArrayList();
        Course[] courses = new Course[3];
        courses[0] = new Course("语文");
        courses[1] = new Course("英文");
        courses[2] = new Course("物理");
        Student xiaoming = new Student("小明", 22, courses);

        Course[] courses1 = new Course[3];
        courses1[0] = new Course("数学");
        courses1[1] = new Course("地理");
        courses1[2] = new Course("化学");
        Student xiaoli = new Student("小李", 23, courses1);

        Student[] students = new Student[]{xiaoming, xiaoli};

        Observable.fromArray(students)
                .flatMap(new Function<Student, ObservableSource<Course>>() {
                    @Override
                    public ObservableSource<Course> apply(Student student) throws Exception {
                        Log.d(TAG, "apply: " + student);
                        return Observable.fromArray(student.getCourses());
                    }
                })
                .subscribe(new Observer<Course>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Course course) {
                        Log.d(TAG, "onNext: " + course);
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
     * flatMap平铺转化是无序的
     *
     * @param view
     */
    public void onFlatMap2(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "subscribe: " + Thread.currentThread().getName());
                if (!emitter.isDisposed()) {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                    //emitter.onComplete();
                }
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                Log.d(TAG, "apply: " + Thread.currentThread().getName() + "," + integer);
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    list.add("这是字符串" + integer);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                Log.d(TAG, "apply: delayTime: " + delayTime);
                Observable<String> observable = Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
                return observable;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                    }
                });
    }


    /**
     * concatMap 与 FlatMap 的唯一区别
     * 就是 concatMap 保证了顺序
     *
     * @param view
     */
    public void onconcatMap(View view) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "subscribe: " + Thread.currentThread().getName());
                if (!emitter.isDisposed()) {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                    //emitter.onComplete();
                }
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                Log.d(TAG, "apply: " + Thread.currentThread().getName() + "," + integer);
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    list.add("这是字符串" + integer);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                Log.d(TAG, "apply: delayTime: " + delayTime);
                Observable<String> observable = Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
                return observable;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                    }
                });
    }


    /**
     * zip 专用于合并事件，该合并不是连接（连接操作符后面会说），而是两两配对，也就意味着，最终配对出的 Observable 发射事件数目只和少的那个相同。
     */
    public void onZip(View view) {
        Observable.zip(getStringObservable(), getIntegerObservable(), new BiFunction<String, Integer, String>() {
            //1.合并被观察者
            @Override
            public String apply(String s, Integer integer) throws Exception {
                Log.d(TAG, "apply: " + s + "," + integer);
                return s + integer;//4.被观察者发送事件
            }
        }).subscribe(//3. 注册观察者
                new Consumer<String>() {//2.创建观察者
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);//5.观察者接收被观察者发送的事件
                    }
                });
    }


    /**
     * 创建一个发射字符串的被观察者
     *
     * @return 被观察者
     */
    private Observable<String> getStringObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                boolean disposed = emitter.isDisposed();
                Log.d(TAG, "subscribe: " + disposed);
                if (!disposed) {
                    emitter.onNext("A");
                    Log.d(TAG, "subscribe: A");
                    emitter.onNext("B");
                    Log.d(TAG, "subscribe: B");
                    emitter.onNext("C");
                    Log.d(TAG, "subscribe: C");
                    emitter.onComplete();
                    Log.d(TAG, "subscribe: onComplete ...");
                }
            }
        });
    }

    /**
     * 创建一个发射整数的被观察者
     *
     * @return
     */
    private Observable<Integer> getIntegerObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                boolean disposed = emitter.isDisposed();
                Log.d(TAG, "subscribe: " + disposed);
                if (!disposed) {
                    emitter.onNext(1);
                    Log.d(TAG, "subscribe: 1");
                    emitter.onNext(2);
                    Log.d(TAG, "subscribe: 2");
                    emitter.onNext(3);
                    Log.d(TAG, "subscribe: 3");
                    emitter.onNext(4);
                    Log.d(TAG, "subscribe: 4");
                    emitter.onNext(5);
                    Log.d(TAG, "subscribe: 5");
                    emitter.onComplete();
                    Log.d(TAG, "subscribe: onComplete xxx");
                }
            }
        });
    }

    /**
     * Concat把两个发射器连接成一个发射器
     *
     * @param view
     */
    public void onConcat(View view) {
        Observable.concat(getStringObservable(), getIntegerObservable())
                .subscribe(new Consumer<Serializable>() {
                    @Override
                    public void accept(Serializable serializable) throws Exception {
                        Log.w(TAG, "accept: " + serializable);
                    }
                });

        Observable.concat(Observable.just(11, 22, 33), Observable.just(44, 55, 66, 77, 88))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * distinct去重
     *
     * @param view
     */
    public void onDistinct(View view) {
        Observable.just("小明", "小李", "老吴", "小明", "老张", "小李", "晓燕")
                .distinct()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                    }
                });

        Log.d(TAG, "onDistinct: ==================");
        String[] address = new String[]{"北京", "上海", "北京", "深圳", "广州", "成都", "上海", "南宁"};
        Observable.fromArray(address)
                .distinct()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                    }
                });

        Log.d(TAG, "onDistinct: ======================");
        List<String> fruits = Arrays.asList("苹果", "香蕉", "火龙果", "李子", "香蕉", "西瓜", "苹果", "李子");
        Observable.fromIterable(fruits)
                .distinct()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                    }
                });
    }


    /**
     * Filter条件过滤
     *
     * @param view
     */
    public void onFilter(View view) {
        List<Person> persons = new ArrayList<Person>() {
            {
                add(new Person("张无忌", 23));
                add(new Person("赵敏", 18));
                add(new Person("周芷若", 24));
                add(new Person("杨过", 16));
                add(new Person("小龙女", 32));
            }
        };

        Observable.fromIterable(persons)//1.被观察者发生事件
                .filter(new Predicate<Person>() {
                    @Override
                    public boolean test(Person person) throws Exception {
                        return person.getAge() > 20;//2.条件过滤
                    }
                })
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        Log.d(TAG, "accept: " + person);//3.观察者接收过滤后的结果
                    }
                });
    }


    /**
     * buffer 操作符接受两个参数，buffer(count,skip)，
     * 作用是将 Observable 中的数据按 skip (步长) 分成最大不超过 count 的 buffer ，然后生成一个 Observable
     * skip: 每一组的步长
     * count: 每一组最多多少个
     *
     * @param view
     */
    public void onBuffer(View view) {
        List<Person> persons = new ArrayList<Person>() {
            {
                add(new Person("张无忌", 23));
                add(new Person("赵敏", 18));
                add(new Person("周芷若", 24));
                add(new Person("杨过", 16));
                add(new Person("小龙女", 32));
                add(new Person("令狐冲", 27));
                add(new Person("风清扬", 64));
                add(new Person("西门吹雪", 29));
                add(new Person("林冲", 25));
            }
        };

        Observable.fromIterable(persons)
                .buffer(3, 2)
                .subscribe(new Consumer<List<Person>>() {
                    @Override
                    public void accept(List<Person> people) throws Exception {
                        Log.d(TAG, "accept: " + people.size());
                        for (Person person : people) {
                            Log.d(TAG, "accept: " + person);
                        }
                    }
                });
    }


    /**
     * Timer定时任务
     *
     * @param view
     */
    public void onTimer(View view) {
        final long startTime = System.currentTimeMillis();
        Log.d(TAG, "onTimer: " + startTime);
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: " + aLong);
                        long endTime = System.currentTimeMillis();
                        Log.d(TAG, "accept: " + endTime + "," + (endTime - startTime));
                    }
                });
    }

    private long startTime;

    /**
     * interval 操作符用于间隔时间执行某个操作，
     * 其接受三个参数，
     * 分别是第一次发送延迟，
     * 间隔时间，
     * 时间单位
     */
    public void onInterval(View view) {
        startTime = System.currentTimeMillis();
        Log.d(TAG, "onInterval: " + startTime);
        mDisposable = Observable.interval(2, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())//1.指定事件发生在IO线程
                .observeOn(AndroidSchedulers.mainThread())//2.指定事件回调在主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: " + aLong);
                        long endTime = System.currentTimeMillis();
                        Log.d(TAG, "accept: " + endTime + "," + (endTime - startTime));
                        startTime = endTime;
                    }
                });
    }

    /**
     * doOnNext它的作用是让订阅者在接收到数据之前干点有意思的事情。
     * 假如我们在获取到数据之前想先保存一下它，无疑我们可以这样实现。
     *
     * @param view
     */
    public void doOnNext(View view) {
        List<Person> persons = new ArrayList<Person>() {
            {
                add(new Person("张无忌", 23));
                add(new Person("赵敏", 18));
                add(new Person("周芷若", 24));
                add(new Person("杨过", 16));
                add(new Person("小龙女", 32));
                add(new Person("令狐冲", 27));
                add(new Person("风清扬", 64));
                add(new Person("西门吹雪", 29));
                add(new Person("林冲", 25));
            }
        };

        Observable.fromIterable(persons)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        Log.d(TAG, "doOnNext: " + Thread.currentThread().getName());
                        Log.d(TAG, "doOnNext: 保存到数据库 ..." + person);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        Log.d(TAG, "accept: " + Thread.currentThread().getName());
                        Log.d(TAG, "accept: " + person);
                    }
                });
    }


    /**
     * skip跳过 count 个数目开始接收
     *
     * @param view
     */
    public void onSkip(View view) {
        List<Person> persons = new ArrayList<Person>() {
            {
                add(new Person("张无忌", 23));
                add(new Person("赵敏", 18));
                add(new Person("周芷若", 24));
                add(new Person("杨过", 16));
                add(new Person("小龙女", 32));
                add(new Person("令狐冲", 27));
                add(new Person("风清扬", 64));
                add(new Person("西门吹雪", 29));
                add(new Person("林冲", 25));
            }
        };

        Observable.fromIterable(persons)
                .skip(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        Log.d(TAG, "accept: " + person + "," + Thread.currentThread().getName());
                    }
                });
    }

    /**
     * take，接受一个 long 型参数 count ，代表至多接收 count 个数据。
     */
    public void onTake(View view) {
        List<Person> persons = new ArrayList<Person>() {
            {
                add(new Person("张无忌", 23));
                add(new Person("赵敏", 18));
                add(new Person("周芷若", 24));
                add(new Person("杨过", 16));
                add(new Person("小龙女", 32));
                add(new Person("令狐冲", 27));
                add(new Person("风清扬", 64));
                add(new Person("西门吹雪", 29));
                add(new Person("林冲", 25));
            }
        };

        Observable.fromIterable(persons)
                .subscribeOn(Schedulers.io())
                .take(4)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        Log.d(TAG, "accept: " + person);
                    }
                });
    }

    /**
     * just发射器依次调用 onNext() 方法
     *
     * @param view
     */
    public void onJust(View view) {
        Person xiaoming = new Person("小明", 22);
        Person xiaohong = new Person("小红", 23);
        Observable.just(xiaoming, xiaohong)
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        Log.d(TAG, "accept: " + person);
                    }
                });
    }

    /**
     * Single 只会接收一个参数，而 SingleObserver 只会调用 onError() 或者 onSuccess()
     */
    public void onSingle(View view) {
        Single.just(new Random().nextInt(100))
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.d(TAG, "onSuccess: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                    }
                });
    }

    /**
     * debounce
     * 去除发送频率过快的项
     */
    public void onDebounce(View view) {
        Observable
                .create(new ObservableOnSubscribe<Person>() {
                    @Override
                    public void subscribe(ObservableEmitter<Person> emitter) throws Exception {
                        emitter.onNext(new Person("杨过", 22));//skip
                        Thread.sleep(100);
                        emitter.onNext(new Person("小龙女", 23));//deliver
                        Thread.sleep(505);
                        emitter.onNext(new Person("郭靖", 24));//skip
                        Thread.sleep(400);
                        emitter.onNext(new Person("黄蓉", 25));//deliver
                        Thread.sleep(1000);
                        emitter.onNext(new Person("李寻欢", 26));//skip
                        Thread.sleep(300);
                        emitter.onNext(new Person("武松", 27));//deliver
                        Thread.sleep(600);
                    }
                })
                .subscribeOn(Schedulers.io())
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        Log.d(TAG, "accept: " + person);
                    }
                });
    }

    /**
     * defer简单地时候就是每次订阅都会创建一个新的 Observable，并且如果没有被订阅，就不会产生新的 Observable
     *
     * @param view
     */
    public void onDefer(View view) {
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> call() throws Exception {
                Log.d(TAG, "call: ");
                return Observable.just(1, 2, 3, 4);
            }
        });

        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
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
     * last 操作符仅取出可观察到的最后一个值，或者是满足某些条件的最后一项
     */
    public void onLast(View view) {
        Observable.just(1, 2, 3, 5)
                .last(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * merge合并
     *
     * @param view
     */
    public void onMerge(View view) {
        List<Person> p1 = new ArrayList<Person>() {
            {
                add(new Person("张无忌", 23));
                add(new Person("赵敏", 18));
                add(new Person("周芷若", 24));
                add(new Person("杨过", 16));
                add(new Person("小龙女", 32));
            }
        };

        List<Person> p2 = new ArrayList<Person>() {
            {
                add(new Person("令狐冲", 27));
                add(new Person("风清扬", 64));
                add(new Person("西门吹雪", 29));
                add(new Person("林冲", 25));
            }
        };

        Observable<Person> observable1 = Observable.fromIterable(p1);
        Observable<Person> observable2 = Observable.fromIterable(p2);
        Observable.merge(observable1, observable2)
                .subscribe(new Consumer<Person>() {
                    @Override
                    public void accept(Person person) throws Exception {
                        Log.d(TAG, "accept: " + person);
                    }
                });

    }

    /**
     * reduce 操作符每次用一个方法处理一个值，可以有一个 seed 作为初始值。
     * 同递归
     */
    public void onReduce(View view) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            list.add(i);
        }

        Observable.fromIterable(list)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.d(TAG, "apply: " + integer + "," + integer2);
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * scan 操作符作用和上面的 reduce 一致，唯一区别是 reduce 是个只追求结果的坏人，而 scan 会始终如一地把每一个步骤都输出
     */
    public void onScan(View view) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            list.add(i);
        }

        Observable.fromIterable(list)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.d(TAG, "apply: " + integer + "," + integer2);
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            Log.d(TAG, "onDestroy: ");
            mDisposable.dispose();//取消定时任务
        }
    }
}
