package android.example.friendlychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsListActivity extends AppCompatActivity {

    private final String LOG_TAG = FriendsListActivity.class.getName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth.IdTokenListener mIdTokenListener;

    private TextView contact1TextView, contact2TextView;
    private final String emailId1 = "rishitaburman7@gmail.com";
    private final String emailId2 = "reenaburman888@gmail.com";
    private final String emailId3 = "saritb734@gmail.com";
    private final String[] emails = new String[]{emailId1, emailId2, emailId3};

    private String[] friends = new String[]{};

    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        // Initialize the View components
        contact1TextView = findViewById(R.id.tv_contact1);
        contact2TextView = findViewById(R.id.tv_contact2);

        // Initialize the firebase components
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

        contact1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsListActivity.this, MessageRoomActivity.class);
                // Pass in the information related to the friend which can help the MainActivity load the
                // correct chat room
                //String uid = DatabaseManager.getUidForEmail(contact1TextView.getText().toString());
                intent.putExtra("friendUid", contact1TextView.getText().toString());
                startActivity(intent);
            }
        });

        contact2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsListActivity.this, MessageRoomActivity.class);
                // Pass in the information related to the friend which can help the MainActivity load the
                // correct chat room
                //String uid = DatabaseManager.getUidForEmail(contact2TextView.getText().toString());
                intent.putExtra("friendUid", contact2TextView.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed In!", Toast.LENGTH_SHORT).show();
                reload();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign In cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void reload(){
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        Map<String, Object> userInfo = new HashMap<>();

        // Putting the "userInfo" into the "users" collection
        userInfo.put("userName", user.getDisplayName());
        userInfo.put("emailId", user.getEmail());
        findExistingOrAddNewUser(userInfo);

        // Set the memeber variables in the "User" class
        User.setEmailId(user.getEmail());
        User.setUsername(user.getDisplayName());
        Log.d(LOG_TAG, "User.getEmailId = " + User.getEmailId());

        // Show a toast message when the user Authentication is complete
        Toast.makeText(FriendsListActivity.this,
                "You're now signed in as " + User.getEmailId() + ". Welcome to FriendlyChat!", Toast.LENGTH_SHORT).show();

        // initialize the friends list (or array)
        int m = 1;
        for(String email: emails){
            Log.d(LOG_TAG, "email = " + email + ", User.getmEmailId() = " + User.getEmailId() + ", User.getmUsername = " + User.getUsername());
            if(!email.equals(User.getEmailId())){
                if(m == 1){
                    contact1TextView.setText(email);
                    m++;
                }
                else if(m == 2){
                    contact2TextView.setText(email);
                    m++;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIdTokenListener != null) {
            mFirebaseAuth.removeIdTokenListener(mIdTokenListener);
        }
        //detachDatabaseReadListener();
        //mMessageAdapter.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        AuthUI.getInstance()
//                .signOut(this);
    }

    protected void findExistingOrAddNewUser(Map<String, Object> user) {

        DatabaseManager.initializeDb();

        // Adding the "user" document to the "users" collection. If it already exists,
        // it will be merged with the new data
        DatabaseManager.db.collection("users").document(user.get("emailId").toString())
                .set(user, SetOptions.merge());

        DatabaseManager.db.collection("users")
                .whereEqualTo("emailId", user.get("emailId"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //if (user.get("emailId") == document.getData().get("emailId")) {
                                User.setMyUid(document.getId());
                                //}

                                Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(LOG_TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void onSignedOutCleanup() {
        //User.setmUsername(User.ANONYMOUS);
        //mMessageAdapter.clear();
    }
}
