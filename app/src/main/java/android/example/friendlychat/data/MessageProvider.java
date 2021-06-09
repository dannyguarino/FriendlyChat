package android.example.friendlychat.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MessageProvider extends ContentProvider {

    private final String LOG_TAG = MessageProvider.class.getSimpleName();

    private MessagesDbHelper mOpenHelper;

//    private String mFriendUid;
//    private String TABLE_NAME;

//    public MessageProvider(String friendUid) {
//        super();
//        mFriendUid = friendUid;
//        TABLE_NAME = "messages_" + mFriendUid;
//    }

    @Override
    public boolean onCreate() {
        MessageContract.incrementDatabaseVersion();
        mOpenHelper = new MessagesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        //db.setVersion(db.getVersion() + 1);

        cursor = db.query(
                MessageContract.MessageEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        long newRowId = db.insert(MessageContract.MessageEntry.TABLE_NAME, null, values);

        if(newRowId == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the get content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, newRowId);
    }

    /* We are not implementing deleting messages option yet */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    /* We are not implementing the option to edit messages once sent yet */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
