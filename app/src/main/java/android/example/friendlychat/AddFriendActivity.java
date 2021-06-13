package android.example.friendlychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.Activity;
import android.content.Intent;
import android.example.friendlychat.cryptography.RSA;
import android.example.friendlychat.data.MessageContract;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AddFriendActivity extends AppCompatActivity {

    private final String LOG_TAG = AddFriendActivity.class.getName();

    private EditText mNameEditText, mEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        // set the title of the Activity

        // enable the back button on the Action Bar
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the EditText fields
        mNameEditText = findViewById(R.id.et_friends_name);
        mEmailEditText = findViewById(R.id.et_friend_email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from the res/menu/menu_editor.xml file
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_save:
                saveFriend();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                //MessageContract.incrementDatabaseVersion();
                finish();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(AddFriendActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFriend(){

        final String email = mEmailEditText.getText().toString().trim();

        DatabaseManager.db.collection("users").document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String name = "";
                            String friendPublicKey = "";
                            if (document.exists()) {
                                name = document.get("userName").toString();
                                friendPublicKey = document.get("userPublicKey").toString();
                                //RSA.setFriendPublicKey(document.get("userPublicKey").toString());
                                Log.d(LOG_TAG, "document.get(\"userName\").toString() = " + document.get("userName").toString());
                            }
                            else{
                                Toast.makeText(AddFriendActivity.this,
                                        "Couldn't add friend!\nYour friend is not yet registered on Friendly Chat!", Toast.LENGTH_SHORT).show();
                                finish();
                                name = mNameEditText.getText().toString().trim();
                                Log.d(LOG_TAG, "mNameEditText.getText().toString().trim() = " + mNameEditText.getText().toString().trim());
                            }

                            Map<String, Object> friendMap = new HashMap<>();
                            friendMap.put("userName", name);
                            friendMap.put("emailId", email);
                            friendMap.put("userPublicKey", friendPublicKey);

                            DatabaseManager.db.collection("users").document(User.getEmailId())
                                    .collection("contacts").document(email)
                                    .set(friendMap, SetOptions.merge());

                        } else {
                            Log.d(LOG_TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }
}
