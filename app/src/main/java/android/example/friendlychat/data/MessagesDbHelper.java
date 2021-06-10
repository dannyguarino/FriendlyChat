package android.example.friendlychat.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import android.example.friendlychat.data.MessageContract.MessageEntry;
import android.os.Message;
import android.util.Log;

public class MessagesDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = MessagesDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "messsage_store.db";
    public static final int DATABASE_VERSION = 1;

    public MessagesDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MESSAGES_TABLE =
                "CREATE TABLE " + MessageEntry.TABLE_NAME + " (" +
                        MessageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MessageEntry.COLUMN_TEXT + " TEXT, " +
                        MessageEntry.COLUMN_AUTHOR + " TEXT, " +
                        MessageEntry.COLUMN_READER + " TEXT, " +
                        MessageEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL UNIQUE);";

        db.execSQL(SQL_CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(LOG_TAG, "onUpgrade called: oldVersion = " + oldVersion + ", new_version = " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + MessageEntry.TABLE_NAME);
        onCreate(db);
    }
}
