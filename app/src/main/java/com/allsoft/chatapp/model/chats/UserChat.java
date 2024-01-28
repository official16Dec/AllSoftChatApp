package com.allsoft.chatapp.model.chats;

public class UserChat {
    private String endusers;
    private int sender;
    private String chat_desc;
    private String chat_title;
    private String when;

    private ChatData chat;

    public String getEndUsers(){
        return endusers;
    }

    public void setEndUsers(String end_users){
        this.endusers = end_users;
    }

    public String getChatTitle(){
        return chat_title;
    }

    public void setChatTitle(String chat_title){
        this.chat_title = chat_title;
    }

    public String getChatDesc(){
        return chat_desc;
    }

    public void setChatDesc(String chat_desc){
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
