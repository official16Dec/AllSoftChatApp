package com.allsoft.chatapp.utils.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.allsoft.chatapp.app.BaseApplication;
import com.allsoft.chatapp.utils.preference.MySharedPref;
import com.google.firebase.messaging.FirebaseMessagingService;

public class NotificationService extends FirebaseMessagingService {


    private static final String TAG = NotificationService.class.getSimpleName();

    private final MySharedPref mySharedPref = new MySharedPref(BaseApplication.newInstance().getContext());
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        mySharedPref.setPrefFcmToken(MySharedPref.prefFcmToken, token);
    }

}
