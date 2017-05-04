package com.lhy.rxandroiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
//        subscriberRunInSubThread();
//        mapTest1();
//        mapTest2();
//        fromTest1();
//        fromAndMapTest();
//        flatMapTest1();
//        flatMapTest2();
//        filterTest();
//        takeTest();
//        observableWork();
//        distinctUntilChangedTest();
    }

    /**
     * 只传递发生变化的值,注意与distinct(去重复)的区别
     */
    private void distinctUntilChangedTest() {
        Observable.just(12,12,13,14,13,14).distinctUntilChanged().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", integer.toString());
            }
        });
    }

    /**
     * 练习:
     * 去重复,拿到大于1的前4位数字
     */
    private void observableWork() {
        String[] numbers = {"1", "2", "2", "3", "4", "4", "5", "6"};
        Observable.from(numbers)
                .map(new Func1<String, Integer>() {//转换成int类型
                    @Override
                    public Integer call(String s) {
                        return Integer.parseInt(s);
                    }
                })
                .distinct()//去重复
                .filter(new Func1<Integer, Boolean>() {//得到大于1的数值
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 1;
                    }
                })
                .take(4)//取前4位数
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d("MainActivity", integer.toString());
                    }
                });
    }

    /**
     * take的参数值n就是要传递的just的参数从第1到n个数
     */
    private void takeTest() {
        Observable.just(1, 2, 2, 4, 4, 5, 6, 7).take(4).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", integer.toString());
            }
        });
    }

    /**
     * 过滤器,条件判断
     */
    private void filterTest() {
        Observable.just(1, 2, 2, 4, 4, 6).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer > 1;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("MainActivity", integer.toString());
            }
        });
    }

    /**
     * flatMap和map的区别
     * 遍历三维数组
     * 当数组是n(n>1)维数组时,为了遍历每一个元素,flatMap的回调方法携带的参数是的n-1层的被观察者
     */
    private void flatMapTest2() {
        String[][][] sets = {{{"小明", "小天"}, {"小黄"}}, {{"小李", "小东"}}, {{"小华"}, {"小墩", "小强"}}};
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("MainActivity", "当前桌位: " + s);
            }
        };
        Observable.from(sets).flatMap(new Func1<String[][], Observable<String>>() {
                                          @Override
                                          public Observable<String> call(String[][] strings) {
                                              return Observable.from(strings).flatMap(new Func1<String[], Observable<String>>() {
                                                  @Override
                                                  public Observable<String> call(String[] strings) {
                                                      return Observable.from(strings);
                                                  }
                                              });
                                          }
                                      }

        ).subscribe(action1);
    }

    /**
     * 遍历二维数组
     * 当数组是n(n>1)维数组时,为了遍历每一个元素,flatMap的回调方法携带的参数是的n-1层的被观察者
     */
    private void flatMapTest1() {
        String[][] sets = {{"小明", "小黄"}, {"小李", "小东"}, {"小华", "小墩", "小强"}};
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("MainActivity", "当前桌位: " + s);
            }
        };
        Observable.from(sets).flatMap(new Func1<String[], Observable<String>>() {
            @Override
            public Observable<String> call(String[] strings) {
                return Observable.from(strings);
            }
        }).subscribe(action1);
    }

    /**
     * from和map结合使用
     */
    private void fromAndMapTest() {
        String[] names = {"小明", "小黄", "小花"};
        Observable.from(names).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return "我的名字是: " + s;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("MainActivity", s);
            }
        });
    }

    /**
     * from的用法,参数支持多种集合,数组类型
     */
    private void fromTest1() {
        String[] names = {"小明", "小黄", "小花"};
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("MainActivity", s);
            }
        });
    }

    /**
     * map,在观察者接收之前,把事件截取后再加工(修改)
     */
    private void mapTest1() {
        Action1<String> action1 = new Action1<String>() {

            @Override
            public void call(String s) {
                Log.d("MainActivity", s);
            }
        };

        Observable.just(1, 2, 3, 4).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return integer.toString();
            }
        }).subscribe(action1);
    }

    private void mapTest2() {
        Action1<String> action1 = new Action1<String>() {

            @Override
            public void call(String s) {
                Log.d("MainActivity", s);
            }
        };

        Observable.just("黄焖鸡", "大盘鸡", "羊肉泡馍").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s + ",微辣";
            }
        }).subscribe(action1);
    }

    /**
     * 观察者在子线程中运行   Schedulers.newThread()
     */
    private void subscriberRunInSubThread() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {//被观察者
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.i("MainActivity", "被观察者发送信息,线程: " + Thread.currentThread().getName());
                subscriber.onNext("吃饭了");
            }
        });
        Action1<String> action1 = new Action1<String>() {//观察者
            @Override
            public void call(String s) {
                Log.i("MainActivity", "观察者收到调用信息:" + s + " ,线程: " + Thread.currentThread().getName());
            }
        };
        observable.observeOn(Schedulers.newThread()).subscribe(action1);
    }

    /**
     * repeat的使用,重复打印5次just()里的参数,一共会打印2*5(just参数个数*repeat参数值)行
     */
    private void repeatTest() {
        Observable.just(1, 2).repeat(5).subscribe(new Action1<Integer>() {
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
