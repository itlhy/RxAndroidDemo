package com.lhy.rxandroiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        observableCreate();
//        simpleObservableCreate();
//        justTest();
//        rangeTest();
//        repeatTest();
    }

    /**
     * repeat的使用,重复打印5次just()里的参数,一共会打印(just参数个数*repeat参数值)次
     */
    private void repeatTest() {
        Observable.just(1,2).repeat(5).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "integer:" + integer);
            }
        });
    }

    /**
     * range的使用,从第一个参数的值开始,每次自增一个数,连续自增10(第二个参数)次
     */
    private void rangeTest() {
        Observable.range(3, 10).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "integer:" + integer);
            }
        });
    }

    /**
     * just的使用,依次传递数据,会调用四次call方法,把1234依次打印出来
     */
    private void justTest() {
        Observable.just(1, 2, 3, 4).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", "integer:" + integer);
            }
        });
    }

    /**
     * 简单的使用(传递字符串,int等)
     */
    private void simpleObservableCreate() {
        Observable<String> observable = Observable.just("我要喝果汁");
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("MainActivity", "Action1__call: " + s);
            }
        });
    }

    /**
     * 标准的使用
     */
    private void observableCreate() {
        Subscriber<String> subscriber = new Subscriber<String>() {//观察者
            @Override
            public void onCompleted() {
                Log.d("MainActivity", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d("MainActivity", "onNext__1: " + s);
            }
        };

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {//被观察者
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("我要喝水");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(subscriber);//建立关系
    }
}
