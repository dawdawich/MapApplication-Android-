package com.example.dawdawich.maps.fragments;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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

import com.example.dawdawich.maps.R;
import com.example.dawdawich.maps.activity.UserPageActivity;
import com.example.dawdawich.maps.app.UserController;
import com.example.dawdawich.maps.data.User;
import com.example.dawdawich.maps.data.UserAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FriendsConfirmProposalFragment extends Fragment implements AdapterView.OnItemClickListener {

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
        FriendsConfirmProposalFragment.inflater = inflater;
        FriendsConfirmProposalFragment.container = container;

        if (adapter == null)
        {
            if (UserController.getInstance(getContext()).getUser().getProposalFriends().size() == 0) {
                List<User> userList = new ArrayList<>();
                userList.addAll(UserController.getInstance(getContext()).getUser().getProposalFriends());
                adapter = new UserAdapter(activity, R.layout.friends_list_row_layout, userList);
            }
            else
            {
                List<User> emptyList = new ArrayList<>();
                emptyList.add(new User(0, "Список пуст"));
                adapter = new UserAdapter(activity, R.layout.friends_list_row_layout, emptyList);
            }
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
        if (UserController.getInstance(getContext()).getUser().getProposalFriends().size() != 0) {
            List<User> userList = new ArrayList<>();
            userList.addAll(UserController.getInstance(getContext()).getUser().getProposalFriends());
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
            UserController.getInstance(getContext()).setUserPage(adapter.getItem(position));
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
