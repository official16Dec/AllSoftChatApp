package com.allsoft.chatapp.utils.firebaseStorage;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageManager {

    private StorageReference storageRef;
    private StorageActivityCallback storageActivityCallback;
    public FirebaseStorageManager(StorageActivityCallback storageCallback){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        storageActivityCallback = storageCallback;
    }

    public void addImageToStorage(Uri fileUri){
        StorageReference imageRef = storageRef.child("images/" + fileUri.getLastPathSegment());


        UploadTask uploadTask = imageRef.putFile(fileUri);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // File uploaded successfully
                // You can get the download URL for the uploaded file
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    storageActivityCallback.onImageUploaded(downloadUrl);
                    // Now you can store this download URL in your Firebase Realtime Database or use it as needed.
                    // For example, save the URL along with other details in the database.
                    // Be cautious about the security rules of your Firebase Storage to control access.
                });
            } else {
                // Handle unsuccessful upload
                Exception exception = task.getException();
                // Handle the exception as needed
            }
        });
    }


    public interface StorageActivityCallback{

        void onImageUploaded(String downloadUrl);
    }
}
