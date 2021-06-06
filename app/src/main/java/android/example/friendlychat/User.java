package android.example.friendlychat;

public class User {

    private static final String ANONYMOUS = "anonymous";
    private static String mUsername = ANONYMOUS, mEmailId;
    private static String mMyUid;

    public static String getmEmailId() {
        return mEmailId;
    }

    public static String getmUsername() {
        return mUsername;
    }

    public static String getmMyUid() {
        return mMyUid;
    }

    public static void setmEmailId(String mEmailId) {
        User.mEmailId = mEmailId;
    }

    public static void setmUsername(String mUsername) {
        User.mUsername = mUsername;
    }

    public static void setmMyUid(String mMyUid) {
        User.mMyUid = mMyUid;
    }
}
