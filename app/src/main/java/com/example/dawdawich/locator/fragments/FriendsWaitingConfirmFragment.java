package com.example.dawdawich.locator.fragments;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dawdawich.locator.ConnectionApi.UserInfoController;
import com.example.dawdawich.locator.R;
import com.example.dawdawich.locator.activity.UserPageActivity;
import com.example.dawdawich.locator.app.UserController;
import com.example.dawdawich.locator.data.User;
import com.example.dawdawich.locator.data.UserAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FriendsWaitingConfirmFragment extends Fragment implements AdapterView.OnItemClickListener{

    private static ArrayAdapter<User> adapter;
    private static ViewGroup rootView;
    private static Activity activity;
    private static LayoutInflater inflater;
    private static ViewGroup container;
    private static ListView list;
    private Set<User> friendsWaiting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();
        FriendsWaitingConfirmFragment.inflater = inflater;
        FriendsWaitingConfirmFragment.container = container;


        friendsWaiting = UserController.getInstance().getUser().getWaitingConfirmFriends();

        if (friendsWaiting != null && friendsWaiting.size() != 0) {
                List<User> userList = new ArrayList<>();
                userList.addAll(friendsWaiting);
                adapter = new UserAdapter(activity, R.layout.friends_list_row_layout, userList);
            }
            else
            {
                List<User> emptyList = new ArrayList<>();
                emptyList.add(new User(0, "Список пуст"));
                adapter = new UserAdapter(activity, R.layout.friends_list_row_layout, emptyList);
            }



        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_friends_layout, container, false);
        list = (ListView) rootView.findViewById(R.id.friends_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);


        setRetainInstance(true);

        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        UserInfoController.isUpdate(UserController.getInstance().getUser().getId());
        friendsWaiting = UserController.getInstance().getUser().getWaitingConfirmFriends();
        if (friendsWaiting != null && friendsWaiting.size() != 0) {
            List<User> userList = new ArrayList<>();
            userList.addAll(friendsWaiting);
            adapter = new UserAdapter(activity, R.layout.friends_list_row_layout, userList);
        }
        else
        {
            List<User> emptyList = new ArrayList<>();
            emptyList.add(new User(0, "Список пуст"));
            adapter = new UserAdapter(activity, R.layout.friends_list_row_layout, emptyList);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getItem(position) != null && adapter.getItem(position).getId() != 0) {
            UserController.getInstance().setUserPage(adapter.getItem(position));
            getActivity().startActivity(new Intent(getContext(), UserPageActivity.class));
        }
    }

    public static void changeList(Set<User> users)
    {
        if (users == null || users.size() == 0)
        {
            rootView = (ViewGroup)inflater.inflate(R.layout.empty_list_view, container, false);
            adapter = null;
            return;
        }

        List<User> friendsList = new ArrayList<>();
        friendsList.addAll(users);

        if (adapter == null) {
            adapter = new UserAdapter(activity, R.layout.friends_list_row_layout, friendsList);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else
        {
            adapter.clear();
            adapter.addAll(friendsList);
            adapter.notifyDataSetChanged();
        }
    }

}
