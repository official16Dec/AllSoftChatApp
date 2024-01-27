package com.allsoft.chatapp.model.chats;

public class UserChat {
    private String end_users;
    private String message;
    private String media_audio;
    private String media_photo;
    private String media_video;

    public String getEndUsers(){
        return end_users;
    }

    public void setEndUsers(String end_users){
        this.end_users = end_users;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMediaPhoto(){
        return media_photo;
    }

    public void setMediaPhoto(String media_photo){
        this.media_photo = media_photo;
    }

    public String getMediaAudio(){
        return media_audio;
    }

    public void setMediaAudio(String media_audio){
        this.media_audio = media_audio;
    }

    public String getMediaVideo(){
        return media_video;
    }

    public void setMediaVideo(String media_video){
        this.media_video = media_video;
    }
}
