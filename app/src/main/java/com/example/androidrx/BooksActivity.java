package com.example.androidrx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {
    private Disposable bookSubscription;
    private TextView textView;
    private ProgressBar progressBar;
    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restClient = new RestClient(this);
        configureLayout();
        addButtonsAction();
    }

    private void createObservable(boolean error) {
        Observable<List<String>> booksObservable;
        if(error) {
            booksObservable =
                    Observable.fromCallable(() -> restClient.getFavoriteBooksWithException());
        } else {
            booksObservable =
                    Observable.fromCallable(() -> restClient.getFavoriteBooks());
        }
        bookSubscription = booksObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> System.out.println("ERROR OCCUR"))
                .subscribe(strings -> displayBooks(strings), throwable -> {
                    progressBar.setVisibility(View.GONE);
                    textView = (TextView) findViewById(R.id.text_view);
                    textView.setText("Error while downloading");
                });
    }

    private void displayBooks(List<String> books) {
        progressBar.setVisibility(View.GONE);
        textView = (TextView) findViewById(R.id.text_view);
        String booksList = "";
        for(String book : books) {
            booksList += book + "\n";
        }
        textView.setText(booksList);
    }

    private void configureLayout() {
        setContentView(R.layout.activity_books);
        progressBar = (ProgressBar) findViewById(R.id.loader);
        progressBar.setVisibility(View.GONE);
    }

    public void addButtonsAction() {
        View.OnClickListener onClickListenerError = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView = (TextView) findViewById(R.id.text_view);
                textView.setText("");
                progressBar.setVisibility(View.VISIBLE);
                createObservable(true);
            }
        };

        Button button_error = (Button) findViewById(R.id.button_load_error);
        button_error.setOnClickListener(onClickListenerError);

        View.OnClickListener onClickListenerSuccess = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView = (TextView) findViewById(R.id.text_view);
                textView.setText("");
                progressBar.setVisibility(View.VISIBLE);
                createObservable(false);
            }
        };

        Button button_success = (Button) findViewById(R.id.button_load_success);
        button_success.setOnClickListener(onClickListenerSuccess);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookSubscription != null && !bookSubscription.isDisposed()) {
            bookSubscription.dispose();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bookSubscription != null && !bookSubscription.isDisposed()) {
            bookSubscription.dispose();
        }
    }
}
