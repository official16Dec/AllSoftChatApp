package com.allsoft.chatapp.model.chatmain;

import androidx.annotation.Keep;

import com.allsoft.chatapp.model.user.EndUsers;

@Keep
public class ChatApp {
    public EndUsers end;

    public String user_name;
    public String user_mobile;
    public String user_mail;


//    EndUser(int user_id, String user_name, String user_mobile, String user_mail){
//        this.user_id = user_id;
//        this.user_name = user_name;
//        this.user_mobile = user_mobile;
//        this.user_mail = user_mail;
//    }

//    public int getUserId(){
//        return user_id;
//    }

    public String getUserName(){
        return user_name;
    }

    public String getUserMobile(){
        return user_mobile;
    }

    public String getUserMail(){
        return user_mail;
    }

//    public void setUserId(int user_id){
//        this.user_id = user_id;
//    }
    public void setUserName(String user_name){
        this.user_name = user_name;
    }
    public void setUserMail(String user_mail){
        this.user_mail = user_mail;
    }

    public void setUserMobile(String user_mobile){
        this.user_mobile = user_mobile;
    }
}
