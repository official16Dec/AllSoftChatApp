package com.allsoft.chatapp.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.allsoft.chatapp.R;
import com.allsoft.chatapp.databinding.ActivityMainBinding;
import com.allsoft.chatapp.model.chats.UserChat;
import com.allsoft.chatapp.model.user.EndUser;
import com.allsoft.chatapp.ui.auth.LoginView;
import com.allsoft.chatapp.ui.dashboard.chatDetail.ChatDetailFragment;
import com.allsoft.chatapp.ui.dashboard.chatGroup.ChatGroupFragment;
import com.allsoft.chatapp.ui.dashboard.creategroup.CreateGroupFragment;
import com.allsoft.chatapp.ui.dashboard.viewmodel.MainViewModel;
import com.allsoft.chatapp.utils.dbmanager.RealDatabaseManager;
import com.allsoft.chatapp.utils.preference.MySharedPref;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainView extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String TAG = MainView.class.getSimpleName();

    private MainViewModel mainViewModel;

    private MySharedPref mySharedPref;

    private RealDatabaseManager realDatabaseManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityMainBinding.inflate(getLayoutInflater());

        mySharedPref = new MySharedPref(this);

        setContentView(binding.getRoot());

        initViewModel();

        setObserver();

        refreshGroups();

        mainViewModel.setChatGroupLiveData(new HashMap<>());

        setListener();

    }

    private void setListener() {
        binding.backBtn.setOnClickListener(view -> onBackPressed());

        binding.logoutBtn.setOnClickListener(view -> {

            mySharedPref.clearData();

            startActivity(new Intent(this, LoginView.class));
            finish();

        });

        binding.composeNewBtn.setOnClickListener(view -> mainViewModel.setCreateGroupLiveData(new HashMap<>()));
    }


    private void refreshGroups() {
        realDatabaseManager = new RealDatabaseManager(this, result -> manageChatHistory(result));

    }

    private void manageChatHistory(JSONObject result) {
        try {
            JSONObject chatData = result.getJSONObject("user_chats");
            ArrayList<UserChat> userChatList = new ArrayList<>();

            Iterator<String> groupKeys = chatData.keys();
            while(groupKeys.hasNext()){
                String groupKey = groupKeys.next();
                JSONObject groupData = chatData.getJSONObject(groupKey);

                Iterator<String> conversationKeys = groupData.keys();
                while(conversationKeys.hasNext()){
                    String conversationKey = conversationKeys.next();
                    JSONObject conversationData = groupData.getJSONObject(conversationKey);

                    if(conversationData.getInt("sender") == mySharedPref.getPrefUserId(MySharedPref.prefUserId)){

                        Gson gson = new Gson();
                        UserChat userChat = gson.fromJson(conversationData.toString(), UserChat.class);
                        userChatList.add(userChat);
                    }
                }
            }

            Log.d(TAG, "Size is "+userChatList.size());
            HashMap<String, ArrayList<UserChat>> chatHistoryData = new HashMap<>();
            chatHistoryData.put("chatList", userChatList);
            mainViewModel.setChatGroupAdapterLiveData(chatHistoryData);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setObserver() {
        mainViewModel.getChatGroupLiveData().observe(this, stringObjectHashMap -> {
            ChatGroupFragment chatGroupFragment = ChatGroupFragment.newInstance("", "");
            loadFragment(chatGroupFragment, ChatGroupFragment.class.getSimpleName());
        });

        mainViewModel.getChatDetailLiveData().observe(this, mapData -> {
            if(mapData.containsKey("endusers")){
                getChatGroupTitle(String.valueOf(mapData.get("endusers")));

                new Handler().postDelayed(() -> {
                    ChatDetailFragment chatDetailFragment = ChatDetailFragment.newInstance(String.valueOf(mapData.get("endusers")),
                            binding.mainBarTitle.getText().toString());
                    loadFragment(chatDetailFragment, ChatDetailFragment.class.getSimpleName());
                }, 600);

            }

        });

        mainViewModel.getRefreshChatHistoryLiveData().observe(this, stringObjectHashMap -> refreshGroups());

        mainViewModel.getGroupChatLiveData().observe(this, mapData -> {
            if(mapData.containsKey("endusers")){
                getGroupConversation(String.valueOf(mapData.get("endusers")));
            }
        });

        mainViewModel.getUpdateGroupChatLiveData().observe(this, mapData -> {
            if(mapData.containsKey("user_chat")){
                updateGroupChat((UserChat) mapData.get("user_chat"), String.valueOf(mapData.get("endusers")));
            }
        });


        mainViewModel.getCreateGroupLiveData().observe(this, mapData->{
            binding.mainBarTitle.setText(getString(R.string.send_message_to));
            binding.composeNewBtn.setVisibility(View.GONE);

            CreateGroupFragment createGroupFragment = CreateGroupFragment.newInstance("", "");
            loadFragment(createGroupFragment, CreateGroupFragment.class.getSimpleName());

            HashMap<String, ArrayList<EndUser>> userListMap = new HashMap<>();

            try{
                JSONObject userData = realDatabaseManager.getAllUserData();
                Iterator<String> userKeys = userData.keys();

                ArrayList<EndUser> userList = new ArrayList<>();
                while(userKeys.hasNext()){
                    String userKey = userKeys.next();
                    JSONObject userObj = userData.getJSONObject(userKey);

                    Gson gson = new Gson();
                    EndUser endUser = gson.fromJson(userObj.toString(), EndUser.class);
                    if(endUser.getUser_id() != mySharedPref.getPrefUserId(MySharedPref.prefUserId)){
                        userList.add(endUser);
                    }
                }
                userListMap.put("userList", userList);
                new Handler().postDelayed(() -> mainViewModel.setUserListLiveData(userListMap), 600);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });

        mainViewModel.getUserGroupLiveData().observe(this, groupMap -> {
            if(groupMap.containsKey("usergroup")){
                realDatabaseManager.createGroupWithData(groupMap.get("usergroup"));
            }
        });

    }

    private void getGroupConversation(String endusers) {

        JSONObject result = realDatabaseManager.getAllData();

        try {
            JSONObject chatData = result.getJSONObject("user_chats");
            ArrayList<UserChat> userChatList = new ArrayList<>();

            Iterator<String> keys = chatData.keys();
            while(keys.hasNext()){
                String key = keys.next();
                JSONObject groupData = chatData.getJSONObject(key);

                Iterator<String> groupKeys = groupData.keys();
                while(groupKeys.hasNext()){
                    String groupKey = groupKeys.next();
                    JSONObject conversationData = groupData.getJSONObject(groupKey);

                    if(conversationData.getString("endusers").equals(endusers)){
                        Gson gson = new Gson();
                        UserChat userChat = gson.fromJson(conversationData.toString(), UserChat.class);
                        userChatList.add(userChat);
                    }
                }
            }

            Log.d(TAG, "Size is "+userChatList.size());
            HashMap<String, ArrayList<UserChat>> chatHistoryData = new HashMap<>();
            chatHistoryData.put("chatList", userChatList);
            mainViewModel.setChatDetailAdapterLiveData(chatHistoryData);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void loadFragment(Fragment fragment, String fragmentKey){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(fragmentKey);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        super.onBackPressed();
    }


    private void getChatGroupTitle(String endusers){
        String[] users = endusers.split("V");

        String chatTitle = "";

        try{
            for(String user : users) {
                EndUser endUser = realDatabaseManager.getEndUserById(Integer.parseInt(user));
                if (endUser.getUser_id() != mySharedPref.getPrefUserId(MySharedPref.prefUserId)) {
                    if(chatTitle.isEmpty()){
                        chatTitle = "to "+endUser.getUser_name();
                    }
                    else{
                        chatTitle = chatTitle+", "+endUser.getUser_name();
                    }
                }
            }
            binding.mainBarTitle.setText(chatTitle);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateGroupChat(UserChat userChat, String endusers){
        realDatabaseManager.updateGroupChat(userChat, endusers);
    }
}
