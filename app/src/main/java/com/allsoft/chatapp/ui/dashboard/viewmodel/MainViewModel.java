package com.allsoft.chatapp.ui.dashboard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    private final MutableLiveData<HashMap<String, Object>> chatAdapterLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getChatAdapterLiveData(){
        return chatAdapterLiveData;
    }

    public void setChatAdapterLiveData(HashMap<String, Object> chatHistoryData){
        chatAdapterLiveData.setValue(chatHistoryData);
    }
}
