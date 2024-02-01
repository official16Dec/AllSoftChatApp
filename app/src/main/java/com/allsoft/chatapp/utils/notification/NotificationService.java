package com.allsoft.chatapp.utils.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.allsoft.chatapp.app.BaseApplication;
import com.allsoft.chatapp.utils.preference.MySharedPref;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationService extends FirebaseMessagingService {


    private static final String TAG = NotificationService.class.getSimpleName();

    private final MySharedPref mySharedPref = new MySharedPref(BaseApplication.newInstance().getContext());
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        mySharedPref.setPrefFcmToken(MySharedPref.prefFcmToken, token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

    }

    public void sendNotification(String targetUserToken, String title, String message) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"to\":\"" + targetUserToken + "\",\n\t\"notification\": {\n\t\t\"title\":\"" + title + "\",\n\t\t\"body\":\"" + message + "\"\n\t}\n}");
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=FIREBASE_SERVER_KEY")
                .build();

        try {
            Response response = client.newCall(request).execute();
            // Handle the response if needed
            String responseBody = response.body().string();
            Log.d("FCM Response", responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
