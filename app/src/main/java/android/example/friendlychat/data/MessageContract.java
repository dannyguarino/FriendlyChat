package android.example.friendlychat.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MessageContract {

    public static final String CONTENT_AUTHORITY = "android.example.friendlychat";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //public static int DATABASE_VERSION = 1;

    public static final String PATH_MESSAGES = "messages";

//    public static void incrementDatabaseVersion(){
//        DATABASE_VERSION++;
//    }

    /* Inner class that defines the table contents of the messages table */
    public static final class MessageEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MESSAGES)
                .build();

        public static final String TABLE_NAME = "messages";

        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_ROOMID = "roomId";
        public static final String COLUMN_TIMESTAMP = "timestamp";

//        public static void setTableName(String tableName) {
//            TABLE_NAME = tableName;
//        }

//        public static void setContentUri(String friendUid){
//            CONTENT_URI = buildMessagesUriWithFriendUid(friendUid);
//        }

        public static Uri buildMessagesUriWithFriendUid(String friendUid){
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath("messages_" + friendUid.substring(0, friendUid.indexOf("@gmail.com")))
                    .build();
        }

    }
}
