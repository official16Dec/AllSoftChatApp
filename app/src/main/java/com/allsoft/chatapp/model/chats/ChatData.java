package com.allsoft.chatapp.model.chats;

public class ChatData {
    private String chat_message;
    private String chat_audio;
    private String chat_image;
    private String chat_video;
    private String chat_file;

    public String getChat_message(){
        return chat_message;
    }

    public void setChat_message(String chat_message){
        this.chat_message = chat_message;
    }

    public String getChat_image(){
        return chat_image;
    }

    public void setChat_image(String chat_image){
        this.chat_image = chat_image;
    }

    public String getChat_audio(){
        return chat_audio;
    }

    public void setChat_audio(String chat_audio){
        this.chat_audio = chat_audio;
    }

    public String getChat_video(){
        return chat_video;
    }

    public void setChat_video(String chat_video){
        this.chat_video = chat_video;
    }

    public String getChat_file(){
        return chat_file;
    }

    public void setChat_file(String chat_file){
        this.chat_file = chat_file;
    }
}
