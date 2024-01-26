package com.allsoft.chatapp.utils.dbmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.allsoft.chatapp.model.user.EndUser;
import com.allsoft.chatapp.utils.preference.MySharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

public class RealDatabaseManager {


    private String TAG = RealDatabaseManager.class.getSimpleName();
    public final static String chatapp = "chat_app";
    private JSONObject allDataObj;
    private final DatabaseReference databaseReference;

    private final MySharedPref mySharedPref;

    public RealDatabaseManager(Context context){
        mySharedPref = new MySharedPref(context);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(chatapp);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getValue() != null) {
                    try {
                        Object object = dataSnapshot.getValue(Object.class);
                        String json = new Gson().toJson(object);

                        allDataObj = new JSONObject(json);

                        databaseCallback.databaseLoadingCallback();

//                        Log.d(TAG, "All Value"+allDataObj.getJSONObject("endusers"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void registerUser(EndUser endUser){

        databaseReference.setValue(endUser);
    }

    public EndUser getEndUserById(int userId){

        EndUser endUser = null;
        String userKey = "user"+userId;
        if(getAllUserData() != null) {
            try {
                JSONObject jsonObject = getAllUserData().getJSONObject(userKey);
                Gson gson = new Gson();
                endUser = gson.fromJson(jsonObject.toString(), EndUser.class);

                Log.d(TAG, "Value is " + endUser.getUserId());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return endUser;
    }

    public boolean loginUser(String mobileNo, String pass) {

        if(getAllUserData() != null) {
            try {
                JSONObject jsonObject = getAllUserData();
                Iterator<String> keys = jsonObject.keys();
                while(keys.hasNext()){
                    String key = keys.next();
                    JSONObject endUserObj = jsonObject.getJSONObject(key);
                    Gson gson = new Gson();
                    EndUser endUser = gson.fromJson(endUserObj.toString(), EndUser.class);
                    if(Objects.equals(endUser.getUserMobile(), mobileNo) && Objects.equals(endUser.getUserPassword(), pass)){
                        setUserPreference(endUser);
                        return true;
                    }
                }

//                Log.d(TAG, "Value is " + endUser.getUserId());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void setUserPreference(EndUser endUser) {
        mySharedPref.setPrefUserId(MySharedPref.prefUserId, endUser.getUserId());
        mySharedPref.setPrefUserName(MySharedPref.prefUserName, endUser.getUserName());
        mySharedPref.setPrefUserMobile(MySharedPref.prefUserMobile, endUser.getUserMobile());
        mySharedPref.setPrefUserMobile(MySharedPref.prefUserMail, endUser.getUserMail());
    }

    public JSONObject getAllData(){

        return allDataObj;
    }

    public JSONObject getAllUserData() {
        JSONObject userData = null;
        try {
            Log.d(TAG, "All User");

            userData = getAllData().getJSONObject("endusers");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return userData;

    }

    public interface DatabaseCallback{
        void databaseLoadingCallback();
    }

    private DatabaseCallback databaseCallback;
    public void setDatabaseCallback(DatabaseCallback callback){
        databaseCallback = callback;
    }
}
