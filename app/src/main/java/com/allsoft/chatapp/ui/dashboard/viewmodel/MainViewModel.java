package com.allsoft.chatapp.ui.dashboard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allsoft.chatapp.model.chats.UserChat;
import com.allsoft.chatapp.model.user.EndUser;

import java.util.ArrayList;
import java.util.HashMap;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<HashMap<String, Object>> chatGroupLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getChatGroupLiveData(){
        return chatGroupLiveData;
    }

    public void setChatGroupLiveData(HashMap<String, Object> chatGroupData){
        chatGroupLiveData.setValue(chatGroupData);
    }


    private final MutableLiveData<HashMap<String, Object>> chatDetailLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getChatDetailLiveData(){
        return chatDetailLiveData;
    }

    public void setChatDetailLiveData(HashMap<String, Object> chatData){
        chatDetailLiveData.setValue(chatData);
    }

    private final MutableLiveData<HashMap<String, Object>> refreshChatHistoryLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getRefreshChatHistoryLiveData(){
        return refreshChatHistoryLiveData;
    }

    public void setRefreshChatHistoryLiveData(HashMap<String, Object> chatHistoryData){
        refreshChatHistoryLiveData.setValue(chatHistoryData);
    }

    private final MutableLiveData<HashMap<String, ArrayList<UserChat>>> chatGroupAdapterLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, ArrayList<UserChat>>> getChatGroupAdapterLiveData(){
        return chatGroupAdapterLiveData;
    }

    public void setChatGroupAdapterLiveData(HashMap<String, ArrayList<UserChat>> chatHistoryData){
        chatGroupAdapterLiveData.setValue(chatHistoryData);
    }


    private final MutableLiveData<HashMap<String, Object>> initGroupChatLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getGroupChatLiveData(){
        return initGroupChatLiveData;
    }

    public void setGroupChatLiveData(HashMap<String, Object> mapData){
        initGroupChatLiveData.setValue(mapData);
    }

    private final MutableLiveData<HashMap<String, ArrayList<UserChat>>> chatDetailAdapterLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, ArrayList<UserChat>>> getChatDetailAdapterLiveData(){
        return chatDetailAdapterLiveData;
    }

    public void setChatDetailAdapterLiveData(HashMap<String, ArrayList<UserChat>> chatHistoryData){
        chatDetailAdapterLiveData.setValue(chatHistoryData);
    }

    private final MutableLiveData<HashMap<String, Object>> updateGroupChatLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getUpdateGroupChatLiveData(){
        return updateGroupChatLiveData;
    }

    public void setUpdateGroupChatLiveData(HashMap<String, Object> groupChat){
        updateGroupChatLiveData.setValue(groupChat);
    }


    private final MutableLiveData<HashMap<String, Object>> createGroupLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getCreateGroupLiveData(){
        return createGroupLiveData;
    }

    public void setCreateGroupLiveData(HashMap<String, Object> groupChat){
        createGroupLiveData.setValue(groupChat);
    }

    private final MutableLiveData<HashMap<String, ArrayList<EndUser>>> userListLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, ArrayList<EndUser>>> getUserListLiveData(){
        return userListLiveData;
    }

    public void setUserListLiveData(HashMap<String, ArrayList<EndUser>> groupChat){
        userListLiveData.setValue(groupChat);
    }

    private final MutableLiveData<HashMap<String, ArrayList<UserChat>>> userGroupLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, ArrayList<UserChat>>> getUserGroupLiveData(){
        return userGroupLiveData;
    }

    public void setUserGroupLiveData(HashMap<String, ArrayList<UserChat>> groupChat){
        userGroupLiveData.setValue(groupChat);
    }
}
