package com.example.dawdawich.maps.fragments;


import android.app.Activity;
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

        friendsEditText.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return 0;
            }

            @Override
            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {



                return false;
            }

            @Override
            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                return false;
            }

            @Override
            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                return false;
            }

            @Override
            public void clearMetaKeyState(View view, Editable content, int states) {

            }
        });

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ActionBar actionBar = ((FragmentActivity)activity);
        actionBar.setCustomView(R.layout.custom_actionbar_friends);
        actionBar.setDisplayShowCustomEnabled(true);

    }


//    private class FriendsListAdapter extends ArrayAdapter<>

}
