package com.allsoft.chatapp.ui.auth.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allsoft.chatapp.R;
import com.allsoft.chatapp.databinding.FragmentSignUpBinding;
import com.allsoft.chatapp.model.user.EndUser;
import com.allsoft.chatapp.ui.auth.viewmodel.LoginViewModel;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentSignUpBinding binding;

    private LoginViewModel loginViewModel;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();

        setListener();
    }

    private void initViewModel() {
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
    }

    private void setListener(){
        binding.signUpBtn.setOnClickListener(view1 -> {
            if(validateSignup()){
                EndUser user = new EndUser();
                user.setUser_name(binding.userNameEdit.getText().toString());
                user.setUser_mobile(binding.userMobileEdit.getText().toString());
                user.setUser_mail(binding.userMailEdit.getText().toString());
                user.setUser_password(binding.userPassEdit.getText().toString());
                user.setUser_profile_pic("");

                HashMap<String, Object> mapData = new HashMap<>();
                mapData.put("enduser", user);
                loginViewModel.setSignUpUserLiveData(mapData);
            }
        });
    }

    private boolean validateSignup(){
        boolean valid = false;
        if(binding.userNameEdit.getText().toString().isEmpty()){
            binding.userNameEdit.setError("Enter your name");
        }
        else{
            binding.userNameEdit.setError(null);
            valid = true;
        }

        if(binding.userMobileEdit.getText().toString().isEmpty()){
            binding.userMobileEdit.setError("Enter your mobile no");
        }
        else if(binding.userMobileEdit.getText().toString().length() != 10){
            binding.userMobileEdit.setError("Enter valid mobile no");
        }
        else{
            binding.userMobileEdit.setError(null);
            valid = true;
        }

        if(binding.userMailEdit.getText().toString().isEmpty()){
            binding.userMailEdit.setError("Enter your mail id");
        }
        else{
            binding.userMailEdit.setError(null);
            valid = true;
        }

        if(binding.userPassEdit.getText().toString().isEmpty()){
            binding.userPassEdit.setError("Enter password");
        }
        else{
            binding.userPassEdit.setError(null);
            valid = true;
        }

        return valid;
    }
}