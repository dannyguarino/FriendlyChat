package android.example.friendlychat;

public class Friend {

    private String mName;
    private String mEmailId;

    public Friend(String name, String emailId){
        mName = name;
        mEmailId = emailId;
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
}
