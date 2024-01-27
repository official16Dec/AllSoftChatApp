package com.allsoft.chatapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.allsoft.chatapp.R;
import com.allsoft.chatapp.databinding.ActivityLoginBinding;
import com.allsoft.chatapp.ui.auth.fragments.LoginFragment;
import com.allsoft.chatapp.ui.auth.fragments.SignUpFragment;
import com.allsoft.chatapp.ui.auth.viewmodel.LoginViewModel;
import com.allsoft.chatapp.ui.dashboard.MainView;
import com.allsoft.chatapp.utils.dbmanager.RealDatabaseManager;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginView extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private ActivityLoginBinding binding;

    RealDatabaseManager realDatabaseManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        realDatabaseManager = new RealDatabaseManager(this, new RealDatabaseManager.DatabaseCallback() {
            @Override
            public void databaseLoadingCallback(JSONObject result) {

            }
        });

        setObserver();


        loginViewModel.setLoginLiveData(new HashMap<>());

        loginViewModel.setManager(realDatabaseManager);

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
