package com.allsoft.chatapp.ui.dashboard;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.allsoft.chatapp.R;
import com.allsoft.chatapp.databinding.ActivityMainBinding;
import com.allsoft.chatapp.model.user.EndUser;
import com.allsoft.chatapp.ui.dashboard.chatDetail.ChatDetailFragment;
import com.allsoft.chatapp.ui.dashboard.chatGroup.ChatGroupFragment;
import com.allsoft.chatapp.ui.dashboard.viewmodel.MainViewModel;
import com.allsoft.chatapp.utils.dbmanager.RealDatabaseManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainView extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String TAG = MainView.class.getSimpleName();

    private MainViewModel mainViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        initViewModel();

        setObserver();

        refreshChat();

        mainViewModel.setChatGroupLiveData(new HashMap<>());

    }

    private void refreshChat() {
        RealDatabaseManager realDatabaseManager = new RealDatabaseManager(this);
        realDatabaseManager.setDatabaseCallback(() -> {

        });
    }

    private void setObserver() {
        mainViewModel.getChatGroupLiveData().observe(this, stringObjectHashMap -> {
            ChatGroupFragment chatGroupFragment = ChatGroupFragment.newInstance("", "");
            loadFragment(chatGroupFragment, ChatGroupFragment.class.getSimpleName());
        });

        mainViewModel.getChatDetailLiveData().observe(this, stringObjectHashMap -> {
            ChatDetailFragment chatDetailFragment = ChatDetailFragment.newInstance("", "");
            loadFragment(chatDetailFragment, ChatDetailFragment.class.getSimpleName());
        });
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void loadFragment(Fragment fragment, String fragmentKey){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(fragmentKey);
        fragmentTransaction.commit(); // save the changes
    }
}
