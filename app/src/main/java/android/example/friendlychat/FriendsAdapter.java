package android.example.friendlychat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.example.friendlychat.Friend;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FriendsAdapter extends ArrayAdapter<Friend> {

    public FriendsAdapter(@NonNull Context context, int resource, @NonNull List<Friend> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_friend, parent, false);
        }

        TextView name = convertView.findViewById(R.id.tv_username);
        TextView email = convertView.findViewById(R.id.tv_userEmail);

        Friend friend = getItem(position);

        name.setText(friend.getName());
        email.setText(friend.getEmailId());

        return convertView;
    }
}
