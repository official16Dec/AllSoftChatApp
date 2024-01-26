package com.allsoft.chatapp.ui.dashboard;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allsoft.chatapp.databinding.ActivityMainBinding;
import com.allsoft.chatapp.model.user.EndUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainView extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String TAG = MainView.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

    }
}
