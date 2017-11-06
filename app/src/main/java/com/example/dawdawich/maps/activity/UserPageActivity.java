package com.example.dawdawich.maps.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.dawdawich.maps.R;
import com.example.dawdawich.maps.app.UserController;

public class UserPageActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page_layout);
        TextView nickname = (TextView) findViewById(R.id.user_nickname);
        nickname.setText(UserController.getInstance(this).getUserPage().getNickname());

    }
}
