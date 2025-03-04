package com.questionpro.oauth_poc;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        ImageView ivProfilePic = findViewById(R.id.ivProfilePic);

        // Retrieve data
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String photoUrl = getIntent().getStringExtra("photoUrl");

        tvName.setText(name);
        tvEmail.setText(email);
        if (photoUrl != null) {
            // Use your favorite image loading library, e.g., Glide
            Glide.with(this).load(photoUrl).into(ivProfilePic);
        }
    }

}