package com.example.dawdawich.maps.data;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dawdawich.maps.R;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

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

    @Nullable
    @Override
    public User getItem(int position) {
        return super.getItem(position);
    }

}
