package com.example.dawdawich.maps.fragments;


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

public class FriendsPagerFragment extends Fragment{

    private User user;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.friends_tabs_layout, container, false);

        user = UserController.getInstance(getContext()).getUser();

        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        TabLayout tabLayout = (TabLayout)getActivity().findViewById(R.id.tab_layout);


        tabLayout.addTab(tabLayout.newTab().setText("Friends (" + (user.getFriends() != null ? user.getFriends().size() : 0) + ")"));
        tabLayout.addTab(tabLayout.newTab().setText("Заявки (" + (user.getProposalFriends() != null ? user.getProposalFriends().size() : 0) + ")"));
        tabLayout.addTab(tabLayout.newTab().setText("Отправленые (" + (user.getWaitingConfirmFriends() != null ? user.getWaitingConfirmFriends().size() : 0) + ")"));
        tabLayout.addTab(tabLayout.newTab().setText("Поиск"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager = (ViewPager)getActivity().findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
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

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
        actionBar.setCustomView(R.layout.custom_actionbar_friends);
        actionBar.setDisplayShowCustomEnabled(true);



        final ImageView search = (ImageView)getActivity().findViewById(R.id.search_friends_image);
        final TextView friendsLabel = (TextView) getActivity().findViewById(R.id.friends_label);
        final EditText friendsEditText = (EditText) getActivity().findViewById(R.id.search_friend_label);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                Connection.getInstance(getContext()).searchUsers(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
