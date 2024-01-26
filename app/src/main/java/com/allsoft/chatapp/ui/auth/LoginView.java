package com.allsoft.chatapp.ui.auth;

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

import java.util.HashMap;

public class LoginView extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        setObserver();


        loginViewModel.setLoginLiveData(new HashMap<>());

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
    }


    private void loadFragment(Fragment fragment, String fragmentKey){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(fragmentKey);
        fragmentTransaction.commit(); // save the changes
    }
}
