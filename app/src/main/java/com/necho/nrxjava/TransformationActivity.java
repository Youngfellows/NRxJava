package com.necho.nrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.necho.nrxjava.bean.Course;
import com.necho.nrxjava.bean.Student;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class TransformationActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformation);
    }

    /**
     * map参数对象变换
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


}
