package android.example.friendlychat.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URL;

public class MessageContract {

    public static final String CONTENT_AUTHORITY = "android.example.friendlychat";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MESSAGES = "messages";

    /* Inner class that defines the table contents of the messages table */
    public static final class MessageEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MESSAGES)
                .build();

        public static final String TABLE_NAME = "messages";

        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_PHOTO_URL = "photoUrl";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_ROOMID = "roomId";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
