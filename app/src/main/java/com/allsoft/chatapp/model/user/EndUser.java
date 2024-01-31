package com.allsoft.chatapp.model.user;


import androidx.annotation.Keep;

@Keep
public class EndUser implements Comparable<EndUser>{
    private int user_id;

    private String user_name;
    private String user_mobile;
    private String user_mail;
    private String user_password;
    private String user_profile_pic;


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

    public String getUser_profile_pic(){
        return user_profile_pic;
    }

    public void setUser_profile_pic(String user_profile_pic){
        this.user_profile_pic = user_profile_pic;
    }

    @Override
    public int compareTo(EndUser endUser) {
        return Integer.compare(this.user_id, endUser.user_id);
    }
}
