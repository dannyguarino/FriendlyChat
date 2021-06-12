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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class MessageCursorAdapter extends CursorAdapter {


    public MessageCursorAdapter(Context context, Cursor c) { super(context, c, 0); }

//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        return null;
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

//        if(cursor == null){
//            return null;
//        }

        ViewHolder holder = new ViewHolder();
        View v;

        if(cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_AUTHOR)).equals(User.getUsername())) {
            v = LayoutInflater.from(context).inflate(R.layout.item_message_from,
                    parent, false);
            holder.viewType = 0;
        }
        else {
            v = LayoutInflater.from(context).inflate(R.layout.item_message_to,
                    parent, false);
            holder.viewType = 1;
        }

        holder.messageTextView = v.findViewById(R.id.text_view);
        //holder.authorTextView = v.findViewById(R.id.nameTextView);

        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();

        //FriendlyMessage message = getItem(position);
        String text = cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_TEXT));
        String author = cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_AUTHOR));

        if(!TextUtils.isEmpty(text)){
            holder.messageTextView.setText(text);
        }

//        if(!TextUtils.isEmpty(author)){
//            holder.authorTextView.setText(author);
//        }
    }

    @Override
    public int getItemViewType(int position) {

        Cursor cursor = (Cursor) getItem(position);
        if(cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_AUTHOR)).equals(User.getUsername())){
            return 0;
        }
        else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public static class ViewHolder {
        public TextView messageTextView;
//        public TextView authorTextView;
        public int viewType;
    }
}
