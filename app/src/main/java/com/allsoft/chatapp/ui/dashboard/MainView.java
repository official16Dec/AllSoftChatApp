package com.allsoft.chatapp.ui.dashboard;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
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
import com.allsoft.chatapp.ui.dashboard.chatDetail.ChatDetailFragment;
import com.allsoft.chatapp.ui.dashboard.chatGroup.ChatGroupFragment;
import com.allsoft.chatapp.ui.dashboard.viewmodel.MainViewModel;
import com.allsoft.chatapp.utils.dbmanager.RealDatabaseManager;
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

public class MainView extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String TAG = MainView.class.getSimpleName();

    private MainViewModel mainViewModel;

    private MySharedPref mySharedPref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityMainBinding.inflate(getLayoutInflater());

        mySharedPref = new MySharedPref(this);

        setContentView(binding.getRoot());

        initViewModel();

        setObserver();

        refreshChat();

        mainViewModel.setChatGroupLiveData(new HashMap<>());

    }



    private void refreshChat() {
        RealDatabaseManager realDatabaseManager = new RealDatabaseManager(this);
        realDatabaseManager.setDatabaseCallback(result -> {
            manageChatHistory(result);
        });
    }

    private void manageChatHistory(JSONObject result) {
        try {
            JSONObject chatData = result.getJSONObject("user_chats");
            ArrayList<UserChat> userChatList = new ArrayList<>();

            Iterator<String> keys = chatData.keys();
            while(keys.hasNext()){
                String key = keys.next();
                JSONObject chatObj = chatData.getJSONObject(key);
                String endUsers = chatObj.getString("end_users");
                String[] splitUser = endUsers.split("V");
                if(splitUser.length > 0){
                    if(Integer.parseInt(splitUser[0]) == mySharedPref.getPrefUserId(MySharedPref.prefUserId) || Integer.parseInt(splitUser[1]) == mySharedPref.getPrefUserId(MySharedPref.prefUserId)){
                        Gson gson = new Gson();
                        UserChat userChat = gson.fromJson(chatObj.toString(), UserChat.class);
                        userChatList.add(userChat);
                    }
                }
            }

            HashMap<String, Object> chatHistoryData = new HashMap<>();
            chatHistoryData.put("chatList", userChatList);
            mainViewModel.setChatAdapterLiveData(chatHistoryData);

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

        mainViewModel.getChatDetailLiveData().observe(this, stringObjectHashMap -> {
            ChatDetailFragment chatDetailFragment = ChatDetailFragment.newInstance("", "");
            loadFragment(chatDetailFragment, ChatDetailFragment.class.getSimpleName());
        });

        mainViewModel.getRefreshChatHistoryLiveData().observe(this, stringObjectHashMap -> refreshChat());

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
}
