package android.example.friendlychat;

public class Friend {

    private String mName;
    private String mEmailId;
    private String mFriendPublicKey;

    public Friend(String name, String emailId){
        mName = name;
        mEmailId = emailId;
    }

    public Friend(String name, String emailId, String friendPublicKey){
        mName = name;
        mEmailId = emailId;
        mFriendPublicKey = friendPublicKey;
    }

    public void setEmailId(String mEmailId) {
        this.mEmailId = mEmailId;
    }

    public String getEmailId() {
        return mEmailId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public String getFriendPublicKey() {
        return mFriendPublicKey;
    }

    public void setFriendPublicKey(String mFriendPublicKey) {
        this.mFriendPublicKey = mFriendPublicKey;
    }
}
