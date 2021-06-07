package android.example.friendlychat;

public class User {

    private static final String ANONYMOUS = "anonymous";
    private static String mUsername = ANONYMOUS, mEmailId;
    private static String mMyUid;

    public static String getEmailId() {
        return mEmailId;
    }

    public static String getUsername() { return mUsername; }

    public static String getMyUid() { return mMyUid; }

    public static void setEmailId(String mEmailId) {
        User.mEmailId = mEmailId;
    }

    public static void setUsername(String mUsername) {
        User.mUsername = mUsername;
    }

    public static void setMyUid(String mMyUid) {
        User.mMyUid = mMyUid;
    }
}
