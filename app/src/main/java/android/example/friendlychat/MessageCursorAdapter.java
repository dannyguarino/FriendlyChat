package android.example.friendlychat;

import android.content.Context;
import android.database.Cursor;
import android.example.friendlychat.data.MessageContract;
import android.example.friendlychat.data.MessageContract.MessageEntry;
import android.example.friendlychat.data.MessageUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MessageCursorAdapter extends CursorAdapter {


    public MessageCursorAdapter(Context context, Cursor c) { super(context, c, 0); }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_message,
                parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView messageTextView = view.findViewById(R.id.messageTextView);
        TextView authorTextView = view.findViewById(R.id.nameTextView);

        //FriendlyMessage message = getItem(position);
        String text = cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_TEXT));
        String author = cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_AUTHOR));

        if(!TextUtils.isEmpty(text)){
            messageTextView.setText(text);
        }

        if(!TextUtils.isEmpty(author)){
            authorTextView.setText(author);
        }
    }
}
