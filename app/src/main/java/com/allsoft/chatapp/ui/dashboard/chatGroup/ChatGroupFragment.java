package com.allsoft.chatapp.ui.dashboard.chatGroup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allsoft.chatapp.R;
import com.allsoft.chatapp.databinding.FragmentChatGroupBinding;
import com.allsoft.chatapp.model.chats.UserChat;
import com.allsoft.chatapp.ui.dashboard.MainView;
import com.allsoft.chatapp.ui.dashboard.chatGroup.adapter.UserChatAdapter;
import com.allsoft.chatapp.ui.dashboard.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.HashMap;

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

    private FragmentChatGroupBinding binding;

    private MainViewModel mainViewModel;

    private UserChatAdapter userChatAdapter;

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
    }

    private void setObserver() {

        mainViewModel.getChatAdapterLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                if(stringObjectHashMap.containsKey("chatList")){
                    ArrayList<UserChat> chatList = new ArrayList<>();
                    chatList.addAll((ArrayList)stringObjectHashMap.get("chatList"));
                    userChatAdapter.updateChat(chatList);
                }
            }
        });
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    private void initUserChatAdapter() {
        binding.userChatRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        userChatAdapter = new UserChatAdapter(requireActivity(), new UserChatAdapter.ChatHistoryCallback() {
            @Override
            public void setHistoryItemClicked(UserChat userChat) {

            }
        });
        binding.userChatRecycler.setAdapter(userChatAdapter);
    }
}