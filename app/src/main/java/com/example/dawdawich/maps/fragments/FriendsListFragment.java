package com.example.dawdawich.maps.fragments;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dawdawich.maps.R;
import com.example.dawdawich.maps.app.UserController;
import com.example.dawdawich.maps.data.User;

public class FriendsListFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView;

        if (UserController.getInstance(getContext()).getUser().getFriends() == null || UserController.getInstance(getContext()).getUser().getFriends().size() == 0)
        {
            rootView = (ViewGroup)inflater.inflate(R.layout.empty_list_view, container, false);
        }
        else {
            ArrayAdapter<User> adapter = new ArrayAdapter<User>(getActivity(), R.layout.friends_list_row_layout, R.id.txtitem, (User[]) UserController.getInstance(getContext()).getUser().getFriends().toArray());
            rootView = (ViewGroup) inflater.inflate(R.layout.fragment_friends_layout, container, false);
            ListView list = (ListView) rootView.findViewById(R.id.friends_list);
            list.setAdapter(adapter);
        }

        setRetainInstance(true);

        return rootView;

    }




}
