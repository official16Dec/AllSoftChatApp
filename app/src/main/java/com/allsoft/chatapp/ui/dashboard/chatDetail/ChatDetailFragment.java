package com.allsoft.chatapp.ui.dashboard.chatDetail;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allsoft.chatapp.R;
import com.allsoft.chatapp.databinding.FragmentChatDetailBinding;
import com.allsoft.chatapp.model.chats.ChatData;
import com.allsoft.chatapp.model.chats.UserChat;
import com.allsoft.chatapp.ui.dashboard.chatDetail.adapter.UserChatDetailAdapter;
import com.allsoft.chatapp.ui.dashboard.chatGroup.ChatGroupFragment;
import com.allsoft.chatapp.ui.dashboard.creategroup.CreateGroupFragment;
import com.allsoft.chatapp.ui.dashboard.viewmodel.MainViewModel;
import com.allsoft.chatapp.utils.preference.MySharedPref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String TAG = ChatDetailFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentChatDetailBinding binding;

    private MainViewModel mainViewModel;

    private UserChatDetailAdapter userChatDetailAdapter;

    private MySharedPref mySharedPref;

    private boolean isForSend = false;

    public ChatDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatDetailFragment newInstance(String param1, String param2) {
        ChatDetailFragment fragment = new ChatDetailFragment();
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

                requireActivity().getSupportFragmentManager()
                        .popBackStack(ChatDetailFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

                if(requireActivity().getSupportFragmentManager().findFragmentByTag(CreateGroupFragment.class.getSimpleName())
                        == null){
                    mainViewModel.setUIMainLiveData(true);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatDetailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mySharedPref = new MySharedPref(requireActivity());

        initViewModel();

        setObserver();

        refreshGroupChat();

        setListeners();

        mainViewModel.setUIMainLiveData(false);

    }

    private void refreshGroupChat(){
        HashMap<String, Object> mapData = new HashMap<>();
        mapData.put("endusers", mParam1);
        mainViewModel.setGroupChatLiveData(mapData);
    }

    private void setListeners() {

        binding.chatMessageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.chatMessageEdit.getText().toString().isEmpty()){
                    isForSend = false;
                    binding.attachBtn.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_attach_icon));
                    binding.cameraBtn.setVisibility(View.VISIBLE);
                }
                else{
                    isForSend = true;
                    binding.attachBtn.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_send));
                    binding.cameraBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.attachBtn.setOnClickListener(view -> {
            if(isForSend){
                UserChat userChat = new UserChat();

                ChatData chatData = new ChatData();
                chatData.setChat_audio("");
                chatData.setChat_file("");
                chatData.setChat_image("");
                chatData.setChat_message(binding.chatMessageEdit.getText().toString());
                chatData.setChat_video("");

                userChat.setChat(chatData);
                userChat.setChat_title(mParam2);
                userChat.setChat_desc("");
                userChat.setEndusers(mParam1);
                userChat.setSender(mySharedPref.getPrefUserId(MySharedPref.prefUserId));
                userChat.setWhen(String.valueOf(System.currentTimeMillis()));

                HashMap<String, Object> mapData = new HashMap<>();
                mapData.put("user_chat", userChat);
                mapData.put("endusers", mParam1);

                mainViewModel.setUpdateGroupChatLiveData(mapData);

                binding.chatMessageEdit.setText("");

                refreshGroupChat();

            }
        });

        binding.cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserChat userChat = new UserChat();

                ChatData chatData = new ChatData();
                chatData.setChat_audio("");
                chatData.setChat_file("");
                chatData.setChat_image("");
                chatData.setChat_message("");
                chatData.setChat_video("");

                userChat.setChat(chatData);
                userChat.setChat_title(mParam2);
                userChat.setChat_desc("");
                userChat.setEndusers(mParam1);
                userChat.setSender(mySharedPref.getPrefUserId(MySharedPref.prefUserId));
                userChat.setWhen(String.valueOf(System.currentTimeMillis()));

                HashMap<String, Object> mapData = new HashMap<>();
                mapData.put("user_chat", userChat);
                mapData.put("endusers", mParam1);
                mainViewModel.setPickImageLiveData(mapData);
            }
        });
    }

    private void initRecyclerAdapter() {
        binding.userChatRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        userChatDetailAdapter = new UserChatDetailAdapter(requireActivity(), userChat -> {

        });

        binding.userChatRecycler.setAdapter(userChatDetailAdapter);
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    private void setObserver() {
        mainViewModel.getChatDetailAdapterLiveData().observe(getViewLifecycleOwner(), mapData -> {
            if(mapData.containsKey("chatList")){
                initRecyclerAdapter();
                ArrayList<UserChat> userChatList = new ArrayList<>();
                userChatList.addAll(mapData.get("chatList"));
                userChatDetailAdapter.updateChat(userChatList);
                binding.userChatRecycler.smoothScrollToPosition(userChatList.size()-1);

            }
        });
    }
}