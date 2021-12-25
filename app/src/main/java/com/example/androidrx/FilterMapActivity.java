package com.example.androidrx;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FilterMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_map);
        TextView example_2_text = (TextView) findViewById(R.id.example_2);
        example_2_text.setText("Example 2");
    }
}