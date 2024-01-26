package com.allsoft.chatapp.ui.dashboard.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class MainViewModel extends ViewModel {

    private MutableLiveData<HashMap<String, Object>> chatGroupLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getChatGroupLiveData(){
        return chatGroupLiveData;
    }

    public void setChatGroupLiveData(HashMap<String, Object> chatGroupData){
        chatGroupLiveData.setValue(chatGroupData);
    }


    private MutableLiveData<HashMap<String, Object>> chatDetailLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getChatDetailLiveData(){
        return chatDetailLiveData;
    }

    public void setChatDetailLiveData(HashMap<String, Object> chatData){
        chatDetailLiveData.setValue(chatData);
    }
}
