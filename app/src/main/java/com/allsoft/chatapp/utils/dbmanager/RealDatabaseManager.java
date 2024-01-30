package com.allsoft.chatapp.utils.dbmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.allsoft.chatapp.model.chats.UserChat;
import com.allsoft.chatapp.model.user.EndUser;
import com.allsoft.chatapp.utils.preference.MySharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class RealDatabaseManager {


    private String TAG = RealDatabaseManager.class.getSimpleName();
    public final static String chatapp = "chat_app";
    private JSONObject allDataObj;
    private final DatabaseReference databaseReference;

    private final MySharedPref mySharedPref;

    public RealDatabaseManager(Context context, DatabaseCallback databasCallback){
        mySharedPref = new MySharedPref(context);

        databaseCallback = databasCallback;

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

                        if(!allDataObj.has("endusers")) {
                            HashMap<String, Object> enduserMap = new HashMap<>();
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("user1", new EndUser());
                            enduserMap.put("endusers", userMap);
                            databaseReference.updateChildren(enduserMap);
                        }

                        if(!allDataObj.has("user_chats")) {
                            HashMap<String, Object> userChatMap = new HashMap<>();
                            HashMap<String, Object> groupMap = new HashMap<>();
                            HashMap<String, Object> conversationMap = new HashMap<>();
                            UserChat userChat = new UserChat();
                            userChat.setEndusers("");
                            userChat.setSender(0);
                            conversationMap.put("conversation1", userChat);
                            groupMap.put("group0", conversationMap);
                            userChatMap.put("user_chats", groupMap);
                            databaseReference.updateChildren(userChatMap);
                        }



                        databaseCallback.databaseLoadingCallback(allDataObj);

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

    public void registerUser(EndUser endUser, String userKey){

        if(endUser.getUser_id() == 1){
            Map<String, Object> mapData = new HashMap<>();
            Map<String, Object> userData = new HashMap<>();
            userData.put(userKey, endUser);
            mapData.put("endusers", userData);
            databaseReference.updateChildren(mapData);
        }
        else{
            DatabaseReference userRef = databaseReference.child("endusers");

            Map<String, Object> userData = new HashMap<>();
            userData.put(userKey, endUser);

            userRef.updateChildren(userData);
        }
    }

    public EndUser getEndUserById(int userId){

        EndUser endUser = null;
        String userKey = "user"+userId;
        if(getAllUserData() != null) {
            try {
                JSONObject jsonObject = getAllUserData().getJSONObject(userKey);
                Gson gson = new Gson();
                endUser = gson.fromJson(jsonObject.toString(), EndUser.class);

                Log.d(TAG, "Value is " + endUser.getUser_id());

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
                    if(Objects.equals(endUser.getUser_mobile(), mobileNo) && Objects.equals(endUser.getUser_password(), pass)){
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
        mySharedPref.setPrefUserId(MySharedPref.prefUserId, endUser.getUser_id());
        mySharedPref.setPrefUserName(MySharedPref.prefUserName, endUser.getUser_name());
        mySharedPref.setPrefUserMobile(MySharedPref.prefUserMobile, endUser.getUser_mobile());
        mySharedPref.setPrefUserMobile(MySharedPref.prefUserMail, endUser.getUser_mail());
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

    public void updateGroupChat(UserChat userChat, String endUsers){
        try{
            try {
                DatabaseReference userChatRef = databaseReference.child("user_chats");
                JSONObject userChatObj = getAllData().getJSONObject("user_chats");
                Iterator<String> groupKeys = userChatObj.keys();

                String lastGroupKey = "";
                String lastConversationKey = "";

                while(groupKeys.hasNext()){
                    String groupKey = groupKeys.next();
                    JSONObject conversationObj = userChatObj.getJSONObject(groupKey);
                    Iterator<String> conversationKeys = conversationObj.keys();

                    while(conversationKeys.hasNext()){
                        String conversationKey = conversationKeys.next();
                        JSONObject chatObj = conversationObj.getJSONObject(conversationKey);

                        if(chatObj.getString("endusers").equals(endUsers)){
                            lastConversationKey = conversationKey;
                        }
                    }
                    lastGroupKey = groupKey;
                    if(!lastConversationKey.equals("")){
                        break;
                    }
                }

                HashMap<String, Object> conversationMap = new HashMap<>();
                DatabaseReference groupRef = userChatRef.child(lastGroupKey);
                int newKey = Integer.parseInt(lastConversationKey.replaceAll("conversation", "")) + 1;
                conversationMap.put("conversation" + newKey, userChat);
                groupRef.updateChildren(conversationMap);



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createGroupWithData(ArrayList<UserChat> userChatList){
        try{
            JSONObject userChats = allDataObj.getJSONObject("user_chats");
            Iterator<String> groupKeys = userChats.keys();

            String lastGroupKey = "";
            String lastConversationKey = "";
            while (groupKeys.hasNext()) {
                lastGroupKey = groupKeys.next();
                JSONObject groupObj = userChats.getJSONObject(lastGroupKey);
                Iterator<String> conversationKeys = groupObj.keys();
                while(conversationKeys.hasNext()){
                    String conversationKey = conversationKeys.next();
                    JSONObject conversationObj = groupObj.getJSONObject(conversationKey);
                    if(conversationObj.has("endusers")){
                        if(conversationObj.getString("endusers").equals(userChatList.get(0).getEndusers())){
                            lastConversationKey = conversationKey;
                        }
                    }
                }
            }

            if(lastConversationKey.equals("")){
                HashMap<String, Object> groupMap = new HashMap<>();
                int newGroupKeyId = Integer.parseInt(lastGroupKey.replace("group", "")) + 1;
                String newGroupKey = "group" + newGroupKeyId;
                int conversationKeyId = 1;

                DatabaseReference chatDataRef = databaseReference.child("user_chats");

                for (UserChat userChat : userChatList) {
                    HashMap<String, Object> conversationMap = new HashMap<>();
                    conversationMap.put("conversation" + conversationKeyId, userChat);
                    groupMap.put(newGroupKey, conversationMap);
                    if(conversationKeyId == 1){
                        chatDataRef.updateChildren(groupMap);
                    }
                    else{
                        DatabaseReference groupPref = chatDataRef.child(newGroupKey);
                        groupPref.updateChildren(conversationMap);
                    }

                    conversationKeyId++;
                }
            }
            else{
                DatabaseReference chatDataRef = databaseReference.child("user_chats");
                DatabaseReference groupRef = chatDataRef.child(lastGroupKey);
                int conversationKeyId = 1;


                for (UserChat userChat : userChatList) {
                    HashMap<String, Object> conversationMap = new HashMap<>();
                    conversationMap.put("conversation" + conversationKeyId, userChat);
                    groupRef.updateChildren(conversationMap);

                    conversationKeyId++;
                }
            }


            databaseCallback.groupDetailCallBack(userChatList.get(0).getEndusers());

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public interface DatabaseCallback{
        void databaseLoadingCallback(JSONObject result);

        void groupDetailCallBack(String endusers);
    }

    private DatabaseCallback databaseCallback;
//    public void setDatabaseCallback(DatabaseCallback callback){
//        databaseCallback = callback;
//    }
}
