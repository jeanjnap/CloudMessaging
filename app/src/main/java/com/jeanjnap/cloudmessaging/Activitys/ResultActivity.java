package com.jeanjnap.cloudmessaging.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jeanjnap.cloudmessaging.R;

public class ResultActivity extends AppCompatActivity {

    String text;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textView = findViewById(R.id.textView);

        text = getIntent().getStringExtra("text");

        if(text != null)
            textView.setText(text);
    }
}
