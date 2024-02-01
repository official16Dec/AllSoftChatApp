package com.allsoft.chatapp.ui.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.allsoft.chatapp.utils.firebaseStorage.FirebaseStorageManager;
import com.allsoft.chatapp.utils.notification.NotificationService;
import com.allsoft.chatapp.utils.preference.MySharedPref;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainView extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String TAG = MainView.class.getSimpleName();

    private MainViewModel mainViewModel;

    private MySharedPref mySharedPref;

    private RealDatabaseManager realDatabaseManager;

    private FirebaseStorageManager firebaseStorageManager;

    private ActivityResultLauncher<Intent> pickImageLauncher;

    private UserChat pickImageUserChat;
    private String pickImageEndUsers;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityMainBinding.inflate(getLayoutInflater());

        mySharedPref = new MySharedPref(this);

        setContentView(binding.getRoot());

        initViewModel();

        setObserver();

        refreshGroups();

        initStorage();

        mainViewModel.setChatGroupLiveData(new HashMap<>());

        setListener();

        pickImageResultLauncher();

    }

    private void pickImageResultLauncher() {
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Handle the result here
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            firebaseStorageManager.addImageToStorage(selectedImageUri);
                        }
                    }
                });
    }

    private void initStorage() {
        firebaseStorageManager = new FirebaseStorageManager(new FirebaseStorageManager.StorageActivityCallback() {
            @Override
            public void onImageUploaded(String downloadUrl) {
                pickImageUserChat.getChat().setChat_image(downloadUrl);
                updateGroupChat(pickImageUserChat, pickImageEndUsers);
            }
        });
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
        realDatabaseManager = new RealDatabaseManager(this, new RealDatabaseManager.DatabaseCallback() {
            @Override
            public void databaseLoadingCallback(JSONObject result) {
                manageChatHistory(result);
            }

            @Override
            public void groupDetailCallBack(String endusers) {
                HashMap<String, Object> mapData = new HashMap<>();
                mapData.put("endusers", endusers);
                mainViewModel.setChatDetailLiveData(mapData);
            }

            @Override
            public void getChatListCallback(ArrayList<UserChat> groupChatList) {

                try {
                    ArrayList<UserChat> userChatList = new ArrayList<>();

                    for(UserChat userChat : groupChatList){
                        if(userChat.getChat() != null){
                            if(!userChat.getChat().getChat_message().equals("")){
                                userChatList.add(userChat);
                            }
                        }
                    }


                    HashMap<String, ArrayList<UserChat>> chatHistoryData = new HashMap<>();
                    chatHistoryData.put("chatList", userChatList);
                    mainViewModel.setChatDetailAdapterLiveData(chatHistoryData);

                    //Sending notification
                    String[] users = userChatList.get(0).getEndusers().split("V");
                    if(users.length >1){
                        if(Integer.parseInt(users[0])!=mySharedPref.getPrefUserId(MySharedPref.prefUserId)){
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("enduser", realDatabaseManager.getEndUserById(Integer.parseInt(users[0])));
//                            mainViewModel.setNotificationLiveData(userMap);
                        }

                        if(Integer.parseInt(users[1])!=mySharedPref.getPrefUserId(MySharedPref.prefUserId)){
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("enduser", realDatabaseManager.getEndUserById(Integer.parseInt(users[1])));
//                            mainViewModel.setNotificationLiveData(userMap);
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

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
                        break;
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
        mainViewModel.getUIMainLiveData().observe(this, uiMainFlag -> {
            if(uiMainFlag){
                binding.mainBarTitle.setText("Chat History");
                binding.composeNewBtn.setVisibility(View.VISIBLE);
            }
            else{
                binding.composeNewBtn.setVisibility(View.GONE);
            }
        });

        mainViewModel.getChatGroupLiveData().observe(this, stringObjectHashMap -> {
            ChatGroupFragment chatGroupFragment = ChatGroupFragment.newInstance("", "");
            loadFragment(chatGroupFragment, ChatGroupFragment.class.getSimpleName());
        });

        mainViewModel.getChatDetailLiveData().observe(this, mapData -> {
            if(mapData.containsKey("endusers")){

                getChatGroupTitle(String.valueOf(mapData.get("endusers")));
                ChatDetailFragment chatDetailFragment = ChatDetailFragment.newInstance(String.valueOf(mapData.get("endusers")),
                        binding.mainBarTitle.getText().toString());
                loadFragment(chatDetailFragment, ChatDetailFragment.class.getSimpleName());

            }

        });

        mainViewModel.getRefreshChatHistoryLiveData().observe(this, stringObjectHashMap -> refreshGroups());

        mainViewModel.getGroupChatLiveData().observe(this, mapData -> {
            if(mapData.containsKey("endusers")){
                refreshGroups();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getGroupConversation(String.valueOf(mapData.get("endusers")));
                    }
                }, 1500);

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

        mainViewModel.getNotificationLiveData().observe(this, mapData->{
            if(mapData.containsKey("enduser")){
                EndUser endUser = (EndUser) mapData.get("enduser");
                NotificationService.newInstance().sendNotification(endUser.getFcm_token(), "New message received", "New message received");
            }
        });

        mainViewModel.getPickImageLiveData().observe(this, new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> mapData) {
                if(mapData.containsKey("user_chat")){
                    pickImageUserChat = (UserChat) mapData.get("user_chat");
                    pickImageEndUsers = String.valueOf(mapData.get("endusers"));
                    checkForStoragePermission();
                }
            }
        });

    }

    private void getGroupConversation(String endusers) {

        realDatabaseManager.getAllGroupData(endusers);

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


    private void checkForStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                // Request permissions if not granted
                askForStoragePermission();
            } else {
                // Permissions already granted, proceed with your logic
                pickImage();
            }
        } else {
            // Permissions are not required for lower Android versions
            pickImage();
        }
    }
    private void askForStoragePermission(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // Check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            pickImage();
                        } else {
                            // Handle case when some permissions are denied
//                            handleDeniedPermissions(report.getDeniedPermissionResponses());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        // Handle permission rationale if needed
                        // You can show a dialog explaining why the permission is necessary and ask the user to grant it
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void pickImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }
}
