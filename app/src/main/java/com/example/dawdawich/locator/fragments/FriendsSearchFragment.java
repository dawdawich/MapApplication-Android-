package com.example.dawdawich.locator.fragments;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dawdawich.locator.R;
import com.example.dawdawich.locator.activity.UserPageActivity;
import com.example.dawdawich.locator.app.UserController;
import com.example.dawdawich.locator.data.User;
import com.example.dawdawich.locator.data.UserAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FriendsSearchFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static ArrayAdapter<User> adapter;
    private static ViewGroup rootView;
    private static Activity activity;
    private static LayoutInflater inflater;
    private static ViewGroup container;
    private static ListView list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();
        FriendsSearchFragment.inflater = inflater;
        FriendsSearchFragment.container = container;

        if (adapter == null)
        {
            List<User> emptyList = new ArrayList<>();
            emptyList.add(new User(0, "Список пуст"));
            adapter = new UserAdapter(activity, R.layout.friends_list_row_layout,  emptyList);
        }

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_friends_layout, container, false);
        list = (ListView) rootView.findViewById(R.id.friends_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);


        setRetainInstance(true);

        return rootView;

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
            adapter = null;
            return;
        }

        List<User> friendsList = new ArrayList<>();
        friendsList.addAll(users);
        friendsList.remove(UserController.getInstance().getUser());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            UserController.getInstance().getUser().getFriends().forEach(
                    user -> {
                        if (friendsList.contains(user)) {
                            friendsList.remove(user);
                        }
                    }
            );
        }
        else
        {
            for (User user : UserController.getInstance().getUser().getFriends())
            {
                if (friendsList.contains(user)) {
                    friendsList.remove(user);
                }
            }
        }

        if (friendsList.size() == 0)
        {
            clearAdapter();
            return;
        }

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

    public static void clearAdapter () {
        List<User> emptyList = new ArrayList<>();
        emptyList.add(new User(0, "Список пуст"));
        if (adapter == null) {
            adapter = new UserAdapter(activity, R.layout.friends_list_row_layout, emptyList);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else {
            adapter.clear();
            adapter.addAll(emptyList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        TextView friendsLabel = (TextView) getActivity().findViewById(R.id.friends_label);
        EditText friendsEditText = (EditText) getActivity().findViewById(R.id.search_friend_label);
        friendsLabel.setEnabled(true);
        friendsLabel.setVisibility(View.VISIBLE);
        friendsEditText.setEnabled(false);
        friendsEditText.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        TextView friendsLabel = (TextView) getActivity().findViewById(R.id.friends_label);
        EditText friendsEditText = (EditText) getActivity().findViewById(R.id.search_friend_label);
        friendsLabel.setEnabled(false);
        friendsLabel.setVisibility(View.INVISIBLE);
        friendsEditText.setEnabled(true);
        friendsEditText.setVisibility(View.VISIBLE);
    }
}
