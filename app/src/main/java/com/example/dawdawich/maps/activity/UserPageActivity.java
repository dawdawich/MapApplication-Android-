package com.example.dawdawich.maps.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dawdawich.maps.ConnectionApi.Connection;
import com.example.dawdawich.maps.R;
import com.example.dawdawich.maps.app.AppController;
import com.example.dawdawich.maps.app.UserController;
import com.example.dawdawich.maps.data.User;

public class UserPageActivity extends AppCompatActivity {

    private static Button add_friend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page_layout);
        TextView nickname = (TextView) findViewById(R.id.user_nickname);
        nickname.setText(UserController.getInstance(this).getUserPage().getNickname());
        add_friend = (Button) findViewById(R.id.add_friend);
        User user = UserController.getInstance(this).getUser();
        User currentUser = UserController.getInstance(this).getUserPage();
        if (user.getFriends().contains(currentUser)) {
            add_friend.setText("already friend");
            add_friend.setBackgroundColor(Color.GRAY);
        }
        else if (user.getWaitingConfirmFriends() != null && user.getWaitingConfirmFriends().contains(currentUser))
        {
            add_friend.setText("waiting for confirm");
            add_friend.setBackgroundColor(Color.GRAY);
        }
        else if (user.getProposalFriends() != null && user.getProposalFriends().contains(currentUser))
        {
            add_friend.setText("confirm");
            add_friend.setOnClickListener(view -> Connection.getInstance(this).
                    confirmInvite(user.getId(), currentUser.getId()));
        }
        else
        {
            add_friend.setText("send invite");
            add_friend.setOnClickListener(view -> Connection.getInstance(this).
                    sendInvite(user.getId(), currentUser.getId()));
        }

    }

    public static void setWaiting()
    {
        add_friend.setText("waiting for confirm");
        add_friend.setBackgroundColor(Color.GRAY);
    }

    public static void setFriend()
    {
        add_friend.setText("already friend");
        add_friend.setBackgroundColor(Color.GRAY);
    }
}
