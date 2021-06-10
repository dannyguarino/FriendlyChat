package android.example.friendlychat.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MessageUtils {

    private static final String LOG_TAG = MessageUtils.class.getSimpleName();

    private static long maxTimestamp = 0;

    public static void setMaxTimeStamp(long timeStamp){
        maxTimestamp = Math.max(maxTimestamp, timeStamp) + 1;
    }

    public static long getMaxTimestamp() {
        return maxTimestamp;
    }

    public static Timestamp getFirebaseMaxTimestamp(){

        Log.i(LOG_TAG, "maxTimestamp = " + maxTimestamp);

        Date date = new Date(maxTimestamp);
        Timestamp timestamp = new Timestamp(date);

        return timestamp;
    }

    public static void storeMaxtimestampInSharedPreferences(Context context){

        Activity activity = (Activity) context;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        Log.i(LOG_TAG, "maxTimestamp stored in SharedPreferenes: " + maxTimestamp);
        editor.putLong("maxTimestamp", maxTimestamp);
        editor.apply();
    }

    public static void initializeMaxTimestampFromSharedPreferences(Context context){

        Activity activity = (Activity) context;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        maxTimestamp = sharedPref.getLong("maxTimestamp", 0);
        Log.i(LOG_TAG, "maxTimestamp initialized from SharedPreferenes: " + maxTimestamp);
    }
}
