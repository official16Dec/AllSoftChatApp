package com.allsoft.chatapp.ui.dashboard.creategroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.allsoft.chatapp.R;
import com.allsoft.chatapp.databinding.ItemCreateGroupBinding;
import com.allsoft.chatapp.model.user.EndUser;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context mContext;
    private final UserSelectCallback userSelectCallback;
    private final ArrayList<EndUser> userList;
    public UserAdapter(Context mContext, UserSelectCallback userSelectCallback){
        this.mContext = mContext;
        this.userSelectCallback = userSelectCallback;

        userList = new ArrayList<>();
    }

    public void updateUserList(ArrayList<EndUser> userlist){
        userList.addAll(userList.size(), userlist);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCreateGroupBinding binding = ItemCreateGroupBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.userProfilePic.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher));
        holder.userName.setText(userList.get(position).getUser_name());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        private ImageView userProfilePic;
        private TextView userName;
        private TextView userInfo;

        private CheckBox selectUserCheck;
        public UserViewHolder(@NonNull ItemCreateGroupBinding itemView) {
            super(itemView.getRoot());

            userProfilePic = itemView.userProfilePic;
            userName = itemView.userName;
            userInfo = itemView.userInfo;
            selectUserCheck = itemView.selectUserCheck;

            bindView();
        }

        public void bindView(){
            itemView.setOnClickListener(view -> {
                userSelectCallback.setItemClicked(userList.get(getAdapterPosition()));
                selectUserCheck.setChecked(!selectUserCheck.isChecked());
            });
        }
    }

    public interface UserSelectCallback{
        public void setItemClicked(EndUser endUser);
    }
}
