package com.allsoft.chatapp.model.chats;

public class UserChat {
    private String endusers;
    private int sender;
    private String chat_desc;
    private String chat_title;
    private String when;

    private ChatData chat;

    public String getEndusers(){
        return endusers;
    }

    public void setEndusers(String end_users){
        this.endusers = end_users;
    }

    public String getChat_title(){
        return chat_title;
    }

    public void setChat_title(String chat_title){
        this.chat_title = chat_title;
    }

    public String getChat_desc(){
        return chat_desc;
    }

    public void setChat_desc(String chat_desc){
        this.chat_desc = chat_desc;
    }

    public int getSender(){
        return sender;
    }

    public void setSender(int sender){
        this.sender = sender;
    }

    public String getWhen(){
        return when;
    }

    public void setWhen(String when){
        this.when = when;
    }

    public ChatData getChat(){
        return chat;
    }

    public void setChat(ChatData chat){
        this.chat = chat;
    }
}
