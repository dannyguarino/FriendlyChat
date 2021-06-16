package android.example.friendlychat;

import android.content.Context;
import android.database.Cursor;
import android.example.friendlychat.data.MessageContract.MessageEntry;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageCursorAdapter extends CursorAdapter {


    public MessageCursorAdapter(Context context, Cursor c) { super(context, c, 0); }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        View v;

        if(cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_AUTHOR)).equals(User.getUsername())) {
            v = LayoutInflater.from(context).inflate(R.layout.item_message_to,
                    parent, false);
            holder.viewType = 0;
        }
        else {
            v = LayoutInflater.from(context).inflate(R.layout.item_message_from,
                    parent, false);
            holder.viewType = 1;
        }

        holder.messageTextView = v.findViewById(R.id.msg_text_view);
        holder.msgTimeTextView = v.findViewById(R.id.msg_time_text_view);
        holder.photoImageView = v.findViewById(R.id.photoImageView);
        holder.photoTimeTextView = v.findViewById(R.id.photo_time_text_view);
        //holder.authorTextView = v.findViewById(R.id.nameTextView);

        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();

        String text = cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_TEXT));
        String photoUrl = cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_PHOTO_URL));
        long time = cursor.getLong(cursor.getColumnIndex(MessageEntry.COLUMN_TIMESTAMP));
        //String author = cursor.getString(cursor.getColumnIndex(MessageEntry.COLUMN_AUTHOR));

        Date date = new Date(time);
        SimpleDateFormat curFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        String timeString = curFormat.format(date);

        if(text != null && !TextUtils.isEmpty(text)){
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.msgTimeTextView.setVisibility(View.VISIBLE);
            holder.photoImageView.setVisibility(View.GONE);
            holder.photoTimeTextView.setVisibility(View.GONE);
            holder.messageTextView.setText(text);
            holder.msgTimeTextView.setText(timeString);
        }
        else if(photoUrl != null && !TextUtils.isEmpty(photoUrl)){
            holder.messageTextView.setVisibility(View.GONE);
            holder.msgTimeTextView.setVisibility(View.GONE);
            holder.photoImageView.setVisibility(View.VISIBLE);
            holder.photoTimeTextView.setVisibility(View.VISIBLE);
            Glide.with(holder.photoImageView.getContext())
                    .load(photoUrl)
                    .into(holder.photoImageView);
            holder.photoTimeTextView.setText(timeString);
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
        public TextView msgTimeTextView;
        public ImageView photoImageView;
        public TextView photoTimeTextView;
//        public TextView authorTextView;
        public int viewType;
    }
}
