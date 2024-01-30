package com.allsoft.chatapp.ui.dashboard.creategroup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allsoft.chatapp.databinding.FragmentCreateGroupBinding;
import com.allsoft.chatapp.model.chats.ChatData;
import com.allsoft.chatapp.model.chats.UserChat;
import com.allsoft.chatapp.model.user.EndUser;
import com.allsoft.chatapp.ui.dashboard.creategroup.adapter.UserAdapter;
import com.allsoft.chatapp.ui.dashboard.viewmodel.MainViewModel;
import com.allsoft.chatapp.utils.preference.MySharedPref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
    private ArrayList<UserChat> userChatList;

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

        initUserAdapter();

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

    }

    private void setListeners(){
        binding.createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserChatToSetDatabase();
            }
        });
    }
    private void initViewModel(){
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    private void setObserver(){
        mainViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), userListMap -> {
            if(userListMap.containsKey("userList")){
                Log.d(TAG, "Size is : "+userListMap.get("userList").size());
                userAdapter.updateUserList(userListMap.get("userList"));
            }
        });
    }

    private void initUserAdapter(){
        binding.userRecycler.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));

        userAdapter = new UserAdapter(requireActivity(), endUser -> {
            if(userListToHandle.contains(endUser)){
                userListToHandle.remove(endUser);
                Toast.makeText(requireActivity(), endUser.getUser_id()+" Removed", Toast.LENGTH_SHORT).show();
            }else{
                userListToHandle.add(endUser);
                Toast.makeText(requireActivity(), endUser.getUser_id()+" Added", Toast.LENGTH_SHORT).show();
            }
            Collections.sort(userListToHandle);

        });
        binding.userRecycler.setAdapter(userAdapter);
    }

    private void createUserChatToSetDatabase(){
        String endUsers = "";

        for(EndUser endUser : userListToHandle){
            if(endUsers.isEmpty()){
                endUsers = String.valueOf(endUser.getUser_id());
            }
            else{
                endUsers = endUsers+"V"+endUser.getUser_id();
            }
        }

        userChatList = new ArrayList<>();

        for (EndUser endUser : userListToHandle){
            UserChat userChat = new UserChat();
            ChatData chatData = new ChatData();
            chatData.setChat_audio("");
            chatData.setChat_file("");
            chatData.setChat_image("");
            chatData.setChat_message("");
            chatData.setChat_video("");

            String chatTitle = "";
            for (EndUser user : userListToHandle){
                if(!endUser.getUser_name().equals(user.getUser_name())){
                    if(chatTitle.isEmpty()){
                        chatTitle = "to "+user.getUser_name();
                    }
                    else{
                        chatTitle = chatTitle +", "+user.getUser_name();
                    }
                }
            }
            userChat.setEndusers(endUsers);
            userChat.setChatTitle(chatTitle);
            userChat.setSender(endUser.getUser_id());
            userChat.setChatDesc("");
            userChat.setWhen("");

            userChatList.add(userChat);
        }

        HashMap<String, ArrayList<UserChat>> userGroupMap = new HashMap<>();
        userGroupMap.put("usergroup", userChatList);
        mainViewModel.setUserGroupLiveData(userGroupMap);

    }
}