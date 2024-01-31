package com.allsoft.chatapp.ui.dashboard.creategroup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allsoft.chatapp.databinding.FragmentCreateGroupBinding;
import com.allsoft.chatapp.model.chats.ChatData;
import com.allsoft.chatapp.model.chats.UserChat;
import com.allsoft.chatapp.model.user.EndUser;
import com.allsoft.chatapp.ui.dashboard.chatDetail.ChatDetailFragment;
import com.allsoft.chatapp.ui.dashboard.chatGroup.ChatGroupFragment;
import com.allsoft.chatapp.ui.dashboard.creategroup.adapter.UserAdapter;
import com.allsoft.chatapp.ui.dashboard.viewmodel.MainViewModel;
import com.allsoft.chatapp.utils.preference.MySharedPref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGroupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String TAG = CreateGroupFragment.class.getSimpleName();

    private FragmentCreateGroupBinding binding;

    private MainViewModel mainViewModel;

    private UserAdapter userAdapter;

    private ArrayList<EndUser> userListToHandle;

    private MySharedPref mySharedPref;


    public CreateGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateGroupFragment newInstance(String param1, String param2) {
        CreateGroupFragment fragment = new CreateGroupFragment();
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
                        .popBackStack(CreateGroupFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mainViewModel.setUIMainLiveData(true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateGroupBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userListToHandle = new ArrayList<>();

        mySharedPref = new MySharedPref(requireActivity());

        initViewModel();

        setObserver();

        setListeners();


        EndUser endUser = new EndUser();
        endUser.setUser_id(mySharedPref.getPrefUserId(MySharedPref.prefUserId));
        endUser.setUser_name(mySharedPref.getPrefUserName(MySharedPref.prefUserName));
        endUser.setUser_mobile(mySharedPref.getPrefUserMobile(MySharedPref.prefUserMobile));
        endUser.setUser_mail(mySharedPref.getPrefUserMail(MySharedPref.prefUserMail));
        endUser.setUser_password("");
        endUser.setUser_profile_pic("");

        userListToHandle.add(endUser);

        mainViewModel.setUIMainLiveData(false);

    }

    private void setListeners(){
        binding.createGroupBtn.setOnClickListener(view -> createUserChatToSetDatabase());
    }
    private void initViewModel(){
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    private void setObserver(){
        mainViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), userListMap -> {
            if(userListMap.containsKey("userList")){
                initUserAdapter();
                userAdapter.updateUserList(userListMap.get("userList"));
            }
        });
    }

    private void initUserAdapter(){
        binding.userRecycler.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));

        userAdapter = new UserAdapter(requireActivity(), endUser -> {
            mainViewModel.setRefreshChatHistoryLiveData(new HashMap<>());

            if(userListToHandle.contains(endUser)){
                userListToHandle.remove(endUser);
                Toast.makeText(requireActivity(), endUser.getUser_id()+" Removed", Toast.LENGTH_SHORT).show();
            }else{
                userListToHandle.add(endUser);
                Toast.makeText(requireActivity(), endUser.getUser_id()+" Added", Toast.LENGTH_SHORT).show();
            }

            if(userListToHandle.size()>1){
                binding.createGroupBtn.setVisibility(View.VISIBLE);
            }
            else{
                binding.createGroupBtn.setVisibility(View.GONE);
            }

        });
        binding.userRecycler.setAdapter(userAdapter);
    }

    private void createUserChatToSetDatabase(){
        String endusers = getEndusers();
        Log.d("endusers", endusers);
        ArrayList<UserChat> userChatList = new ArrayList<>();

        for (EndUser endUser : userListToHandle){
            UserChat userChat = new UserChat();
            ChatData chatData = new ChatData();
            chatData.setChat_audio("");
            chatData.setChat_file("");
            chatData.setChat_image("");
            chatData.setChat_message("");
            chatData.setChat_video("");
            userChat.setChat(chatData);

            StringBuilder chatTitle = new StringBuilder();
            for (EndUser user : userListToHandle){
                if(!endUser.getUser_name().equals(user.getUser_name())){
                    if(chatTitle.length() == 0){
                        chatTitle = new StringBuilder("to " + user.getUser_name());
                    }
                    else{
                        chatTitle.append(", ").append(user.getUser_name());
                    }
                }
            }

            userChat.setEndusers(endusers);
            userChat.setChat_title(chatTitle.toString());
            userChat.setSender(endUser.getUser_id());
            userChat.setChat_desc("");
            userChat.setWhen(String.valueOf(System.currentTimeMillis()));

            userChatList.add(userChat);
        }

        HashMap<String, ArrayList<UserChat>> userGroupMap = new HashMap<>();
        userGroupMap.put("usergroup", userChatList);
        mainViewModel.setUserGroupLiveData(userGroupMap);

    }

    private String getEndusers(){
        Collections.sort(userListToHandle);

        StringBuilder endUsers = new StringBuilder();

        for(EndUser endUser : userListToHandle){
            if(endUsers.toString().equals("")){
                endUsers = new StringBuilder(String.valueOf(endUser.getUser_id()));
            }
            else{
                endUsers.append("V").append(endUser.getUser_id());
            }
        }

        return endUsers.toString();
    }
}