package com.example.androidrx;

import android.widget.TextView;
import android.os.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Observable<Integer> serverDownloadObservable = Observable.create(emitter -> {
            for(int i = 0; i <= 10; i++) {
                Thread.sleep(1000); // simulate delay
                emitter.onNext(i);
            }
            emitter.onComplete();
        });

        serverDownloadObservable.
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(integer -> {
                    System.out.println("integer: " + integer);
                    updateTheUserInterface(integer); // this methods updates the ui
                });
    }

    public void updateTheUserInterface(int number) {
        TextView hello_world_text = (TextView) findViewById(R.id.hello_world);
        hello_world_text.setText("Hello World from Rx: " + number);
    }
}