package com.allsoft.chatapp.ui.dashboard.chatGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.allsoft.chatapp.R;
import com.allsoft.chatapp.databinding.ItemChatGroupBinding;
import com.allsoft.chatapp.model.chats.UserChat;

import java.util.ArrayList;

public class UserChatGroupAdapter extends RecyclerView.Adapter<UserChatGroupAdapter.UserChatViewHolder> {

    private final Context mContext;
    private final ChatHistoryCallback chatHistoryCallback;

    private ArrayList<UserChat> groupChatList;
    public UserChatGroupAdapter(Context mContext, ChatHistoryCallback chatHistoryCallback){
        this.mContext = mContext;
        this.chatHistoryCallback = chatHistoryCallback;

        groupChatList = new ArrayList<>();
    }

    public void updateChat(ArrayList<UserChat> chatList){
        groupChatList.addAll(groupChatList.size(), chatList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UserChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatGroupBinding binding = ItemChatGroupBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new UserChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserChatViewHolder holder, int position) {
        holder.chatImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher));
        holder.chatTitle.setText(groupChatList.get(position).getChatTitle());
        holder.chatDescription.setText(groupChatList.get(position).getChatDesc());
    }

    @Override
    public int getItemCount() {
        return groupChatList.size();
    }

    class UserChatViewHolder extends RecyclerView.ViewHolder{

        private ImageView chatImage;
        private TextView chatTitle;
        private TextView chatDescription;
        public UserChatViewHolder(@NonNull ItemChatGroupBinding itemView) {
            super(itemView.getRoot());

            chatImage = itemView.chatLogo;
            chatTitle = itemView.chatTitle;
            chatDescription = itemView.chatDesc;

            bindView();
        }

        public void bindView(){
            itemView.setOnClickListener(view -> chatHistoryCallback.setHistoryItemClicked(groupChatList.get(getAdapterPosition())));
        }
    }

    public interface ChatHistoryCallback{
        public void setHistoryItemClicked(UserChat userChat);
    }
}
