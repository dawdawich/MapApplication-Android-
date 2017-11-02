package com.example.dawdawich.maps.fragments;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dawdawich.maps.R;
import com.example.dawdawich.maps.app.UserController;
import com.example.dawdawich.maps.data.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FriendsSearchFragment extends Fragment {

    private static ArrayAdapter<User> adapter;
    private static ViewGroup rootView;
    private static Activity activity;
    private static LayoutInflater inflater;
    private static ViewGroup container;


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
        ListView list = (ListView) rootView.findViewById(R.id.friends_list);
        list.setAdapter(adapter);


        setRetainInstance(true);

        return rootView;

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
            adapter.notifyDataSetChanged();
        }
        else
        {
//            adapter = new UserAdapter(activity, R.layout.friends_list_row_layout, friendsList);
            adapter.clear();
            adapter.addAll(friendsList);
            adapter.notifyDataSetChanged();
        }



    }

    public static void clearAdapter () {
        List<User> emptyList = new ArrayList<>();
        emptyList.add(new User(0, "Список пуст"));
        adapter.clear();
        adapter.addAll(emptyList);
        adapter.notifyDataSetChanged();
    }

    private static class UserAdapter extends ArrayAdapter<User> {

        private List<User> users;

        public UserAdapter(@NonNull Context context, int resource, @NonNull List<User> users) {
            super(context, resource, users);
            this.users = users;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View v = convertView;

            if (v == null)
            {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.friends_list_row_layout, null);
            }

            User u = users.get(position);

            if (u != null)
            {
                TextView nickname = (TextView) v.findViewById(R.id.txtitem);

                nickname.setText(u.getNickname());
            }

            return v;
        }
    }

}
