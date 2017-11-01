package com.example.dawdawich.maps.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dawdawich.maps.R;
import com.example.dawdawich.maps.app.UserController;
import com.example.dawdawich.maps.data.User;

public class FriendsPagerFragment extends Fragment{

    User user;

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
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager)getActivity().findViewById(R.id.pager);
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
}
