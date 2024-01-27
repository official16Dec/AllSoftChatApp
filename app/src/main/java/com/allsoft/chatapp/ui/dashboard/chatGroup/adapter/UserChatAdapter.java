package com.allsoft.chatapp.ui.dashboard.chatGroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.UserChatViewHolder> {

    private final Context mContext;
    private final ChatHistoryCallback chatHistoryCallback;

    private ArrayList<UserChat> chatList;
    public UserChatAdapter(Context mContext, ChatHistoryCallback chatHistoryCallback){
        this.mContext = mContext;
        this.chatHistoryCallback = chatHistoryCallback;
    }

    public void updateChat(ArrayList<UserChat> chatList){
        chatList.addAll(chatList);
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
        holder.chatTitle.setText(chatList.get(position).getEndUsers());
        holder.chatDescription.setText(chatList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return 0;
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
            itemView.setOnClickListener(view -> chatHistoryCallback.setHistoryItemClicked(chatList.get(getAdapterPosition())));
        }
    }

    public interface ChatHistoryCallback{
        public void setHistoryItemClicked(UserChat userChat);
    }
}
