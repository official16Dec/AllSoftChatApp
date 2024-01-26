package com.allsoft.chatapp.utils.dbmanager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.allsoft.chatapp.model.user.EndUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RealDatabaseManager {


    private String TAG = RealDatabaseManager.class.getSimpleName();
    public final static String chatapp = "chatapp";
    private DatabaseReference databaseReference;
    public RealDatabaseManager(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(chatapp);
    }

    public void registerUser(EndUser endUser){

        databaseReference.setValue(endUser);
    }


    EndUser endUser = null;
    public EndUser getEndUserById(int userId){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                JSONObject jObj;
                //                    jObj = new JSONObject(dataSnapshot.toString());
                Log.d(TAG, "Value is: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return endUser;
    }
}
