package com.example.saurabh.firebasechat;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    Context context;

    private ImageView mProfileImage;
    private TextView mProfileName;
    private TextView mProfileStatus;
    private TextView mProfileFriendsCount;
    private Button mSendFriendRequestBtn;
    private ProgressDialog mProgress;
    private int mCurrent_state;

    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef, mFriendRequestDatabase;
    private FirebaseUser mCurrentUser;
    private String current_userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = getApplicationContext();
        FirebaseApp.initializeApp(context);

        final String user_id = getIntent().getStringExtra("user_id").toString();

        mDatabaseRef = Database.getReference().child("Users").child(user_id);
        mFriendRequestDatabase = Database.getReference().child("Friend_request");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        current_userId = mCurrentUser.getUid();

        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileName = (TextView) findViewById(R.id.profile_display_name);
        mProfileStatus = (TextView) findViewById(R.id.profile_status);
        mProfileFriendsCount = (TextView) findViewById(R.id.profile_totalFriends);
        mSendFriendRequestBtn = (Button) findViewById(R.id.profile_request_send_btn);

        mCurrent_state = 0;

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading User Info");
        mProgress.setMessage("Please wait while we load the user info");
        mProgress.setCanceledOnTouchOutside(true);
        mProgress.show();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);

                Picasso.with(ProfileActivity.this).load(image).placeholder(R.mipmap.user_image_transparent).into(mProfileImage);

                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSendFriendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCurrent_state == 0) {
                    mFriendRequestDatabase.child(current_userId).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                mFriendRequestDatabase.child(user_id).child(current_userId).child("request_type")
                                        .setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(ProfileActivity.this, "Request Sent Successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                Toast.makeText(ProfileActivity.this, "Failed Sending Request.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
