package com.allsoft.chatapp.model.user;


import androidx.annotation.Keep;

@Keep
public class EndUser {
    public int user_id;

    public String user_name;
    public String user_mobile;
    public String user_mail;
    public String user_password;


//    EndUser(int user_id, String user_name, String user_mobile, String user_mail){
//        this.user_id = user_id;
//        this.user_name = user_name;
//        this.user_mobile = user_mobile;
//        this.user_mail = user_mail;
//    }

    public int getUser_id(){
        return user_id;
    }

    public String getUser_name(){
        return user_name;
    }

    public String getUser_mobile(){
        return user_mobile;
    }

    public String getUser_mail(){
        return user_mail;
    }
    public String getUser_password(){
        return user_password;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }
    public void setUser_name(String user_name){
        this.user_name = user_name;
    }
    public void setUser_mail(String user_mail){
        this.user_mail = user_mail;
    }

    public void setUser_mobile(String user_mobile){
        this.user_mobile = user_mobile;
    }
    public void setUser_password(String user_password){
        this.user_password = user_password;
    }
}
