package com.example.dawdawich.locator.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dawdawich.locator.ConnectionApi.UserBoundsController;
import com.example.dawdawich.locator.ConnectionApi.UserInfoController;
import com.example.dawdawich.locator.R;
import com.example.dawdawich.locator.app.AppController;
import com.example.dawdawich.locator.app.UserController;
import com.example.dawdawich.locator.data.User;
import com.example.dawdawich.locator.helper.ImageHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserPageActivity extends AppCompatActivity {

    private static Button add_friend;
    public static final int GET_FROM_GALLERY = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page_layout);
        TextView nickname = (TextView) findViewById(R.id.user_nickname);
        add_friend = (Button) findViewById(R.id.add_friend);
        ImageView avatar = (ImageView) findViewById(R.id.user_avatar);
        User user = UserController.getInstance().getUser();
        User currentUser = UserController.getInstance().getUserPage();
        if (currentUser != null) {
            nickname.setText(currentUser.getNickname());
            if (user.getFriends().contains(currentUser)) {
                add_friend.setText("already friend");
                add_friend.setBackgroundColor(Color.GRAY);
            } else if (user.getWaitingConfirmFriends() != null && user.getWaitingConfirmFriends().contains(currentUser)) {
                add_friend.setText("waiting for confirm");
                add_friend.setBackgroundColor(Color.GRAY);
            } else if (user.getProposalFriends() != null && user.getProposalFriends().contains(currentUser)) {
                add_friend.setText("confirm");
                add_friend.setOnClickListener(view -> UserBoundsController.
                        confirmInvite(user.getId(), currentUser.getId()));
            } else {
                add_friend.setText("send invite");
                add_friend.setOnClickListener(view -> UserBoundsController.
                        sendInvite(user.getId(), currentUser.getId()));
            }
        }
        else
        {
            add_friend.setVisibility(View.INVISIBLE);
            nickname.setText(user.getNickname());
            if (user.getAvatar() != null)
            {
                avatar.setImageBitmap(user.getAvatar());
            }
            avatar.setOnClickListener((view) ->
            {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                bitmap = ImageHelper.getResizedBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10);
                bitmap = ImageHelper.getRoundedCornerBitmap(bitmap);
                String path = UserController.getInstance().getUser().getNickname() + ".png";
                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(path, Context.MODE_PRIVATE);
                    outputStream.write(ImageHelper.getBytes(bitmap));
                    outputStream.flush();
                    outputStream.close();
                    SharedPreferences prefs = AppController.getInstance().getSharedPreferences("user_data", MODE_PRIVATE);
                    prefs.edit().putString("avatar_path", path).apply();
                    UserInfoController.sendAvatar(UserController.getInstance().getUser().getId(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                UserController.getInstance().getUser().setAvatar(bitmap);
                ((ImageView)findViewById(R.id.user_avatar)).setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
