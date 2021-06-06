package android.example.friendlychat;

import androidx.annotation.NonNull;
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

import android.example.friendlychat.DatabaseManager;

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

        // Launch the authentication screen (Firebase UI) and fill up the Map of userInfo
        final Map<String, Object> userInfo = new HashMap<>();
        mIdTokenListener = new FirebaseAuth.IdTokenListener() {
            @Override
            public void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    userInfo.put("userName", user.getDisplayName());
                    userInfo.put("emailId", user.getEmail());
                    findExistingOrAddNewUser(userInfo);
                    User.setmEmailId(user.getEmail());
                    Log.d(LOG_TAG, "User.getmEmailId = " + User.getmEmailId());
                    User.setmUsername(user.getDisplayName());
                    //attachDatabaseReadListener();
                    Toast.makeText(FriendsListActivity.this,
                            "You're now signed in. Welcome to FriendlyChat!", Toast.LENGTH_SHORT).show();
                } else {
                    // User is not signed in yet
                    //onSignedOutCleanup();
                    // Choose authentication providers
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            //new AuthUI.IdpConfig.PhoneBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());

                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                if (user != null) {
//                    userInfo.put("userName", user.getDisplayName());
//                    userInfo.put("emailId", user.getEmail());
//                    findExistingOrAddNewUser(userInfo);
//                    User.setmEmailId(user.getEmail());
//                    Log.d(LOG_TAG, "User.getmEmailId = " + User.getmEmailId());
//                    User.setmUsername(user.getDisplayName());
//                    //attachDatabaseReadListener();
//                    Toast.makeText(FriendsListActivity.this,
//                            "You're now signed in. Welcome to FriendlyChat!", Toast.LENGTH_SHORT).show();
//                } else {
//                    // User is not signed in yet
//                    //onSignedOutCleanup();
//                    // Choose authentication providers
//                    List<AuthUI.IdpConfig> providers = Arrays.asList(
//                            new AuthUI.IdpConfig.EmailBuilder().build(),
//                            //new AuthUI.IdpConfig.PhoneBuilder().build(),
//                            new AuthUI.IdpConfig.GoogleBuilder().build());
//
//                    // Create and launch sign-in intent
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setIsSmartLockEnabled(false)
//                                    .setAvailableProviders(providers)
//                                    .build(),
//                            RC_SIGN_IN);
//                }
//            }
//        };

        mFirebaseAuth.addIdTokenListener(mIdTokenListener);

        // initialize the friends list (or array)
        int m = 1;
        for(String email: emails){
            Log.d(LOG_TAG, "email = " + email + ", User.getmEmailId() = " + User.getmEmailId() + ", User.getmUsername = " + User.getmUsername());
            if(!email.equals(User.getmEmailId())){
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

    public void updateUserInfo(){
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        User.setmEmailId(user.getEmail());
        Log.d(LOG_TAG, "User.getmEmailId = " + User.getmEmailId());
        User.setmUsername(user.getDisplayName());
    }

//    private static void onSignedInInitialize(String displayName, String email) {
//        mUsername = displayName;
//        mEmailId = email;
//    }

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

//        // Update the userInfo
//        updateUserInfo();
        //mFirebaseAuth.addAuthStateListener(mAuthStateListener);



        super.onResume();
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
                                User.setmMyUid(document.getId());
                                //}

                                Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(LOG_TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
//
//        if (myOwnUid[0] == null) {
//            db.collection("users")
//                    .add(user)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d(LOG_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(LOG_TAG, "Error adding document", e);
//                        }
//                    });
//        }
    }

    private void onSignedOutCleanup() {
        //User.setmUsername(User.ANONYMOUS);
        //mMessageAdapter.clear();
    }
}
