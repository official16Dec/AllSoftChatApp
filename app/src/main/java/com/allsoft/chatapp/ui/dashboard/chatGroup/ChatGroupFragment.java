package com.allsoft.chatapp.ui.dashboard.chatGroup;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import com.allsoft.chatapp.databinding.FragmentChatGroupBinding;
import com.allsoft.chatapp.model.chats.UserChat;
import com.allsoft.chatapp.ui.dashboard.chatGroup.adapter.UserChatGroupAdapter;
import com.allsoft.chatapp.ui.dashboard.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatGroupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String TAG = "";

    private FragmentChatGroupBinding binding;

    private MainViewModel mainViewModel;

    private UserChatGroupAdapter userChatGroupAdapter;

    public ChatGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatGroupFragment newInstance(String param1, String param2) {
        ChatGroupFragment fragment = new ChatGroupFragment();
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

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(isEnabled()){
                    setEnabled(false);
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatGroupBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUserChatAdapter();

        initViewModel();

        setObserver();

        setProgressVisibility(false);
    }

    private void setObserver() {

        mainViewModel.getChatGroupAdapterLiveData().observe(getViewLifecycleOwner(), stringObjectHashMap -> {

            if(stringObjectHashMap.containsKey("chatList")){
                setProgressVisibility(true);
                ArrayList<UserChat> chatList = new ArrayList<>();
                Log.d(TAG, "List is "+stringObjectHashMap.get("chatList"));
                chatList.addAll(stringObjectHashMap.get("chatList"));
                if (chatList.size() > 0) {
                    userChatGroupAdapter.updateChat(chatList);
                }
            }
        });
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    private void initUserChatAdapter() {
        binding.userGroupRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        userChatGroupAdapter = new UserChatGroupAdapter(requireActivity(), new UserChatGroupAdapter.ChatHistoryCallback() {
            @Override
            public void setHistoryItemClicked(UserChat userChat) {
                HashMap mapData = new HashMap<String, Object>();
                mapData.put("endusers", userChat.getEndusers());
                mainViewModel.setChatDetailLiveData(mapData);
            }
        });

        binding.userGroupRecycler.setAdapter(userChatGroupAdapter);
    }

    private void setProgressVisibility(boolean shouldVisible){
        if(shouldVisible){
            binding.userGroupRecycler.setVisibility(View.VISIBLE);
            binding.chatGroupShimmerLayout.getRoot().setVisibility(View.GONE);
        }
        else{
            binding.userGroupRecycler.setVisibility(View.GONE);
            binding.chatGroupShimmerLayout.getRoot().setVisibility(View.VISIBLE);
        }
    }
}