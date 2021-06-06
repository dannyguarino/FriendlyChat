package android.example.friendlychat;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DatabaseManager {

    public static FirebaseFirestore db;

    public static void initializeDb(){
        db = FirebaseFirestore.getInstance();
    }

    public static String getUidForEmail(String email){

        final String[] uid = new String[1];

        db.collection("users")
                .whereEqualTo("emailId", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                uid[0] = document.getId();

                                //Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            //Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return uid[0];
    }
}
