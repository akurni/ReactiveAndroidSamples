package com.appsindotech.reactiveandroidsamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.text_output)
    TextView textOutput;
    @Bind(R.id.btn_subscribe)
    Button btnSubscribe;
    @Bind(R.id.radioButton)
    RadioButton radioButton;
    @Bind(R.id.radioButton2)
    RadioButton radioButton2;
    @Bind(R.id.radioButton3)
    RadioButton radioButton3;
    @Bind(R.id.radioOptions)
    RadioGroup radioOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @OnClick(R.id.btn_subscribe)
    public void subscribe() {

        switch(radioOptions.getCheckedRadioButtonId())
        {
            case R.id.radioButton:
                myObservable.subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        textOutput.setText(s);
                    }
                });
                break;

            case R.id.radioButton2:
                final StringBuilder stringBuilder = new StringBuilder();
                myIntegerListObservable.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        stringBuilder.append(integer);
                        stringBuilder.append(";");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // Do something on error
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        // Do something when completed
                        textOutput.setText(stringBuilder.toString());
                    }
                });
                break;

            case R.id.radioButton3:
                final StringBuilder longListStringBuilder = new StringBuilder();
                myLongIntegerListObservable
                        .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer + 1;
                    }
                }).filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return (integer%2 == 0);
                    }
                }).map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer * 2;
                    }
                })
                        .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        longListStringBuilder.append(integer);
                        longListStringBuilder.append(";");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // Do something on error
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        // Do something when completed
                        textOutput.setText(longListStringBuilder.toString());
                    }
                });

        }

    }

    // Create Observable that emit a string
    Observable<String> myObservable = Observable.create(
            new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> sub) {
                    sub.onNext("Hello, world!");
                    sub.onCompleted();
                }
            }
    );

    // Create Observable the simple way
    Observable<Integer> myIntegerListObservable = Observable.just(1, 2, 3, 4, 5, 6, 10);

    // Create Observable that emits 100 integers
    Observable<Integer> myLongIntegerListObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            for (int i = 1; i <= 100; i++) {
                subscriber.onNext(i);
            }
            subscriber.onCompleted();
        }
    });


}
