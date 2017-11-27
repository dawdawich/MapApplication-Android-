package com.example.dawdawich.locator.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dawdawich.locator.ConnectionApi.UserBoundsController;
import com.example.dawdawich.locator.R;
import com.example.dawdawich.locator.interfaces.UpdateUser;
import com.example.dawdawich.locator.app.AppController;
import com.example.dawdawich.locator.app.UserController;
import com.example.dawdawich.locator.data.User;
import com.example.dawdawich.locator.fragments.FriendsConfirmProposalFragment;
import com.example.dawdawich.locator.fragments.FriendsListFragment;
import com.example.dawdawich.locator.fragments.FriendsSearchFragment;
import com.example.dawdawich.locator.fragments.FriendsWaitingConfirmFragment;
import com.example.dawdawich.locator.fragments.PagerAdapter;

public class FriendsPagerActivity extends AppCompatActivity implements UpdateUser{

    static private User user;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.friends_tabs_layout);

        user = UserController.getInstance().getUser();
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);



        tabLayout.addTab(tabLayout.newTab().setText("Friends (" + (user.getFriends() != null ? user.getFriends().size() : 0) + ")"));
        tabLayout.addTab(tabLayout.newTab().setText("Заявки (" + (user.getProposalFriends() != null ? user.getProposalFriends().size() : 0) + ")"));
        tabLayout.addTab(tabLayout.newTab().setText("Отправленые (" + (user.getWaitingConfirmFriends() != null ? user.getWaitingConfirmFriends().size() : 0) + ")"));
        tabLayout.addTab(tabLayout.newTab().setText("Поиск"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);

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
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.custom_actionbar_friends);
            actionBar.setDisplayShowCustomEnabled(true);
        }



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

                UserBoundsController.searchUsers(s.toString());

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
        AppController.getInstance().setUpdate(this);
    }


    @Override
    public void updateData() {
        tabLayout.getTabAt(0).setText("Friends (" + (user.getWaitingConfirmFriends() != null ? user.getFriends().size() : 0) + ")");
        tabLayout.getTabAt(1).setText("Заявки (" + (user.getWaitingConfirmFriends() != null ? user.getProposalFriends().size() : 0) + ")");
        tabLayout.getTabAt(2).setText("Отправленые (" + (user.getWaitingConfirmFriends() != null ? user.getWaitingConfirmFriends().size() : 0) + ")");
        FriendsConfirmProposalFragment.changeList(user.getProposalFriends());
        FriendsListFragment.changeList(user.getFriends());
        FriendsWaitingConfirmFragment.changeList(user.getWaitingConfirmFriends());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (AppController.getInstance().getUpdate() instanceof FriendsPagerActivity)
        {
            AppController.getInstance().setUpdate(null);
        }
    }
}
