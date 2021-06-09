package android.example.friendlychat.sync;

import android.content.ContentValues;
import android.content.Context;
import android.example.friendlychat.data.MessageContract;
import android.example.friendlychat.data.MessageContract.MessageEntry;
import android.example.friendlychat.data.MessageProvider;
import android.net.Uri;

import java.security.Provider;

public class MessagesSyncTask {

    /**
     * This function should be called from within the onEvent() function of the
     * SnapshotListener of the MessageRoomActivity. It synchronizes the corresponding
     * document of the "messages" collection with the local SQLite database.
     * @param context
     */
    synchronized public static void syncMessages(Context context, ContentValues values){

        //Uri friendTableUri = MessageEntry.buildMessagesUriWithFriendUid(friendUid);

        //MessageProvider provider = new MessageProvider(friendUid);
        context.getContentResolver().insert(MessageEntry.CONTENT_URI, values);

    }
}
