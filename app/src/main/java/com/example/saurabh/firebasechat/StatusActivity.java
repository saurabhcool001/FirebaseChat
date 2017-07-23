package com.example.saurabh.firebasechat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    Context context;

    private Toolbar mToolbar;
    private TextInputLayout mStatus;
    private Button mSaveBtn;
    private ProgressDialog mProgress;

    private FirebaseUser mCurrentUser;
    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        context = getApplicationContext();
        FirebaseApp.initializeApp(context);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mDatabaseRef = Database.getReference().child("Users").child(uid);

        mToolbar = (Toolbar) findViewById(R.id.status_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value = getIntent().getStringExtra("status_value").toString();

        mStatus = (TextInputLayout) findViewById(R.id.status_input);
        mSaveBtn = (Button) findViewById(R.id.status_save_btn);
        mProgress = new ProgressDialog(this);

        mStatus.getEditText().setText(status_value);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Please wait while we save the changes");
                mProgress.setCanceledOnTouchOutside(true);
                mProgress.show();

                String status = mStatus.getEditText().getText().toString();

                if (!TextUtils.isEmpty(status)) {

                    mDatabaseRef.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgress.dismiss();
                                Intent settingIntent = new Intent(StatusActivity.this, SettingActivity.class);
                                startActivity(settingIntent);
                            } else {
                                mProgress.hide();
                                Toast.makeText(StatusActivity.this, "There was error in saving changes.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    mProgress.hide();
                    Toast.makeText(StatusActivity.this, "Enter Your Display Status.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
