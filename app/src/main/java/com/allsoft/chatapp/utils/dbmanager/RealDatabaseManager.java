package com.allsoft.chatapp.utils.dbmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.allsoft.chatapp.model.chats.UserChat;
import com.allsoft.chatapp.model.user.EndUser;
import com.allsoft.chatapp.utils.preference.MySharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

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
                            EndUser user = new EndUser();
                            user.setUser_id(0);
                            userMap.put("user0", user);
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
                            groupMap.put("0V0", conversationMap);
                            userChatMap.put("user_chats", groupMap);
                            databaseReference.updateChildren(userChatMap);
                        }



                        databaseCallback.databaseLoadingCallback(allDataObj);

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

    public void getAllGroupData(String endusers){

        DatabaseReference convRef = FirebaseDatabase.getInstance().getReference().child("chat_app/user_chats/"+endusers);

        convRef.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{

                    ArrayList<UserChat> groupChatList = new ArrayList<>();

                    for(DataSnapshot childSnapShot : snapshot.getChildren()){
                        UserChat userChat = childSnapShot.getValue(UserChat.class);
                        groupChatList.add(userChat);
                    }

                    databaseCallback.getChatListCallback(groupChatList);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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
            DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference().child("chat_app/user_chats/"+endUsers);
            HashMap<String, Object> conversationMap = new HashMap<>();
            conversationMap.put(userChat.getWhen(), userChat);
            groupRef.updateChildren(conversationMap);
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
                String groupKey = groupKeys.next();
                JSONObject groupObj = userChats.getJSONObject(groupKey);
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

                lastGroupKey = groupKey;

                if(!lastConversationKey.equals("")){
                    break;
                }
            }



            Log.d("LastConKey", lastConversationKey);
            Log.d("groupKeys", lastGroupKey);

            if(lastConversationKey.equals("")){
                String newGroupKey = userChatList.get(0).getEndusers();

                DatabaseReference groupPref = FirebaseDatabase.getInstance().getReference().child("chat_app/user_chats/"+newGroupKey);

                for (UserChat userChat : userChatList) {
                    HashMap<String, Object> conversationMap = new HashMap<>();
                    conversationMap.put(userChat.getWhen(), userChat);
                    groupPref.updateChildren(conversationMap);
                }
            }
            else{
                DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference().child("chat_app/user_chats/"+lastGroupKey);

                for (UserChat userChat : userChatList) {
                    HashMap<String, Object> conversationMap = new HashMap<>();
                    conversationMap.put(userChat.getWhen(), userChat);
                    groupRef.updateChildren(conversationMap);
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

        void getChatListCallback(ArrayList<UserChat> groupChatList);
    }

    private DatabaseCallback databaseCallback;
}
