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

    public int getUserId(){
        return user_id;
    }

    public String getUserName(){
        return user_name;
    }

    public String getUserMobile(){
        return user_mobile;
    }

    public String getUserMail(){
        return user_mail;
    }
    public String getUserPassword(){
        return user_password;
    }

    public void setUserId(int user_id){
        this.user_id = user_id;
    }
    public void setUserName(String user_name){
        this.user_name = user_name;
    }
    public void setUserMail(String user_mail){
        this.user_mail = user_mail;
    }

    public void setUserMobile(String user_mobile){
        this.user_mobile = user_mobile;
    }
    public void setUserPassword(String user_password){
        this.user_password = user_password;
    }
}
