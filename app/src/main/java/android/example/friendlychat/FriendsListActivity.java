package android.example.friendlychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FriendsListActivity extends AppCompatActivity {

    private final String LOG_TAG = FriendsListActivity.class.getName();

    private ListView mFriendsListView;
    private FloatingActionButton mFab;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth.IdTokenListener mIdTokenListener;
    private ListenerRegistration mListenerRegistration;

    private FriendsAdapter mAdapter;
    private final String emailId1 = "rishitaburman7@gmail.com";
    private final String emailId2 = "reenaburman888@gmail.com";
    private final String emailId3 = "saritb734@gmail.com";
    private final String[] emails = new String[]{emailId1, emailId2, emailId3};

    private String[] friends = new String[]{};

    private static final int RC_SIGN_IN = 1;
    private static final int RC_ADD_FRIEND = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        // Initialize the ListView for the adapter
        mFriendsListView = findViewById(R.id.friendsListView);

        // Initialize the FAB
        mFab = findViewById(R.id.fab);

        // Initialize the adapter
        List<Friend> friends = new ArrayList<>();
        mAdapter = new FriendsAdapter(this, R.layout.item_friend, friends);
        mFriendsListView.setAdapter(mAdapter);

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

        mFriendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendsListActivity.this, MessageRoomActivity.class);
                // Pass in the information related to the friend which can help the MainActivity load the
                // correct chat room
                Friend friend = (Friend) parent.getAdapter().getItem(position);
                intent.putExtra("friendUid", friend.getEmailId());
                startActivity(intent);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addFriendIntent = new Intent(FriendsListActivity.this, AddFriendActivity.class);
                //startActivity(addFriendIntent);
                startActivityForResult(addFriendIntent, RC_ADD_FRIEND);
                //reloadAdapter();
            }
        });
    }

    private void attachDatabaseReadListener(){

        mListenerRegistration = DatabaseManager.db.collection("users").document(User.getEmailId())
                .collection("contacts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(LOG_TAG, "Listen failed.", e);
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:

                                    Friend friend = new Friend(dc.getDocument().getString("userName"),
                                            dc.getDocument().getString("emailId"));

                                    mAdapter.add(friend);
                                    break;
                                case MODIFIED:
                                    Log.d(LOG_TAG, "Modified city: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(LOG_TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });
    }

    private void detachDatabaseReadListener(){
        mListenerRegistration.remove();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed In!", Toast.LENGTH_SHORT).show();
                reload();
                attachDatabaseReadListener();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign In cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else if(requestCode == RC_ADD_FRIEND){

            if(resultCode == Activity.RESULT_OK) {
                reloadAdapter();
                Log.d(LOG_TAG, "reloadAdapter() after adding new friend");
                Toast.makeText(this, "reloadAdapter() after adding new friend", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(LOG_TAG, "adapter not reloaded");
                Toast.makeText(this, "adapter not reloaded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void reloadAdapter(){

        mAdapter.clear();

        // initialize the friends list (or array)
        DatabaseManager.db.collection("users").document(User.getEmailId())
                .collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){

                                Friend friend =
                                        new Friend(document.get("userName").toString(),
                                                document.get("emailId").toString());

                                mAdapter.add(friend);
                            }
                        }
                    }
                });
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
//        for(String email: emails){
//            Log.d(LOG_TAG, "email = " + email + ", User.getmEmailId() = " + User.getEmailId() + ", User.getmUsername = " + User.getUsername());
//            if(!email.equals(User.getEmailId())){
//
//                mAdapter.add(new Friend(email.substring(0, 5), email));
//            }
//        }

        //reloadAdapter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIdTokenListener != null) {
            mFirebaseAuth.removeIdTokenListener(mIdTokenListener);
        }
//        detachDatabaseReadListener();
//        mAdapter.clear();
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
