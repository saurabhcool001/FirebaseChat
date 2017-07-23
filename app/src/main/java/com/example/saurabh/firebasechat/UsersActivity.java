package com.example.saurabh.firebasechat;

import android.content.Context;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class UsersActivity extends AppCompatActivity {

    Context context;

    private Toolbar mToolbar;
    private RecyclerView mUserRecyclerList;

    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        context = getApplicationContext();
        FirebaseApp.initializeApp(context);

        mDatabaseRef = Database.getReference().child("Users");

        mToolbar = (Toolbar) findViewById(R.id.user_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserRecyclerList = (RecyclerView) findViewById(R.id.user_recycler_list);
        mUserRecyclerList.setHasFixedSize(true);
        mUserRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseRef.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Users, UsersViewHolder>(

                        Users.class,
                        R.layout.user_single_layout,
                        UsersViewHolder.class,
                        mDatabaseRef
                ) {
                    @Override
                    protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {

                        viewHolder.setName(model.getName());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                    }
                };

                mUserRecyclerList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setStatus(String status) {
            TextView userStatus = (TextView) mView.findViewById(R.id.user_single_status);
            userStatus.setText(status);
        }

        public void setImage(Context context, String image) {
            ImageView userImage = (ImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(context).load(image).into(userImage);
        }
    }
}
