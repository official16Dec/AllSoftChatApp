package com.allsoft.chatapp.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allsoft.chatapp.ui.auth.LoginView;
import com.allsoft.chatapp.ui.dashboard.MainView;
import com.allsoft.chatapp.databinding.ActivitySplashBinding;
import com.allsoft.chatapp.utils.preference.MySharedPref;

public class SplashView extends AppCompatActivity {

    private ActivitySplashBinding binding;

    private MySharedPref mySharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        mySharedPref = new MySharedPref(this);


        new Handler().postDelayed(() -> {
            if(checkUserLogin()){
                initMain();
            }
            else{
                initAuth();
            }

        }, 8000);
    }

    private void initAuth() {
        startActivity(new Intent(this, LoginView.class));
        finish();
    }

    private void initMain() {
        startActivity(new Intent(this, MainView.class));
        finish();
    }

    private boolean checkUserLogin() {
        return mySharedPref.getPrefUserId(MySharedPref.prefUserId) != 0;
    }
}
