package com.allsoft.chatapp.ui.auth.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allsoft.chatapp.utils.dbmanager.RealDatabaseManager;

import java.util.HashMap;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<HashMap<String, Object>> loginLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getLoginLiveData(){
        return loginLiveData;
    }

    public void setLoginLiveData(HashMap<String, Object> loginData){
        loginLiveData.setValue(loginData);
    }


    private final MutableLiveData<HashMap<String, Object>> signUpLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getSignUpLiveData(){
        return signUpLiveData;
    }

    public void setSignUpLiveData(HashMap<String, Object> signUpData){
        signUpLiveData.setValue(signUpData);
    }


    private final MutableLiveData<HashMap<String, Object>> mainViewLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getMainView(){
        return mainViewLiveData;
    }

    public void setMainView(HashMap<String, Object> loginData){
        mainViewLiveData.setValue(loginData);
    }

    private final MutableLiveData<RealDatabaseManager> realDatabaseManagerLiveData = new MutableLiveData<>();

    public LiveData<RealDatabaseManager> getManager(){
        return realDatabaseManagerLiveData;
    }

    public void setManager(RealDatabaseManager realDatabaseManager){
        realDatabaseManagerLiveData.setValue(realDatabaseManager);
    }

    private final MutableLiveData<HashMap<String, Object>> signUpUserLiveData = new MutableLiveData<>();

    public LiveData<HashMap<String, Object>> getSignUpUserLiveData(){
        return signUpUserLiveData;
    }

    public void setSignUpUserLiveData(HashMap<String, Object>  mapData){
        signUpUserLiveData.setValue(mapData);
    }
}
