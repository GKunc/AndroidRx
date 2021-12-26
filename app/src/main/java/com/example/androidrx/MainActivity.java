package com.example.androidrx;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.os.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButtonAction();

        final Observable<Integer> serverDownloadObservable = Observable.create(emitter -> {
            for(int i = 0; i <= 10; i++) {
                Thread.sleep(1000); // simulate delay
                emitter.onNext(i);
            }
            emitter.onComplete();
        });

        disposable = serverDownloadObservable.
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(integer -> {
                    System.out.println("integer: " + integer);
                    updateTheUserInterface(integer); // this methods updates the ui
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void updateTheUserInterface(int number) {
        TextView hello_world_text = (TextView) findViewById(R.id.hello_world);
        hello_world_text.setText("Hello World from Rx: " + number);
    }

    public void addButtonAction() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FilterMapActivity.class));
            }
        };

        Button button = (Button) findViewById(R.id.button_navigate);
        button.setText("CLICK ME");
        button.setOnClickListener(onClickListener);
    }
}