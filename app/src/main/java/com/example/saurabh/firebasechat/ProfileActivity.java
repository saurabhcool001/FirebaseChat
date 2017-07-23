package com.example.saurabh.firebasechat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

public class ProfileActivity extends AppCompatActivity {

    Context context;

    private TextView mDisplayId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = getApplicationContext();
        FirebaseApp.initializeApp(context);

        String user_id = getIntent().getStringExtra("user_id").toString();
        mDisplayId = (TextView) findViewById(R.id.profile_display_name);
        mDisplayId.setText(user_id);
    }
}
