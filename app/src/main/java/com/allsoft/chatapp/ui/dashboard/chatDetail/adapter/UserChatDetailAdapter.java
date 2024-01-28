package com.allsoft.chatapp.ui.dashboard.chatDetail.adapter;

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
import com.allsoft.chatapp.databinding.ItemConversationBinding;
import com.allsoft.chatapp.databinding.ItemReceiverConversationBinding;
import com.allsoft.chatapp.databinding.ItemSenderConversationBinding;
import com.allsoft.chatapp.model.chats.UserChat;
import com.allsoft.chatapp.ui.dashboard.chatGroup.adapter.UserChatGroupAdapter;
import com.allsoft.chatapp.utils.preference.MySharedPref;

import java.util.ArrayList;

public class UserChatDetailAdapter extends RecyclerView.Adapter<UserChatDetailAdapter.UserChatViewHolder> {

    private final Context mContext;
    private final ChatHistoryCallback chatHistoryCallback;

    private MySharedPref mySharedPref;

    private ArrayList<UserChat> groupChatList;
    public UserChatDetailAdapter(Context mContext, ChatHistoryCallback chatHistoryCallback){
        this.mContext = mContext;
        this.chatHistoryCallback = chatHistoryCallback;

        mySharedPref = new MySharedPref(mContext);

        groupChatList = new ArrayList<>();
    }

    public void updateChat(ArrayList<UserChat> chatList){
        groupChatList.addAll(groupChatList.size(), chatList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UserChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConversationBinding binding = ItemConversationBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new UserChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserChatViewHolder holder, int position) {

        if(mySharedPref.getPrefUserId(MySharedPref.prefUserId) == groupChatList.get(position).getSender()){
            holder.senderConversation.getRoot().setVisibility(View.VISIBLE);
            if(!groupChatList.get(position).getChat().getChatMessage().isEmpty()){
                holder.senderConversation.chatMessage.setText(groupChatList.get(position).getChat().getChatMessage());
            }
        }
        else{
            holder.receiverConversation.getRoot().setVisibility(View.VISIBLE);
            if(!groupChatList.get(position).getChat().getChatMessage().isEmpty()){
                holder.receiverConversation.chatMessage.setText(groupChatList.get(position).getChat().getChatMessage());
            }
        }


    }

    @Override
    public int getItemCount() {
        return groupChatList.size();
    }

    class UserChatViewHolder extends RecyclerView.ViewHolder{

        private ItemReceiverConversationBinding receiverConversation;
        private ItemSenderConversationBinding senderConversation;
        public UserChatViewHolder(@NonNull ItemConversationBinding itemView) {
            super(itemView.getRoot());

            receiverConversation = itemView.receiverConvContainer;
            senderConversation = itemView.senderConvContainer;

            bindView();
        }

        public void bindView(){

        }
    }

    public interface ChatHistoryCallback{
        public void setHistoryItemClicked(UserChat userChat);
    }
}
