package com.example.dawdawich.maps.activity;


import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dawdawich.maps.ConnectionApi.Connection;
import com.example.dawdawich.maps.R;
import com.example.dawdawich.maps.app.UserController;
import com.example.dawdawich.maps.data.User;
import com.example.dawdawich.maps.fragments.FriendsConfirmProposalFragment;
import com.example.dawdawich.maps.fragments.FriendsListFragment;
import com.example.dawdawich.maps.fragments.FriendsSearchFragment;
import com.example.dawdawich.maps.fragments.FriendsWaitingConfirmFragment;
import com.example.dawdawich.maps.fragments.PagerAdapter;

public class FriendsPagerActivity extends AppCompatActivity{

    static private User user;
    private ViewPager viewPager;
    static private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.friends_tabs_layout);

        user = UserController.getInstance(this).getUser();
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);


        tabLayout.addTab(tabLayout.newTab().setText("Friends (" + (user.getFriends() != null ? user.getFriends().size() : 0) + ")"));
        tabLayout.addTab(tabLayout.newTab().setText("Заявки (" + (user.getProposalFriends() != null ? user.getProposalFriends().size() : 0) + ")"));
        tabLayout.addTab(tabLayout.newTab().setText("Отправленые (" + (user.getWaitingConfirmFriends() != null ? user.getWaitingConfirmFriends().size() : 0) + ")"));
        tabLayout.addTab(tabLayout.newTab().setText("Поиск"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.custom_actionbar_friends);
        actionBar.setDisplayShowCustomEnabled(true);



        final ImageView search = (ImageView)findViewById(R.id.search_friends_image);
        final TextView friendsLabel = (TextView) findViewById(R.id.friends_label);
        final EditText friendsEditText = (EditText) findViewById(R.id.search_friend_label);


        search.setOnClickListener(v -> {

            if (friendsLabel.isEnabled())
            {
                friendsLabel.setEnabled(false);
                friendsLabel.setVisibility(View.INVISIBLE);
                friendsEditText.setEnabled(true);
                friendsEditText.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(3);
            }
            else
            {
                friendsLabel.setEnabled(true);
                friendsLabel.setVisibility(View.VISIBLE);
                friendsEditText.setEnabled(false);
                friendsEditText.setVisibility(View.INVISIBLE);
            }

        });

        final Context cnt = this;

        friendsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (viewPager.getCurrentItem() != 3)
                {
                    viewPager.setCurrentItem(3);
                }

                if (s.toString().equals(""))
                {
                    FriendsSearchFragment.clearAdapter();
                    return;
                }

                Connection.getInstance(cnt).searchUsers(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        tabLayout.getTabAt(0).setText("Friends (" + (user.getWaitingConfirmFriends() != null ? user.getFriends().size() : 0) + ")");
        tabLayout.getTabAt(1).setText("Заявки (" + (user.getWaitingConfirmFriends() != null ? user.getProposalFriends().size() : 0) + ")");
        tabLayout.getTabAt(2).setText("Отправленые (" + (user.getWaitingConfirmFriends() != null ? user.getWaitingConfirmFriends().size() : 0) + ")");
    }

    public static void updateFragments ()
    {
        tabLayout.getTabAt(0).setText("Friends (" + (user.getWaitingConfirmFriends() != null ? user.getFriends().size() : 0) + ")");
        tabLayout.getTabAt(1).setText("Заявки (" + (user.getWaitingConfirmFriends() != null ? user.getProposalFriends().size() : 0) + ")");
        tabLayout.getTabAt(2).setText("Отправленые (" + (user.getWaitingConfirmFriends() != null ? user.getWaitingConfirmFriends().size() : 0) + ")");
        FriendsConfirmProposalFragment.changeList(user.getProposalFriends());
        FriendsListFragment.changeList(user.getFriends());
        FriendsWaitingConfirmFragment.changeList(user.getWaitingConfirmFriends());
    }
}
