package android.example.friendlychat.data;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MessageUtils {

    private static long maxTimestamp = 0;

    public static void setMaxTimeStamp(long timeStamp){
        maxTimestamp = Math.max(maxTimestamp, timeStamp) + 1;
    }

    public static long getMaxTimestamp() {
        return maxTimestamp;
    }

    public static Timestamp getFirebaseMaxTimestamp(){

        //long maxTimestampMillis = TimeUnit.NANOSECONDS.toMillis(maxTimestamp);
        Date date = new Date(maxTimestamp);
        Timestamp timestamp = new Timestamp(date);

        return timestamp;
    }
}
