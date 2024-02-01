package com.allsoft.chatapp.app;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class BaseApplication extends Application {
    private Context mContext;

    public Context getContext(){
        return mContext;
    }

    public static BaseApplication newInstance(){
        return new BaseApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        FirebaseApp.initializeApp(this);
    }
}
