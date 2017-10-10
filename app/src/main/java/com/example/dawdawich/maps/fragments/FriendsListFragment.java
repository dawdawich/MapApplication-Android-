package com.example.dawdawich.maps.fragments;


import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dawdawich.maps.R;

public class FriendsListFragment extends ListFragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_friends_layout, container, false);

        String[] data = {"dawdawich", "edik", "tema"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.friends_list_row_layout, R.id.txtitem, data);

        setListAdapter(adapter);


        setRetainInstance(true);

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

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ActionBar actionBar=((AppCompatActivity)activity).getSupportActionBar();
        actionBar.setCustomView(R.layout.custom_actionbar_friends);
        actionBar.setDisplayShowCustomEnabled(true);

    }

}
