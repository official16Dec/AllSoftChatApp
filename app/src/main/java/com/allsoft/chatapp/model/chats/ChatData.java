package com.allsoft.chatapp.model.chats;

public class ChatData {
    private String chat_message;
    private String chat_audio;
    private String chat_image;
    private String chat_video;
    private String chat_file;

    public String getChatMessage(){
        return chat_message;
    }

    public void setChatMessage(String chat_message){
        this.chat_message = chat_message;
    }

    public String getChatImage(){
        return chat_image;
    }

    public void setChatImage(String chat_image){
        this.chat_image = chat_image;
    }

    public String getChatAudio(){
        return chat_audio;
    }

    public void setChatAudio(String chat_audio){
        this.chat_audio = chat_audio;
    }

    public String getChatVideo(){
        return chat_video;
    }

    public void setChatVideo(String chat_video){
        this.chat_video = chat_video;
    }

    public String getChatFile(){
        return chat_file;
    }

    public void setChatFile(String chat_file){
        this.chat_file = chat_file;
    }
}
