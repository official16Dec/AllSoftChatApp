package com.allsoft.chatapp.utils.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref {

    public static final String prefUserId = "user_id";
    public static final String prefUserName = "user_name";
    public static final String prefUserMobile = "user_mobile";
    public static final String prefUserMail = "user_mail";
    private Context ctx;
    private SharedPreferences pref;
    private SharedPreferences.Editor writer;

    public MySharedPref(Context ctx) {
        this.ctx = ctx;
        pref = ctx.getSharedPreferences("enduser", Context.MODE_PRIVATE);
        writer = pref.edit();
    }

    public int getPrefUserId(String key) {
        return pref.getInt(key, 0);
    }

    public String getPrefUserName(String key) {
        return pref.getString(key, "");
    }

    public String getPrefUserMobile(String key) {
        return pref.getString(key, "");
    }

    public String getPrefUserMail(String key) {
        return pref.getString(key, "");
    }

    public void setPrefUserId(String key, int value) {
        writer.putInt(key, value);
        writer.commit();
    }

    public void setPrefUserName(String key, String value) {
        writer.putString(key, value);
        writer.commit();
    }

    public void setPrefUserMobile(String key, String value) {
        writer.putString(key, value);
        writer.commit();
    }

    public void setPrefUserMail(String key, String value) {
        writer.putString(key, value);
        writer.commit();
    }


    public void clearData() {
        writer.clear().commit();

    }

}
