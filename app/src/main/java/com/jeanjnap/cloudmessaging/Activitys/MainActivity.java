package com.jeanjnap.cloudmessaging.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.jeanjnap.cloudmessaging.R;
import com.jeanjnap.cloudmessaging.fcm.MyFirebaseMessagingService;

public class MainActivity extends AppCompatActivity {

    Button button;
    String token = null;
    TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.textView2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token = FirebaseInstanceId.getInstance().getToken();
                if(token != null){
                    Log.i("RES", "token: " + token);
                    textView2.setText(token);
                }
            }
        });

    }



}
