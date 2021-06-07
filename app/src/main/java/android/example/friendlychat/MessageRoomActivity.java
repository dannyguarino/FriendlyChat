package android.example.friendlychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRoomActivity extends AppCompatActivity {

    private static final String LOG_TAG = MessageRoomActivity.class.getName();

    private static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int RC_SIGN_IN = 1;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mFriendUid;

    private CollectionReference mMsgCollectionRef;
    private ListenerRegistration mListenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_room);

        // Extract the friend Uid from the Intent Extras
        mFriendUid = getIntent().getStringExtra("friendUid");
        Log.d(LOG_TAG, "mFriendUid = " + mFriendUid);

        // Initialize references to views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> msg = new HashMap<>();
                msg.put("text", mMessageEditText.getText().toString());
                msg.put("author", User.getUsername());
                msg.put("timestamp", new Timestamp(new Date()));

                // create a new "message" document in the "messages" collection
                mMsgCollectionRef.document().set(msg);

                // Clear input box
                mMessageEditText.setText("");
            }
        });
    }

    private void attachDatabaseReadListener() {

        Map<String, Object> room = new HashMap<>();
        //room.put("usersInRoom", new String[]{User.getMyUid(), mFriendUid});
        room.put(User.getMyUid(), true);
        room.put(mFriendUid, true);
        //room.put("messages", null);

        String roomId = getMessageRoomId(User.getMyUid(), mFriendUid);
        DatabaseManager.db.collection("rooms").document(roomId)
                .set(room, SetOptions.merge());

        Log.d(LOG_TAG, "RoomId = " + roomId);

        mMsgCollectionRef = DatabaseManager.db.collection("rooms").document(roomId)
                .collection("messages");

        DatabaseManager.db.collection("rooms")
                .whereEqualTo(User.getMyUid(), true)
                .whereEqualTo(mFriendUid, true)
//                .whereArrayContains("usersInRoom", User.getMyUid())
//                .whereArrayContains("usersInRoom", mFriendUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                mMsgCollectionRef = document.getReference()
                                        .collection("messages");

                                Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            }

                        } else {
                            Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        mListenerRegistration = mMsgCollectionRef.orderBy("timestamp").
                addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(LOG_TAG, "Listen failed.", e);
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            FriendlyMessage friendlyMessage =
                                new FriendlyMessage(dc.getDocument().getString("text"),
                                        dc.getDocument().getString("author"), null);
                            mMessageAdapter.add(friendlyMessage);
                            //Log.d(LOG_TAG, "New city: " + dc.getDocument().getData());
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
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        mMessageAdapter.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }


    public static String getMessageRoomId(String id1, String id2){
        int res = id1.compareTo(id2);
        if(res < 0){
            return id1 + id2;
        }
        else{
            return id2 + id1;
        }
    }
}
