package com.allsoft.chatapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.allsoft.chatapp.R;
import com.allsoft.chatapp.databinding.ActivityLoginBinding;
import com.allsoft.chatapp.model.user.EndUser;
import com.allsoft.chatapp.ui.auth.fragments.LoginFragment;
import com.allsoft.chatapp.ui.auth.fragments.SignUpFragment;
import com.allsoft.chatapp.ui.auth.viewmodel.LoginViewModel;
import com.allsoft.chatapp.ui.dashboard.MainView;
import com.allsoft.chatapp.utils.dbmanager.RealDatabaseManager;
import com.allsoft.chatapp.utils.preference.MySharedPref;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class LoginView extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private ActivityLoginBinding binding;

    RealDatabaseManager realDatabaseManager;

    JSONObject jsonResult;

    MySharedPref mySharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        mySharedPref = new MySharedPref(this);

        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        initRealDatabaseManager();

        setObserver();

        initFirebaseToken();

        loginViewModel.setLoginLiveData(new HashMap<>());

        loginViewModel.setManager(realDatabaseManager);

    }

    private void initFirebaseToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> mySharedPref.setPrefFcmToken(MySharedPref.prefFcmToken, task.getResult()));
    }

    private void initRealDatabaseManager() {
        realDatabaseManager = new RealDatabaseManager(this, new RealDatabaseManager.DatabaseCallback() {
            @Override
            public void databaseLoadingCallback(JSONObject result) {
                jsonResult = result;
            }

            @Override
            public void groupDetailCallBack(String endusers) {

            }
        });
    }


    private void setObserver(){
        loginViewModel.getLoginLiveData().observe(this, stringObjectHashMap -> {
            LoginFragment loginFragment = LoginFragment.newInstance("", "");
            loadFragment(loginFragment, LoginFragment.class.getSimpleName());
        });

        loginViewModel.getSignUpLiveData().observe(this, stringObjectHashMap -> {
            SignUpFragment signUpFragment = SignUpFragment.newInstance("", "");
            loadFragment(signUpFragment, SignUpFragment.class.getSimpleName());
        });

        loginViewModel.getMainView().observe(this, stringObjectHashMap -> startMain());

        loginViewModel.getSignUpUserLiveData().observe(this, mapData -> {
            if(mapData.containsKey("enduser")){
                EndUser user = (EndUser)mapData.get("enduser");
                initRealDatabaseManager();

                try{

                    if(jsonResult.has("endusers")) {
                        JSONObject userData = jsonResult.getJSONObject("endusers");
                        Iterator<String> keys = userData.keys();

                        int userID = 0;
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject userJsonObj = userData.getJSONObject(key);
                            userID = userJsonObj.getInt("user_id");
                        }
                        userID = userID + 1;
                        user.setUser_id(userID);

                        realDatabaseManager.registerUser(user, "user" + userID);

                        loginViewModel.setLoginLiveData(new HashMap<>());
                    }
                    else{
                        user.setUser_id(1);

                        realDatabaseManager.registerUser(user, "user" + 1);

                        loginViewModel.setLoginLiveData(new HashMap<>());
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void startMain() {
        startActivity(new Intent(this, MainView.class));
        finish();
    }


    private void loadFragment(Fragment fragment, String fragmentKey){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(fragmentKey);
        fragmentTransaction.commit(); // save the changes
    }
}
