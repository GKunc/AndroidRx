package com.example.androidrx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FilterMapActivity extends AppCompatActivity {
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_map);
        addButtonAction();

        final Observable<Integer> serverDownloadObservable = Observable.create(emitter -> {
            for(int i = 0; i <= 10; i++) {
                Thread.sleep(1000); // simulate delay
                emitter.onNext(i);
            }
            emitter.onComplete();
        });

        disposable = serverDownloadObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(i -> i % 2 == 0)
                .map(i -> i * 10)
                .subscribe(i -> {
                    System.out.println("SHOW: " + i);
                    setTextField("SHOW: " + i);
                });
    }

    public void setTextField(String text) {
        TextView example_2_text = (TextView) findViewById(R.id.example_2);
        example_2_text.setText(text);
    }

    public void addButtonAction() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICK");
                startActivity(new Intent(FilterMapActivity.this, BooksActivity.class));
            }
        };

        Button button = (Button) findViewById(R.id.button_navigate);
        button.setOnClickListener(onClickListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}